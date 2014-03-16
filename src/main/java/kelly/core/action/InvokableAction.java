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

import java.lang.reflect.Method;

/**
 * 可执行的Action
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class InvokableAction extends Action {

	private final Object controllerObject;
	
	// ----------------------------------------------------------------------------------------
	
	public InvokableAction(Object controllerObject, Action action) {
		this(controllerObject, action.getControllerClass(), action.getMethod());
	}

	public InvokableAction(Object controllerObject, Class<?> controllerClass, Method method) {
		super(controllerClass, method);
		if (controllerObject == null) {
			throw new NullPointerException();
		}
		this.controllerObject = controllerObject;
	}

	// ----------------------------------------------------------------------------------------

	public Object getControllerObject() {
		return controllerObject;
	}

}
