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

import java.util.SortedSet;

import kelly.core.Predicate;
import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.action.ActionCollection;
import kelly.core.action.ActionExecutor;
import kelly.core.action.ActionFinder;
import kelly.core.action.InvokableActionFactory;
import kelly.core.castor.ConversionService;
import kelly.core.exception.ExceptionResolver;
import kelly.core.injector.Injector;
import kelly.core.interceptor.InterceptorCollection;
import kelly.core.json.JsonFactory;
import kelly.core.view.ViewResolver;

/**
 * Kelly配置
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface Config {

	// 系统核心组件
	// ---------------------------------------------------------------

	ActionCollection getActionCollection();

	ActionFinder getActionFinder();

	InvokableActionFactory getInvokableActionFactory();

	InterceptorCollection getInterceptorCollection();

	ActionExecutor getActionExecutor();

	ConversionService getConversionService();
	
	ActionArgumentResolverCollection getActionArgumentResolverCollection();

	Predicate<String> getStaticResourcePredicate();
	
	SortedSet<ViewResolver> getViewResolverSet();
	
	ExceptionResolver getExceptionResolver();

	JsonFactory getJsonFactory();

	Injector getInjector();

	String[] packagesToScan();

	
	// 注册回调方法
	// ---------------------------------------------------------------

	void registerConverters(ConversionService conversionService);
	
	void registerInterceptors(InterceptorCollection interceptorCollection);

	void registerActionArgumentResolvers(ActionArgumentResolverCollection collection);

	void registerViewResolvers(SortedSet<ViewResolver> viewResolverSet);
	
	void configExceptionResolver(ExceptionResolver exceptionResolver);

}
