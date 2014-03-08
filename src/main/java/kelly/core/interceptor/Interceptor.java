package kelly.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
 */
public interface Interceptor {

	boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Throwable;

	void postHandle(HttpServletRequest request, HttpServletResponse response) throws Throwable;

}
