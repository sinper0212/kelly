package kelly.core.injector;

import kelly.core.Plugin;
import kelly.core.interceptor.Interceptor;

/**
 * 注入器 <br>
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see Interceptor
 */
public interface Injector extends Plugin {

	void inject(Object bean);

}
