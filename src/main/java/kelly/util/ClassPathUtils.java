package kelly.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;

import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

/**
 * ClassPath相关通用工具
 * 
 * @author 陈国强(subchen@gmail.com)
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class ClassPathUtils {

	private static final PathMatcher PATH_MATCHER = new AntStylePathMatcher(); 
	
	public static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
	public static final String APP_CLASS_LOADER_NAME = "sun.misc.Launcher$AppClassLoader";
	
	private ClassPathUtils() {
		super();
	}

	// ----------------------------------------------------------
	
	public static boolean isJarExists(String jarPattern) {
		if (jarPattern == null || jarPattern.length() == 0) {
			return false;
		}
		Collection<URL> urls = new HashSet<URL>(32);
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		while (cl != null) {
			if (EXT_CLASS_LOADER_NAME.equals(cl.getClass().getName())) {
				break;
			}
			if (cl instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader) cl).getURLs()) {
					urls.add(url);
				}
			}
			cl = cl.getParent();
		}
		
		for (URL url : urls) {
			String filename = url.getFile();
			System.out.println(filename);
			if (PATH_MATCHER.match(jarPattern, filename)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isJarExists("/**/jetbrick-template-*.jar"));
		System.out.println(isJarExists("/**/jetbrick-mvc-*.jar"));
		System.out.println(isJarExists("/**/jetbrick-orm-*.jar"));
	}
}
