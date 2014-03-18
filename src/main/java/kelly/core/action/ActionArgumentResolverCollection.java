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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.castor.ConversionService;
import kelly.core.exception.ArgumentResolverException;
import kelly.util.ReflectionUtils;
import kelly.util.Validate;

/**
 * ActionArgumentResolver收集器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionArgumentResolverCollection 
			implements Addable<Class<? extends ActionArgumentResolver>>, Aware<ConversionService>

{
	private final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
	// ---------------------------------------------------------------------------------
	

	private final List<ActionArgumentResolver> actionArgumentResolverList = new ArrayList<ActionArgumentResolver>();
	private ConversionService conversionService;
	
	// ---------------------------------------------------------------------------------

	public ActionArgumentResolverCollection() {
		super();
	}
	
	// ---------------------------------------------------------------------------------
	
	public boolean supports(ActionArgument actionArgument, HttpServletRequest request) {
		return findActionArgumentResolver(actionArgument) != null;
	}
	
	public Object[] resolve(ActionArgument[] actionArguments, HttpServletRequest request, HttpServletResponse response) {
		if (actionArguments.length == 0) {
			return EMPTY_OBJECT_ARRAY;
		}
		
		List<Object> params = new ArrayList<Object>();
		for (ActionArgument actionArgument : actionArguments) {
			ActionArgumentResolver resolver = findActionArgumentResolver(actionArgument);
			if (resolver == null) {
				if (actionArgument.isNullable()) {
					Class<?> cls = actionArgument.getParameterType();
					params.add(getDefault(cls));
					continue;
				} else {
					throw new ArgumentResolverException("Cannot find resolver for action-argument : " + actionArgument);
				}
			}
			Object object = resolver.resolve(actionArgument, conversionService);
			params.add(object);
		}
		return params.toArray();
	}

	private Object getDefault(Class<?> cls) {
		RuntimeException ex = new ArgumentResolverException("Cannot resolver set null to a primitive variable");
		if (cls == boolean.class) throw ex;
		if (cls == byte.class) throw ex;
		if (cls == short.class) throw ex;
		if (cls == char.class) throw ex;
		if (cls == int.class) throw ex;
		if (cls == long.class) throw ex;
		if (cls == float.class)  throw ex;
		if (cls == double.class)  throw ex;
		return null;
	}

	@Override
	public void add(Class<? extends ActionArgumentResolver> resolverClass) {
		if (resolverClass != null) {
			ActionArgumentResolver resolver = ReflectionUtils.invokeConstructor(resolverClass);
			add(resolver);
		}
	}

	public void add(ActionArgumentResolver resolver) {
		if (resolver != null) {
			actionArgumentResolverList.add(resolver);
		}
	}
	
	public List<ActionArgumentResolver> getActionArgumentResolvers() {
		return Collections.unmodifiableList(actionArgumentResolverList);		// immutable list
	}

	// ---------------------------------------------------------------------------------

	private ActionArgumentResolver findActionArgumentResolver(ActionArgument actionArgument) {
		if (actionArgument == null) {
			return null;
		}
		for (ActionArgumentResolver resolver : actionArgumentResolverList) {
			if (resolver.supports(actionArgument, conversionService)) {
				return resolver;
			}
		}
		return null;
	}

	@Override
	public void setComponent(ConversionService conversionService) {
		this.conversionService = Validate.notNull(conversionService);
	}

}
