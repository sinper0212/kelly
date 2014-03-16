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
package kelly.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kelly.core.Model;

/**
 * 模型类 (对map的简单封装)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class MapModel implements Model {

	private final Map<String, Object> map = new HashMap<String, Object>();
	
	public MapModel() {
		super();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public Model put(String name, Object value) {
		map.put(name, value);
		return this;
	}
	
	public Object get(String name) {
		return map.get(name);
	}
	
	public Object get(String name, Object defaultIfNull) {
		return get(name) == null ? defaultIfNull : get(name);
	}
	
	public Set<String> getKeys() {
		return map.keySet();			// immutable set
	}

	public Map<String, Object> asMap() {
		return Collections.unmodifiableMap(map);	// immutable
	}

}
