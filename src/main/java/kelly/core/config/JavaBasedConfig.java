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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import kelly.core.Predicate;
import kelly.core.action.Action;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.action.ActionCollection;
import kelly.core.action.ActionExecutor;
import kelly.core.action.ActionFinder;
import kelly.core.action.InvokableActionFactory;
import kelly.core.annotation.Controller;
import kelly.core.annotation.Mapping;
import kelly.core.argument.CastorActionArgumentResolver;
import kelly.core.argument.CommonsFileUploadActionArgumentResolver;
import kelly.core.argument.ModelActionArgumentResolver;
import kelly.core.argument.NewInstanceActionArgumentResolver;
import kelly.core.argument.PrintWriterActionArgumentResolver;
import kelly.core.argument.RequestActionArgumentResolver;
import kelly.core.argument.ResponseActionArgumentResolver;
import kelly.core.argument.ServletContextActionArgumentResolver;
import kelly.core.argument.ServletOutputStreamActionArgumentResolver;
import kelly.core.argument.SessionActionArgumentResolver;
import kelly.core.argument.SessionIdActionArgumentResolver;
import kelly.core.castor.BooleanConverter;
import kelly.core.castor.ClassConverter;
import kelly.core.castor.ConversionService;
import kelly.core.castor.Converter;
import kelly.core.castor.DateConverter;
import kelly.core.castor.DoubleConverter;
import kelly.core.castor.FloatConverter;
import kelly.core.castor.IntConverter;
import kelly.core.castor.NumberConverter;
import kelly.core.castor.StringConverter;
import kelly.core.exception.ExceptionResolver;
import kelly.core.injector.Injector;
import kelly.core.injector.NOPInjector;
import kelly.core.interceptor.Interceptor;
import kelly.core.interceptor.InterceptorCollection;
import kelly.core.json.FastjsonJsonFactory;
import kelly.core.json.JsonFactory;
import kelly.core.json.NOPJsonFactory;
import kelly.core.view.CommittedViewResolver;
import kelly.core.view.DownloadViewResolver;
import kelly.core.view.JetxViewResolver;
import kelly.core.view.JsonViewResolver;
import kelly.core.view.JspViewResolver;
import kelly.core.view.RedirectViewResolver;
import kelly.core.view.ViewResolver;
import kelly.util.ClassUtils;
import kelly.util.scan.ClassLookupUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked" })
public class JavaBasedConfig extends AbstractJavaBasedConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaBasedConfig.class);
	
	// -----------------------------------------------------------------------------------------------

	public JavaBasedConfig() {

		// 扫描注册用户扩展组件
		Set<Class<?>> classes = ClassLookupUtils.lookupClasses(packagesToScan(), true, KELLY_SCAN_ANNOTATIONS);
		generateActions(classes);
		generateInterceptors(classes);
		generateConverters(classes);
		generateActionArgumentResolvers(classes);

		log();
	}

	private void generateActions(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			Controller controller = cls.getAnnotation(Controller.class);
			if (controller == null) continue;
			
			Method[] methods = cls.getDeclaredMethods();
			if (methods == null) continue;
			
			for (Method method : methods) {
				int mod = method.getModifiers();
				if (Modifier.isPublic(mod) == false) continue;			// 排除非public方法
				if (Modifier.isStatic(mod) == true)  continue;			// 排除static方法
				if (method.getAnnotation(Mapping.class) == null) continue; // 排除没有@Mapping的方法
				Action action = new Action(cls, method);
				actionCollection.add(action);
				LOGGER.trace("扫描到Action: pattern({})", action.getPattern());
			}
		}
	}
	
	private void generateInterceptors(Set<Class<?>> classes) {
		// 被标记了@Component的Interceptor
		// 没有标记@Component的Interceptor Lazy Loading.
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			boolean isImpl = ClassUtils.isAssignable(cls, Interceptor.class, true);
			if (isImpl) {
				interceptorCollection.add((Class<? extends Interceptor>) cls);
				LOGGER.trace("扫描到Inteceptor: type({})", cls.getName());
			}
		}
	}
	
	private void generateConverters(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			boolean isImpl = ClassUtils.isAssignable(cls, Converter.class, true);
			if (isImpl) {
				conversionService.add((Class<? extends Converter<?>>) cls);
				LOGGER.trace("扫描到Converter: type({})", cls.getName());
			}
		}
	}
	
	private void generateActionArgumentResolvers(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			boolean isImpl = ClassUtils.isAssignable(cls, ActionArgumentResolver.class, true);
			if (isImpl) {
				actionArgumentResolverCollection.add((Class<? extends ActionArgumentResolver>) cls);
				LOGGER.trace("扫描到ActionArgumentResolver: type({})", cls.getName());
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------

	@Override
	public ActionCollection getActionCollection() {
		return actionCollection;
	}

	@Override
	public ActionFinder getActionFinder() {
		return actionFinder;
	}

	@Override
	public InvokableActionFactory getInvokableActionFactory() {
		return invokableActionFactory;
	}

	@Override
	public InterceptorCollection getInterceptorCollection() {
		return interceptorCollection;
	}

	@Override
	public ActionExecutor getActionExecutor() {
		return actionExecutor;
	}

	@Override
	public ConversionService getConversionService() {
		return conversionService;
	}

	@Override
	public ActionArgumentResolverCollection getActionArgumentResolverCollection() {
		return actionArgumentResolverCollection;
	}

	@Override
	public Predicate<String> getStaticResourcePredicate() {
		return staticResourcePredicate;
	}
	
	@Override
	public SortedSet<ViewResolver> getViewResolverSet() {
		return Collections.unmodifiableSortedSet(viewResolvers);
	}
	
	@Override
	public ExceptionResolver getExceptionResolver() {
		return exceptionResolver;
	}

	// 用户覆盖项
	// ------------------------------------------------------------------------------------------
	
	@Override
	public Injector getInjector() {
		return NOPInjector.INSTANCE;
	}
	
	@Override
	public JsonFactory getJsonFactory() {
		return Dependencies.checkFastjson() ? new FastjsonJsonFactory() : NOPJsonFactory.INSTANCE;
	}

	@Override
	public String[] packagesToScan() {
		return null;
	}

	@Override
	public void registerConverters(ConversionService conversionService) {
		LOGGER.trace("注册系统默认Converter");
		conversionService.add(new StringConverter());
		conversionService.add(new DateConverter());
		conversionService.add(new BooleanConverter());
		conversionService.add(new IntConverter());
		conversionService.add(new FloatConverter());
		conversionService.add(new DoubleConverter());
		conversionService.add(new NumberConverter());
		conversionService.add(new ClassConverter());
	}

	@Override
	public void registerInterceptors(InterceptorCollection interceptorCollection) {
		LOGGER.trace("注册系统默认Intceptor");
	}

	@Override
	public void registerActionArgumentResolvers(ActionArgumentResolverCollection collection) {
		LOGGER.trace("注册系统默认ActionArgumentResolver");
		collection.add(new RequestActionArgumentResolver());
		collection.add(new ResponseActionArgumentResolver());
		collection.add(new SessionActionArgumentResolver());
		collection.add(new SessionIdActionArgumentResolver());
		collection.add(new ServletContextActionArgumentResolver());
		collection.add(new ModelActionArgumentResolver());
		collection.add(new PrintWriterActionArgumentResolver());
		collection.add(new ServletOutputStreamActionArgumentResolver());
		collection.add(new NewInstanceActionArgumentResolver());
		collection.add(new CastorActionArgumentResolver());

		if (Dependencies.checkCommonsFileupload()) {
			collection.add(new CommonsFileUploadActionArgumentResolver());
		}
	}

	@Override
	public void registerViewResolvers(SortedSet<ViewResolver> viewResolverSet) {
		LOGGER.trace("注册系统默认ViewResolver");
		viewResolverSet.add(new CommittedViewResolver());
		viewResolverSet.add(new RedirectViewResolver());
		viewResolverSet.add(new DownloadViewResolver());
		viewResolverSet.add(new JsonViewResolver(getJsonFactory()));
		viewResolverSet.add(new JspViewResolver());

		if (Dependencies.checkJetx()) {
			viewResolverSet.add(new JetxViewResolver());
		}
	}

	@Override
	public void configExceptionResolver(ExceptionResolver exceptionResolver) {
		LOGGER.trace("配置系统默认ExceptionResolver");
	}

}
