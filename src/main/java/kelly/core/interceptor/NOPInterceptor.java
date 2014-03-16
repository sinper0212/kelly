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
package kelly.core.interceptor;

/**
 * 不做任何动作的拦截器，为避免空指针异常 (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class NOPInterceptor extends AbstractInterceptorAdapter {

	public static final Interceptor INSTANCE = new NOPInterceptor();
	
	private NOPInterceptor() {
		super();
	}

}
