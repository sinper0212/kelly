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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.injector.Injector;
import kelly.core.injector.NOPInjector;
import kelly.util.ReflectionUtils;

/**
 * 拦截器搜集器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class InterceptorCollection implements Addable<Class<? extends Interceptor>>, Aware<Injector> {

	private Injector injector = NOPInjector.INSTANCE;
	private final Map<Class<? extends Interceptor>, Interceptor> cache = new IdentityHashMap<Class<? extends Interceptor>, Interceptor>();
	
	// -------------------------------------------------------------------------------------------

	public InterceptorCollection() {
		super();
	}
	
	// -------------------------------------------------------------------------------------------

	@Override
	public void add(Class<? extends Interceptor> interceptorClass) {
		if (cache.containsKey(interceptorClass) == false) {
			Interceptor interceptor = ReflectionUtils.invokeConstructor(interceptorClass);
			injector.inject(interceptor);
			cache.put(interceptorClass, interceptor);
		}
	}
	
	public void add(Interceptor interceptor) {
		if (cache.containsKey(interceptor.getClass()) == false) {
			injector.inject(interceptor);
			cache.put(interceptor.getClass(), interceptor);
		}
	}
	
	public Set<Class<? extends Interceptor>> getInterceptorTypes() {
		return cache.keySet();		// immutable set
	}
	
	public Interceptor[] getInterceptors(Class<? extends Interceptor>[] interceptorClasses, boolean createIfNotFound) {
		List<Interceptor> list = new ArrayList<Interceptor>(interceptorClasses.length);
		for (Class<? extends Interceptor> cls : interceptorClasses) {
			Interceptor instance = cache.get(cls);
			if (instance != null) {
				list.add(instance);
			} else {
				if (createIfNotFound == false) {
					list.add(NOPInterceptor.INSTANCE);
				} else {
					add(cls);
					list.add(cache.get(cls));
				}
			}
		}
		return list.toArray(new Interceptor[interceptorClasses.length]);
	}

	@Override
	public void setComponent(Injector injector) {
		if (injector != null) {
			this.injector = injector;
		}
	}

}
