package kelly.core.resource;

import java.net.MalformedURLException;
import java.net.URL;

import kelly.util.ClassLoaderUtils;
import kelly.util.Validate;

/**
 * 简易的ResourceLoader (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
class SimpleResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;

	public SimpleResourceLoader() {
		this.classLoader = ClassLoaderUtils.getDefaultClassLoader();
	}

	public SimpleResourceLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return (this.classLoader != null ? this.classLoader : ClassLoaderUtils.getDefaultClassLoader());
	}

	public Resource getResource(String location) {
		Validate.notNull(location, "Location must not be null");
		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
			return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
		}
		else {
			try {
				// Try to parse the location as a URL...
				URL url = new URL(location);
				return new UrlResource(url);
			}
			catch (MalformedURLException ex) {
				// No URL -> resolve as resource path.
				return getResourceByPath(location);
			}
		}
	}

	protected Resource getResourceByPath(String path) {
		return new ClassPathContextResource(path, getClassLoader());
	}

	private static class ClassPathContextResource extends ClassPathResource {
		public ClassPathContextResource(String path, ClassLoader classLoader) {
			super(path, classLoader);
		}

		@Override
		public Resource createRelative(String relativePath) {
			String pathToUse = ResourceUtils.applyRelativePath(getPath(), relativePath);
			return new ClassPathContextResource(pathToUse, getClassLoader());
		}
	}

}
