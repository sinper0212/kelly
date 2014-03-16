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
package kelly.core.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.annotation.Singleton;
import kelly.core.injector.Injector;
import kelly.core.injector.NOPInjector;
import kelly.util.ReflectionUtils;


/**
 * Action收集器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionCollection implements Addable<Action>, InvokableActionFactory, Aware<Injector> {

	private Injector injector = NOPInjector.INSTANCE;
	private final List<Action> actionList = new ArrayList<Action>();
	private final Map<Class<?>, Object> singtonControllerCache = new IdentityHashMap<Class<?>, Object>();

	public ActionCollection() {
		super();
	}

	@Override
	public void add(Action action) {
		if (action == null) {
			throw new NullPointerException();
		}
		
		Singleton singelton = action.getControllerClass().getAnnotation(Singleton.class);
		if (singelton != null) {
			Object controllerObject = ReflectionUtils.invokeConstructor(action.getControllerClass());
			injector.inject(controllerObject);
		}
		
		actionList.add(action);
		handleSingletonController(action);
	}
	
	private void handleSingletonController(Action action) {
		if (action.isSingletonController()) {
			Object controllerObject = ReflectionUtils.invokeConstructor(action.getControllerClass());
			injector.inject(controllerObject);
			singtonControllerCache.put(action.getControllerClass(), controllerObject);
		}
	}

	@Override
	public InvokableAction create(Action action) {
		Class<?> controllerClass = action.getControllerClass();
		Object cached = singtonControllerCache.get(controllerClass);
		if (cached == null) {
			cached = ReflectionUtils.invokeConstructor(controllerClass);
			injector.inject(cached);
		}
		return new InvokableAction(cached, action);
	}

	@Override
	public void setComponent(Injector injector) {
		if (injector != null) {
			this.injector = injector;
		}
	}

	public List<Action> getActionList() {
		return Collections.unmodifiableList(actionList);			// immutable list
	}
	
	// 为了输出日志好看
	public List<Action> getSortedActionList() {
		List<Action> list = new ArrayList<Action>(getActionList());
		Collections.sort(list);
		return Collections.unmodifiableList(list);
	}

}
