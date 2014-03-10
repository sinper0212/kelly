package kelly.core.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import kelly.util.Validate;


public class UrlResource extends AbstractFileResolvingResource {

	private final URI uri;
	private final URL url;

	/**
	 * Cleaned URL (with normalized path), used for comparisons.
	 */
//	private final URL cleanedUrl;

	public UrlResource(URI uri) throws MalformedURLException {
		Validate.notNull(uri, "URI must not be null");
		this.uri = uri;
		this.url = uri.toURL();
//		this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
	}

	/**
	 * Create a new UrlResource based on the given URL object.
	 * @param url a URL
	 */
	public UrlResource(URL url) {
		Validate.notNull(url, "URL must not be null");
		this.url = url;
//		this.cleanedUrl = getCleanedUrl(this.url, url.toString());
		this.uri = null;
	}

	public UrlResource(String path) throws MalformedURLException {
		Validate.notNull(path, "Path must not be null");
		this.uri = null;
		this.url = new URL(path);
//		this.cleanedUrl = getCleanedUrl(this.url, path);
	}

	public UrlResource(String protocol, String location) throws MalformedURLException  {
		this(protocol, location, null);
	}

	public UrlResource(String protocol, String location, String fragment) throws MalformedURLException  {
		try {
			this.uri = new URI(protocol, location, fragment);
			this.url = this.uri.toURL();
//			this.cleanedUrl = getCleanedUrl(this.url, this.uri.toString());
		}
		catch (URISyntaxException ex) {
			MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
			exToThrow.initCause(ex);
			throw exToThrow;
		}
	}

//	@SuppressWarnings("unused")
//	private URL getCleanedUrl(URL originalUrl, String originalPath) {
//		try {
//			return new URL(FilenameUtils.normalize(originalPath, true));
//		}
//		catch (MalformedURLException ex) {
//			// Cleaned URL path cannot be converted to URL
//			// -> take original URL.
//			return originalUrl;
//		}
//	}


	public InputStream getInputStream() throws IOException {
		URLConnection con = this.url.openConnection();
		ResourceUtils.useCachesIfNecessary(con);
		try {
			return con.getInputStream();
		}
		catch (IOException ex) {
			// Close the HTTP connection (if applicable).
			if (con instanceof HttpURLConnection) {
				((HttpURLConnection) con).disconnect();
			}
			throw ex;
		}
	}

	@Override
	public URL getURL() throws IOException {
		return this.url;
	}

	@Override
	public URI getURI() throws IOException {
		if (this.uri != null) {
			return this.uri;
		}
		else {
			return super.getURI();
		}
	}

	@Override
	public File getFile() throws IOException {
		if (this.uri != null) {
			return super.getFile(this.uri);
		}
		else {
			return super.getFile();
		}
	}

	@Override
	public Resource createRelative(String relativePath) throws MalformedURLException {
		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}
		return new UrlResource(new URL(this.url, relativePath));
	}

	@Override
	public String getFilename() {
		return new File(this.url.getFile()).getName();
	}

}
