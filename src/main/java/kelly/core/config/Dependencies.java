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
package kelly.core.config;

/**
 * 其他开源软件依赖检查工具(内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
class Dependencies {

	private static final String SPRING_CLASS_NAME = "org.springframework.context.ApplicationContext";
	private static final String FASTJSON_CLASS_NAME = "com.alibaba.fastjson.JSON";
	private static final String JETBRICK_TEMPLATE_CLASS_NAME = "jetbrick.template.JetEngine";
	private static final String COMMONS_FILEUPLOAD = "org.apache.commons.fileupload.servlet.ServletFileUpload";

	// ------------------------------------------------------------------------------------------------
	
	public static boolean checkFastjson() {
		try {
			getClassLoader().loadClass(FASTJSON_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static boolean checkJetx() {
		try {
			getClassLoader().loadClass(JETBRICK_TEMPLATE_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static boolean checkSpring() {
		try {
			getClassLoader().loadClass(SPRING_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static boolean checkCommonsFileupload() {
		try {
			getClassLoader().loadClass(COMMONS_FILEUPLOAD);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

}
