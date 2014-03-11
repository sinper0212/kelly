package kelly.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * ClassLoader相关通用工具
 * 
 * @author 应卓
 *
 */
public final class ClassLoaderUtils {

	private ClassLoaderUtils() {
	}

	// ----------------------------------------------------------------------------------
	
	public static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
	public static final String APP_CLASS_LOADER_NAME = "sun.misc.Launcher$AppClassLoader";
	
	private static final Map<String, String> abbreviationMap;
	
	static {
		abbreviationMap = new HashMap<String, String>();
		abbreviationMap.put("boolean", "Z");
		abbreviationMap.put("byte", "B");
		abbreviationMap.put("short", "S");
		abbreviationMap.put("char", "C");
		abbreviationMap.put("int", "I");
		abbreviationMap.put("long", "J");
		abbreviationMap.put("float", "F");
		abbreviationMap.put("double", "D");
	}
	
	// get
	// ----------------------------------------------------------------------------------
	
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable e) {
		}
		if (cl == null) {
			cl = ClassLoaderUtils.class.getClassLoader();
		}
		return cl;
	}
	
	// classpath
	// ----------------------------------------------------------------------------------
	
	public static Collection<URL> getClasspathURLs() {
		return getClasspathURLs(getDefaultClassLoader());
	}

	public static Collection<URL> getClasspathURLs(ClassLoader classLoader) {
		Collection<URL> urls = new HashSet<URL>(32);
		ClassLoader loader = classLoader;
		while (loader != null) {
			if (EXT_CLASS_LOADER_NAME.equals(loader.getClass().getName())) {
				break;
			}
			if (loader instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader) loader).getURLs()) {
					urls.add(url);
				}
			}
			loader = loader.getParent();
		}
		return urls;
	}
	
	public static Collection<URL> getClassPathURLs(String packageName) {
		return getClasspathURLs(getDefaultClassLoader(), packageName);
	}

	public static Collection<URL> getClasspathURLs(ClassLoader classLoader, String packageName) {
		if (StringUtils.isBlank(packageName)) {
			throw new IllegalArgumentException("PackageName must be not blank");
		}
		Collection<URL> urls = new ArrayList<URL>();
		String dirname = packageName.replace('.', '/');
		try {
			Enumeration<URL> dirs = classLoader.getResources(dirname);
			while (dirs.hasMoreElements()) {
				urls.add(dirs.nextElement());
			}
		} catch (IOException e) {
			ExceptionUtils.rethrow(e);
		}
		return urls;
	}
	
	// load class
	// ----------------------------------------------------------------------------------
	
	public static Class<?> loadClass(String qualifiedClassName) {
		return loadClass(getDefaultClassLoader(), qualifiedClassName);
	}
	
	public static Class<?> loadClass(ClassLoader classLoader, String qualifiedClassName){
		if (qualifiedClassName == null) {
			throw new NullPointerException("qualifiedClassName must not be null.");
		}

		ClassLoader loader = (classLoader == null) ? getDefaultClassLoader() : classLoader;

		// 尝试基本类型
		if (abbreviationMap.containsKey(qualifiedClassName)) {
			String klassName = '[' + abbreviationMap.get(qualifiedClassName);
			try {
				return Class.forName(klassName, false, loader).getComponentType();
			} catch (ClassNotFoundException e) {
				throw ExceptionUtils.rethrow(e);
			}
		}

		// 尝试用 Class.forName()
		try {
			String klassName = getCanonicalClassName(qualifiedClassName);
			return Class.forName(klassName, false, loader);
		} catch (ClassNotFoundException e) {
		}

		// 尝试当做一个内部类去识别
		if (qualifiedClassName.indexOf('$') == -1) {
			int ipos = qualifiedClassName.lastIndexOf('.');
			if (ipos > 0) {
				try {
					String klassName = qualifiedClassName.substring(0, ipos) + '$' + qualifiedClassName.substring(ipos + 1);
					klassName = getCanonicalClassName(klassName);
					return Class.forName(klassName, false, loader);
				} catch (ClassNotFoundException e) {
				}
			}
		}

		throw ExceptionUtils.rethrow(new ClassNotFoundException("Class not found: " + qualifiedClassName));
	}
	
	private static String getCanonicalClassName(String qualifiedClassName) {
		if (qualifiedClassName == null) {
			throw new NullPointerException("qualifiedClassName must not be null.");
		}

		String name = StringUtils.removeWhitespace(qualifiedClassName);
		if (name.endsWith("[]")) {
			StringBuilder sb = new StringBuilder();

			while (name.endsWith("[]")) {
				name = name.substring(0, name.length() - 2);
				sb.append('[');
			}

			String abbreviation = abbreviationMap.get(name);
			if (abbreviation != null) {
				sb.append(abbreviation);
			} else {
				sb.append('L').append(name).append(';');
			}

			name = sb.toString();
		}
		return name;
	}

}
