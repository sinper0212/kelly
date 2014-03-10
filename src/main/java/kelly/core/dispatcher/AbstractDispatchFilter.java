package kelly.core.dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象DispatchFilter
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class AbstractDispatchFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AbstractDispatchFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// NOP
	}

	@Override
	public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest   request  = (HttpServletRequest) req;
		HttpServletResponse  response = (HttpServletResponse) res;

		// 初始化WebContextHolder
		logger.trace("init WebContextHolder's instance");
		WebContextHolder.getInstance().setRequest(request);
		WebContextHolder.getInstance().setResponse(response);
		
		doFilter(request, response, chain);
	}
	
	protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException; 

	@Override
	public void destroy() {
		// NOP
	}
	
	// ------------------------------------------------------------------------------------------
	

}
