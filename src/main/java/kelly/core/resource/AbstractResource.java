package kelly.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import kelly.util.Validate;

public abstract class AbstractResource implements Resource {

	public boolean exists() {
		// Try file existence: can we find the file in the file system?
		try {
			return getFile().exists();
		} catch (IOException ex) {
			// Fall back to stream existence: can we open the stream?
			try {
				InputStream is = getInputStream();
				is.close();
				return true;
			} catch (Throwable isEx) {
				return false;
			}
		}
	}

	public boolean isReadable() {
		return true;
	}

	public boolean isOpen() {
		return false;
	}

	public URL getURL() throws IOException {
		throw new FileNotFoundException("Cannot be resolved to URL");
	}

	public URI getURI() throws IOException {
		URL url = getURL();
		try {
			return ResourceUtils.toURI(url);
		} catch (URISyntaxException ex) {
			throw new IOException("Invalid URI [" + url + "]", ex);
		}
	}

	public File getFile() throws IOException {
		throw new FileNotFoundException("Cannot be resolved to absolute file path");
	}

	public long contentLength() throws IOException {
		InputStream is = this.getInputStream();
		Validate.isTrue(is != null, "resource input stream must not be null");
		try {
			long size = 0;
			byte[] buf = new byte[255];
			int read;
			while ((read = is.read(buf)) != -1) {
				size += read;
			}
			return size;
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
			}
		}
	}

	public long lastModified() throws IOException {
		long lastModified = getFileForLastModifiedCheck().lastModified();
		if (lastModified == 0L) {
			throw new FileNotFoundException(
					"Cannot be resolved in the file system for resolving its last-modified timestamp");
		}
		return lastModified;
	}

	protected File getFileForLastModifiedCheck() throws IOException {
		return getFile();
	}

	public Resource createRelative(String relativePath) throws IOException {
		throw new FileNotFoundException("Cannot create a relative resource");
	}

	public String getFilename() {
		return null;
	}

}
