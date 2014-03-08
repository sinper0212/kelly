package junit.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.annotation.Component;
import kelly.core.interceptor.Interceptor;

@Component
public class YInterceptor implements Interceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
