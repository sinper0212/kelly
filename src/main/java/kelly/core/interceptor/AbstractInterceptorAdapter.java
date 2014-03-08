package kelly.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器抽象适配器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class AbstractInterceptorAdapter implements Interceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
	}

}
