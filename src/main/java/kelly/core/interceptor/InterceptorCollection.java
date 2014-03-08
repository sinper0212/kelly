package kelly.core.interceptor;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.injector.Injector;
import kelly.core.injector.NOPInjector;
import kelly.util.ReflectionUtils;

/**
 * 拦截器搜集器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class InterceptorCollection implements Addable<Class<? extends Interceptor>>, Aware<Injector> {

	private Injector injector = new NOPInjector();
	private final Map<Class<? extends Interceptor>, Interceptor> cache = new IdentityHashMap<Class<? extends Interceptor>, Interceptor>();
	
	// -------------------------------------------------------------------------------------------

	public InterceptorCollection() {
		super();
	}
	
	// -------------------------------------------------------------------------------------------

	@Override
	public void add(Class<? extends Interceptor> interceptorClass) {
		if (cache.containsKey(interceptorClass) == false) {
			Interceptor interceptor = ReflectionUtils.invokeConstructor(interceptorClass);
			injector.inject(interceptor);
			cache.put(interceptorClass, interceptor);
		}
	}
	
	public void add(Interceptor interceptor) {
		if (cache.containsKey(interceptor.getClass()) == false) {
			injector.inject(interceptor);
			cache.put(interceptor.getClass(), interceptor);
		}
	}
	
	public Set<Class<? extends Interceptor>> getInterceptorTypes() {
		return cache.keySet();		// immutable set
	}

	@Override
	public void setComponent(Injector injector) {
		if (injector != null) {
			this.injector = injector;
		}
	}

}
