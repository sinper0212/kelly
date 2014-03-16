/*
 * Copyright 2002-2012 Zhuo Ying. All rights reserved.
 * Email: yingzhor@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@Deprecated
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
