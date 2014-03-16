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
import java.util.Map;
import java.util.Set;

import kelly.core.Model;

/**
 * 不可变的Model (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public final class ImmutableModel implements Model {

	public static final Model INSTANCE = new ImmutableModel();
	
	private ImmutableModel() {
		super();
	}
	
	@Override
	public Map<String, Object> asMap() {
		return Collections.emptyMap();			// immutable map
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Model put(String name, Object value) {
		throw new UnsupportedOperationException("Cannot put key-value pair to a immutable model.");
	}

	@Override
	public Object get(String name) {
		return null;
	}

	@Override
	public Object get(String name, Object defaultIfNull) {
		return defaultIfNull;
	}

	@Override
	public Set<String> getKeys() {
		return Collections.emptySet();
	}

}
