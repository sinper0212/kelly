package kelly.core.config;

import java.util.SortedSet;

import kelly.core.action.ActionArgumentResolverCollection;
import kelly.core.action.ActionCollection;
import kelly.core.action.ActionExecutor;
import kelly.core.action.ActionFinder;
import kelly.core.action.InvokableActionFactory;
import kelly.core.castor.ConversionService;
import kelly.core.functor.Predicate;
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

	JsonFactory getJsonFactory();

	Injector getInjector();

	String[] packagesToScan();
	
	// 注册回调方法
	// ---------------------------------------------------------------

	void registerConverters(ConversionService conversionService);
	
	void registerInterceptors(InterceptorCollection interceptorCollection);

	void registerActionArgumentResolvers(ActionArgumentResolverCollection collection);

	void registerViewResolvers(SortedSet<ViewResolver> viewResolverSet);

}
