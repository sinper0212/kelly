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
package kelly.core.config;

import java.lang.annotation.Annotation;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import kelly.core.Ordered;
import kelly.core.Predicate;
import kelly.core.action.Action;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.action.ActionCollection;
import kelly.core.action.ActionExecutor;
import kelly.core.action.ActionFinder;
import kelly.core.action.InvokableActionFactory;
import kelly.core.annotation.Component;
import kelly.core.annotation.Controller;
import kelly.core.castor.ConversionService;
import kelly.core.castor.Converter;
import kelly.core.castor.PropertyEditorFindingConversionService;
import kelly.core.exception.ExceptionResolver;
import kelly.core.interceptor.Interceptor;
import kelly.core.interceptor.InterceptorCollection;
import kelly.core.path.DefaultStaticResourcePredicate;
import kelly.core.view.ViewResolver;
import kelly.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置项 (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
abstract class AbstractJavaBasedConfig implements Config {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJavaBasedConfig.class);
	
	@SuppressWarnings("unchecked")
	public static final Class<? extends Annotation>[] KELLY_SCAN_ANNOTATIONS = 
					new Class[] {Component.class, Controller.class};

	// ------------------------------------------------------------------------------------------------

	protected final ActionCollection actionCollection = new ActionCollection();
	
	protected final ActionFinder actionFinder = new ActionFinder();
	
	protected final InvokableActionFactory invokableActionFactory = actionCollection;
	
	protected final InterceptorCollection interceptorCollection = new InterceptorCollection();
	
	protected final ActionExecutor actionExecutor = new ActionExecutor();
	
	protected final ConversionService conversionService = new PropertyEditorFindingConversionService();
	
	protected final ActionArgumentResolverCollection actionArgumentResolverCollection = new ActionArgumentResolverCollection();
	
	protected final Predicate<String> staticResourcePredicate = new DefaultStaticResourcePredicate();
	
	protected final SortedSet<ViewResolver> viewResolvers = new TreeSet<ViewResolver>(Ordered.DEFAULT_COMPARATOR);
	
	protected final ExceptionResolver exceptionResolver = new ExceptionResolver();

	// ------------------------------------------------------------------------------------------------

	public AbstractJavaBasedConfig() {
		actionFinder.setComponent(actionCollection);
		actionCollection.setComponent(getInjector());
		interceptorCollection.setComponent(getInjector());
		actionArgumentResolverCollection.setComponent(conversionService);
		actionExecutor.setComponent(interceptorCollection);
		exceptionResolver.setComponent(viewResolvers);

		// 调用注册钩
		registerConverters(conversionService);
		registerInterceptors(interceptorCollection);
		registerActionArgumentResolvers(actionArgumentResolverCollection);
		registerViewResolvers(viewResolvers);
		configExceptionResolver(exceptionResolver);

	}
	
	// ------------------------------------------------------------------------------------------------
	
	
	public final void log() {
		if (LOGGER.isDebugEnabled() == false) return;
		
		// action
		LOGGER.debug(StringUtils.repeat('=', 120));
		LOGGER.debug("Action(s) : ");
		for (Action action : actionCollection.getSortedActionList()) {
			LOGGER.debug(action.toString());
		}
		
		// interceptor
		LOGGER.debug(StringUtils.repeat('-', 120));
		LOGGER.debug("Interceptor(s) : ");
		for (Class<? extends Interceptor> interceptorClass : interceptorCollection.getInterceptorTypes()) {
			LOGGER.debug(interceptorClass.getName());
		}
		
		// converter
		LOGGER.debug(StringUtils.repeat('-', 120));
		LOGGER.debug("Converter(s) : ");
		for (Entry<Class<?>, Converter<?>> entry : conversionService.getConverterPairs()) {
			LOGGER.debug("{}  java.lang.String -> {}",entry.getValue().getClass().getName(), entry.getKey().getName());
		}
		
		// actionArgumentResolver
		LOGGER.debug(StringUtils.repeat('-', 120));
		LOGGER.debug("ActionArgumentResolver(s) : ");
		for (ActionArgumentResolver resolver : actionArgumentResolverCollection.getActionArgumentResolvers()) {
			LOGGER.debug("{}", resolver.getClass().getName());
		}

		// viewResolver
		LOGGER.debug(StringUtils.repeat('-', 120));
		LOGGER.debug("ViewResolver(s) : ");
		for (ViewResolver resolver : viewResolvers) {
			LOGGER.debug("{}", resolver.getClass().getName());
		}

		// 
		LOGGER.debug(StringUtils.repeat('-', 120));
		LOGGER.debug("dependency check (spring)             : {}", Dependencies.checkSpring() ? "OK" : "NG");
		LOGGER.debug("dependency check (fastjson)           : {}", Dependencies.checkFastjson() ? "OK" : "NG");
		LOGGER.debug("dependency check (jetbrick-template)  : {}", Dependencies.checkJetx() ? "OK" : "NG");
		LOGGER.debug("dependency check (commons-fileupload) : {}", Dependencies.checkCommonsFileupload() ? "OK" : "NG");

		// end
		LOGGER.debug(StringUtils.repeat('=', 120));
	}

}
