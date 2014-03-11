package kelly.util;

import java.net.URL;
import java.net.URLClassLoader;

import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

/**
 * ClassPath相关通用工具
 * 
 * @author 陈国强(subchen@gmail.com)
 * @author 应卓(yingzhor@gmail.com)
 *
 * @since 1.0.0
 */
public class ClassPathUtils {

	private static final PathMatcher PATH_MATCHER = new AntStylePathMatcher(); 
	
	private static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
//	private static final String APP_CLASS_LOADER_NAME = "sun.misc.Launcher$AppClassLoader";
	
	// --------------------------------------------------------------------------------

	private ClassPathUtils() {
		super();
	}

	// --------------------------------------------------------------------------------
	
	/**
	 * 查看jar是否存在于classpath
	 * 
	 * @param jarPattern 如"/**\/jetbrick-template-*.jar"
	 * @return true时说明ClassPath中有相应的jar
	 */
	public static boolean isJarExists(String jarPattern) {
		if (jarPattern == null || jarPattern.length() == 0) {
			return false;
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		while (cl != null) {
			if (EXT_CLASS_LOADER_NAME.equals(cl.getClass().getName())) {
				break;
			}
			if (cl instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader) cl).getURLs()) {
					String filename = url.getFile();
					if (PATH_MATCHER.match(jarPattern, filename)) {
						return true;
					}
				}
			}
			cl = cl.getParent();
		}
		return false;
	}

}
