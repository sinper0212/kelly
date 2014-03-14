package kelly.core.config;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import kelly.core.action.Action;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.action.ActionCollection;
import kelly.core.action.ActionExecutor;
import kelly.core.action.ActionFinder;
import kelly.core.action.InvokableActionFactory;
import kelly.core.annotation.Controller;
import kelly.core.argument.BooleanResolver;
import kelly.core.argument.CastorForwardingResolver;
import kelly.core.argument.CreateableActionArgument;
import kelly.core.argument.DateResolver;
import kelly.core.argument.HttpServletRequestResolver;
import kelly.core.argument.HttpServletResponseResolver;
import kelly.core.argument.HttpSessionResolver;
import kelly.core.argument.ModelResolver;
import kelly.core.argument.PrintWriterResolver;
import kelly.core.argument.PropertyEditorForwardingResolver;
import kelly.core.argument.ServletContextResolver;
import kelly.core.argument.ServletOutputStreamResolver;
import kelly.core.argument.SessionIdResolver;
import kelly.core.castor.BooleanConverter;
import kelly.core.castor.ClassConverter;
import kelly.core.castor.ConversionService;
import kelly.core.castor.Converter;
import kelly.core.castor.DoubleConverter;
import kelly.core.castor.FloatConverter;
import kelly.core.castor.IntConverter;
import kelly.core.castor.StringConverter;
import kelly.core.functor.Predicate;
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

@SuppressWarnings({ "unchecked" })
public class JavaBasedConfig extends AbstractJavaBasedConfig {

	// -----------------------------------------------------------------------------------------------

	public JavaBasedConfig() {
		super();

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
	
	// ------------------------------------------------------------------------------------------
	
	public ActionCollection getActionCollection() {
		return actionCollection;
	}

	public ActionFinder getActionFinder() {
		return actionFinder;
	}

	public InvokableActionFactory getInvokableActionFactory() {
		return invokableActionFactory;
	}

	public InterceptorCollection getInterceptorCollection() {
		return interceptorCollection;
	}

	public ActionExecutor getActionExecutor() {
		return actionExecutor;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public ActionArgumentResolverCollection getActionArgumentResolverCollection() {
		return actionArgumentResolverCollection;
	}

	public Predicate<String> getStaticResourcePredicate() {
		return staticResourcePredicate;
	}
	
	public SortedSet<ViewResolver> getViewResolverSet() {
		return Collections.unmodifiableSortedSet(viewResolvers);
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
		log.debug("register converters");
		conversionService.add(new StringConverter());
		conversionService.add(new BooleanConverter());
		conversionService.add(new IntConverter());
		conversionService.add(new FloatConverter());
		conversionService.add(new DoubleConverter());
		conversionService.add(new ClassConverter());
	}

	@Override
	public void registerInterceptors(InterceptorCollection interceptorCollection) {
		log.debug("register interceptors");
	}

	@Override
	public void registerActionArgumentResolvers(ActionArgumentResolverCollection collection) {
		log.debug("register action-argument resolvers");
		collection.add(new ModelResolver());
		collection.add(new SessionIdResolver());
		collection.add(new HttpServletRequestResolver());
		collection.add(new HttpServletResponseResolver());
		collection.add(new HttpSessionResolver());
		collection.add(new ServletContextResolver());
		collection.add(new PrintWriterResolver());
		collection.add(new ServletOutputStreamResolver());
		collection.add(new DateResolver());
		collection.add(new BooleanResolver());
		collection.add(new PropertyEditorForwardingResolver());
		collection.add(new CastorForwardingResolver());
		collection.add(new CreateableActionArgument());
	}

	@Override
	public void registerViewResolvers(SortedSet<ViewResolver> viewResolverSet) {
		log.debug("register view resolvers");
		viewResolverSet.add(new CommittedViewResolver());
		viewResolverSet.add(new RedirectViewResolver());
		viewResolverSet.add(new DownloadViewResolver());
		viewResolverSet.add(new JsonViewResolver(getJsonFactory()));
		viewResolverSet.add(new JspViewResolver());

		// 如果ClassPath中有jetbrick-template.xxx.jar
		// 则加载JetxViewResolver的实例
		if (Dependencies.checkJetx()) {
			viewResolverSet.add(new JetxViewResolver());
		}
	}

}
