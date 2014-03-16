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
package kelly.core.argument;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.util.ReflectionUtils;

public class NewInstanceActionArgumentResolver extends AbstractActionArgumentResolver {

	private static final ThreadLocal<Object> NEW_INSTANCE_HOLDER = new ThreadLocal<Object>();
	
	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		if (getSource(actionArgument) != null) {
			return false;
		}
		try {
			Object object = actionArgument.getParameterType().newInstance();
			NEW_INSTANCE_HOLDER.set(object);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		Object target = NEW_INSTANCE_HOLDER.get();
		try {
			return target;
		} finally {
			init(target, castor);
		}
	}

	private void init(Object target, Castor castor) {
		HttpServletRequest request = WebContextHolder.getInstance().getRequest();
		Class<?> cls = target.getClass();
		
		Set<String> methodNameSet = new HashSet<String>();
		List<Method> methodList = new ArrayList<Method>();
		
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if ( 
				Modifier.isPublic(method.getModifiers()) &&
				Modifier.isStatic(method.getModifiers()) == false &&
				method.getParameterTypes().length == 1 &&
				methodName.length() >= 4 &&
				methodName.startsWith("set") && 
				method.getReturnType() == void.class
				)
			{
				if (methodNameSet.contains(methodName) == false) {
					methodNameSet.add(methodName);
					methodList.add(method);
				}
			}
		}
		
		for (Method setter : methodList) {
			String att = setter.getName().substring(3).toLowerCase();
			String source = null;
			Enumeration<String> paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				if (att.equalsIgnoreCase(paramName)) {
					source = request.getParameter(paramName);
				}
			}
			
			if (source == null) {
				continue;
			}
			
			Class<?> targetType = setter.getParameterTypes()[0];
			Object value = null;
			if (castor.canConvert(targetType)) {
				value = castor.convert(source, targetType);
			}

			if (value != null) {
				try {
					ReflectionUtils.invokeMethod(setter, target, value);
				} catch (Throwable e) {
					// swallow this exception
				}
			}
		}
	}

}
