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
package kelly.core.resource;

import java.io.IOException;

import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

/**
 * ResourceLoader的加强版本，默认实现为SmartResourceLoader
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see SmartResourceLoader
 * @see PathMatcher
 * @see AntStylePathMatcher
 */
public interface ResourcePatternResolver extends ResourceLoader {

	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	
	ResourcePatternResolver DEFAULT = new SmartResourceLoader();

	Resource[] getResources(String locationPattern) throws IOException;

}
