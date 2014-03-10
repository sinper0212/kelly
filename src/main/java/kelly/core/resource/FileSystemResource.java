package kelly.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import kelly.util.FilenameUtils;
import kelly.util.Validate;


public class FileSystemResource extends AbstractResource implements WritableResource {

	private final File file;
	private final String path;

	public FileSystemResource(File file) {
		Validate.notNull(file, "File must not be null");
		this.file = file;
		this.path = FilenameUtils.normalize(file.getPath(), true);
	}

	public FileSystemResource(String path) {
		Validate.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = FilenameUtils.normalize(file.getPath(), true);
	}

	public final String getPath() {
		return this.path;
	}

	@Override
	public boolean exists() {
		return this.file.exists();
	}

	@Override
	public boolean isReadable() {
		return (this.file.canRead() && !this.file.isDirectory());
	}

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

	@Override
	public URI getURI() throws IOException {
		return this.file.toURI();
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public long contentLength() throws IOException {
		return this.file.length();
	}

	@Override
	public Resource createRelative(String relativePath) {
		String pathToUse = ResourceUtils.applyRelativePath(this.path, relativePath);
		return new FileSystemResource(pathToUse);
	}

	@Override
	public String getFilename() {
		return this.file.getName();
	}

	// implementation of WritableResource
	@Override
	public boolean isWritable() {
		return (this.file.canWrite() && !this.file.isDirectory());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.file);
	}

}
