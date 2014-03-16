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
package kelly.util.scan;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 类查找工具
 * 
 * @author 陈国强(subchen@gmail.com)
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ClassLookupUtils {

	private ClassLookupUtils() {
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------------------------
	
	public static Set<Class<?>> lookupClasses(String packageName, Class<? extends Annotation>... annotationTypes) {
		if (packageName == null) {
			return lookupClasses(annotationTypes);
		}
		else {
			return lookupClasses(new String[] {packageName}, annotationTypes);
		}
	}
	
	public static Set<Class<?>> lookupClasses(String[] packageNames, Class<? extends Annotation>... annotationTypes) {
		return lookupClasses(packageNames, true, annotationTypes);
	}

	public static Set<Class<?>> lookupClasses(Class<? extends Annotation>... annotationTypes) {
		return lookupClasses(null, true, annotationTypes, true);
	}

	public static Set<Class<?>> lookupClasses(String[] packageNames, boolean recursive, Class<? extends Annotation>[] annotationTypes) {
		return lookupClasses(packageNames, recursive, annotationTypes, true);
	}

	public static Set<Class<?>> lookupClasses(String[] packageNames, boolean recursive, Class<? extends Annotation>[] annotationTypes, final boolean skipErrors) {
		if (annotationTypes == null || annotationTypes.length == 0) {
			return Collections.emptySet();
		}
		
		final ClassFileReader reader = new ClassFileReader();
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			reader.addAnnotation(annotationType);
		}
		
		final Set<Class<?>> set = new LinkedHashSet<Class<?>>();
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		final FileFinder finder = new FileFinder() {
			public void visitFileEntry(FileEntry file) {
				if (file.isJavaClass()) {
					try {
						if (reader.isAnnotationed(file.getInputStream())) {
							String qcn = file.getQualifiedJavaName().trim();
							Class<?> klass = cl.loadClass(qcn);
							if (klass != null) {
								set.add(klass);
							}
						}
					} catch (Exception e) {
						if (skipErrors == false) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		};

		if (packageNames == null || packageNames.length == 0) {
			finder.lookupClasspath();
		} else {
			finder.lookupClasspath(packageNames, recursive);
		}
		return Collections.unmodifiableSet(set);
	}

}
