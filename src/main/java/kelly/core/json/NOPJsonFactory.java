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
package kelly.core.json;

/**
 * 无法生成Json字符串的生成类
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public class NOPJsonFactory implements JsonFactory {

	public static final JsonFactory INSTANCE = new NOPJsonFactory();

	private NOPJsonFactory() {
		super();
	}
	
	@Override
	public String create(Object object) throws Throwable {
		return "";
	}

}
