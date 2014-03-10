package kelly.core.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 本接口完成对文件或URL的抽象
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see ResourceLoader
 * @see ResourcePatternResolver
 * @see SmartResourceLoader
 * @see ClassPathResource
 * @see FileSystemResource
 * @see UrlResource
 * @see ByteArrayResource
 */
public interface Resource {

	InputStream getInputStream() throws IOException;

	boolean exists();

	boolean isReadable();

	boolean isOpen();

	URL getURL() throws IOException;

	URI getURI() throws IOException;

	File getFile() throws IOException;

	long contentLength() throws IOException;

	long lastModified() throws IOException;

	Resource createRelative(String relativePath) throws IOException;

	String getFilename();

}
