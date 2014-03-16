package kelly.core.config;

import java.lang.annotation.Annotation;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import kelly.core.Ordered;
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
import kelly.core.functor.Predicate;
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

	public static final Logger log = LoggerFactory.getLogger(AbstractJavaBasedConfig.class);
	
	@SuppressWarnings("unchecked")
	public static final Class<? extends Annotation>[] KELLY_SCAN_ANNOTATIONS = 
			new Class[] {Component.class, Controller.class};

	// ------------------------------------------------------------------------------------------------

	public final ActionCollection actionCollection = new ActionCollection();
	
	public final ActionFinder actionFinder = new ActionFinder();
	
	public final InvokableActionFactory invokableActionFactory = actionCollection;
	
	public final InterceptorCollection interceptorCollection = new InterceptorCollection();
	
	public final ActionExecutor actionExecutor = new ActionExecutor();
	
	public final ConversionService conversionService = new PropertyEditorFindingConversionService();
	
	public final ActionArgumentResolverCollection actionArgumentResolverCollection = new ActionArgumentResolverCollection();
	
	public final Predicate<String> staticResourcePredicate = new DefaultStaticResourcePredicate();
	
	public final SortedSet<ViewResolver> viewResolvers = new TreeSet<ViewResolver>(Ordered.DEFAULT_COMPARATOR);

	// ------------------------------------------------------------------------------------------------

	public AbstractJavaBasedConfig() {
		actionFinder.setComponent(actionCollection);
		actionCollection.setComponent(getInjector());
		interceptorCollection.setComponent(getInjector());
		actionArgumentResolverCollection.setComponent(conversionService);
		actionExecutor.setComponent(interceptorCollection);

		// 调用注册钩
		registerConverters(conversionService);
		registerInterceptors(interceptorCollection);
		registerActionArgumentResolvers(actionArgumentResolverCollection);
		registerViewResolvers(viewResolvers);
	}
	
	// ------------------------------------------------------------------------------------------------
	
	
	public final void log() {
		if (log.isDebugEnabled() == false) return;
		
		// action
		log.debug(StringUtils.repeat('=', 120));
		log.debug("Action(s) : ");
		for (Action action : actionCollection.getSortedActionList()) {
			log.debug(action.toString());
		}
		
		// interceptor
		log.debug(StringUtils.repeat('-', 120));
		log.debug("Interceptor(s) : ");
		for (Class<? extends Interceptor> interceptorClass : interceptorCollection.getInterceptorTypes()) {
			log.debug(interceptorClass.getName());
		}
		
		// converter
		log.debug(StringUtils.repeat('-', 120));
		log.debug("Converter(s) : ");
		for (Entry<Class<?>, Converter<?>> entry : conversionService.getConverterPairs()) {
			log.debug("{}  java.lang.String -> {}",entry.getValue().getClass().getName(), entry.getKey().getName());
		}
		
		// actionArgumentResolver
		log.debug(StringUtils.repeat('-', 120));
		log.debug("ActionArgumentResolver(s) : ");
		for (ActionArgumentResolver resolver : actionArgumentResolverCollection.getActionArgumentResolvers()) {
			log.debug("{}", resolver.getClass().getName());
		}

		// viewResolver
		log.debug(StringUtils.repeat('-', 120));
		log.debug("ViewResolver(s) : ");
		for (ViewResolver resolver : viewResolvers) {
			log.debug("{}", resolver.getClass().getName());
		}

		// 
		log.debug(StringUtils.repeat('-', 120));
		log.debug("dependency check (spring)            : {}", Dependencies.checkSpring() ? "OK" : "NG");
		log.debug("dependency check (fastjson)          : {}", Dependencies.checkFastjson() ? "OK" : "NG");
		log.debug("dependency check (jetbrick-template) : {}", Dependencies.checkJetx() ? "OK" : "NG");
		
		// end
		log.debug(StringUtils.repeat('=', 120));
	}

}
