package kelly.core.dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.servlet.HttpMethodRequestWrapper;

/**
 * 抽象DispatchFilter
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class AbstractDispatchFilter implements Filter {

	@Override
	public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest   request  = (HttpServletRequest) req;
		HttpServletResponse  response = (HttpServletResponse) res;

		// 初始化WebContextHolder
		WebContextHolder.getInstance().setRequest(request);
		WebContextHolder.getInstance().setResponse(response);
		
		// 支持其他类型的请求
		String method = request.getParameter("_method");
		if (method != null) {
			request = new HttpMethodRequestWrapper(request, method);
		}
		
		doFilter(request, response, chain);
	}
	
	protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException; 

	@Override
	public void destroy() {
		// NOP
	}

}
