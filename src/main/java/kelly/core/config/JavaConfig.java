package kelly.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import kelly.core.action.Action;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.annotation.Component;
import kelly.core.annotation.Controller;
import kelly.core.argument.BooleanResolver;
import kelly.core.argument.DateResolver;
import kelly.core.argument.HttpServletRequestResolver;
import kelly.core.argument.HttpServletResponseResolver;
import kelly.core.argument.HttpSessionResolver;
import kelly.core.argument.PrintWriterResolver;
import kelly.core.argument.ServletContextResolver;
import kelly.core.argument.ServletOutputStreamResolver;
import kelly.core.castor.ConversionService;
import kelly.core.castor.Converter;
import kelly.core.injector.Injector;
import kelly.core.injector.NOPInjector;
import kelly.core.interceptor.Interceptor;
import kelly.core.interceptor.InterceptorCollection;
import kelly.util.ClassUtils;
import kelly.util.scan.ClassLookupUtils;

@SuppressWarnings({ "unchecked" })
public class JavaConfig extends Config {
	
	protected static final Class<? extends Annotation>[] KELLY_SCAN_ANNOTATIONS = 
							new Class[] {Component.class, Controller.class};
	
	// -----------------------------------------------------------------------------------------------

	public JavaConfig() {

		// 注册系统默认组件
		registerConverters(conversionService);
		registerActionArgumentResolvers(actionArgumentResolverCollection);
		registerInterceptors(interceptorCollection);

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
				if (! Modifier.isPublic(mod)) continue;
				Action action = new Action(cls, method);
				actionCollection.add(action);
				log.trace("Action found: {}", action.getPattern());
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
				log.trace("Inteceptor found: {}", cls.getName());
			}
		}
	}
	
	private void generateConverters(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			boolean isImpl = ClassUtils.isAssignable(cls, Converter.class, true);
			if (isImpl) {
				conversionService.add((Class<? extends Converter<?>>) cls);
				log.trace("Converter found: {}", cls.getName());
			}
		}
	}
	
	private void generateActionArgumentResolvers(Set<Class<?>> classes) {
		for (Class<?> cls : classes) {
			if (cls == null) continue;
			boolean isImpl = ClassUtils.isAssignable(cls, ActionArgumentResolver.class, true);
			if (isImpl) {
				actionArgumentResolverCollection.add((Class<? extends ActionArgumentResolver>) cls);
				log.trace("ActionArgumentResolver found: {}", cls.getName());
			}
		}
	}

	// 用户覆盖项
	// ------------------------------------------------------------------------------------------
	
	@Override
	protected Injector getInjector() {
		return new NOPInjector();
	}

	@Override
	protected String[] packagesToScan() {
		return null;
	}

	@Override
	protected void registerConverters(ConversionService conversionService) {
	}

	@Override
	protected void registerInterceptors(InterceptorCollection interceptorCollection) {
	}

	@Override
	protected void registerActionArgumentResolvers(ActionArgumentResolverCollection collection) {
		collection.add(HttpServletRequestResolver.class);
		collection.add(HttpServletResponseResolver.class);
		collection.add(HttpSessionResolver.class);
		collection.add(ServletContextResolver.class);
		collection.add(PrintWriterResolver.class);
		collection.add(ServletOutputStreamResolver.class);
		collection.add(DateResolver.class);
		collection.add(BooleanResolver.class);
	}

}
