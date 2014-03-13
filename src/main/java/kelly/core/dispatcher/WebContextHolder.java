package kelly.core.dispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.config.Config;

/**
 * Kelly环境的上下文 (线程安全)
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 *
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @see ServletContext
 * @see Config
 * @see ThreadLocal
 * 
 */
public final class WebContextHolder {

	private static final WebContextHolder INSTANCE = new WebContextHolder();
	private static final ThreadLocal<HttpServletRequest>  REQUEST_HOLDER  = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new ThreadLocal<HttpServletResponse>();
	private static ServletContext SERVLET_CONTEXT = null;
	private static Config CONFIG = null;
	
	public static WebContextHolder getInstance() {
		return INSTANCE;
	}

	private WebContextHolder() {
		super();
	}
	
	// -------------------------------------------------------------------------------------------------
	
	// 包内访问
	void setRequest(HttpServletRequest request) {
		REQUEST_HOLDER.set(request);
	}
	
	// 包内访问
	void setResponse(HttpServletResponse response) {
		RESPONSE_HOLDER.set(response);
	}
	
	// 包内访问
	void setServletContext(ServletContext servletContext) {
		SERVLET_CONTEXT = servletContext;
	}
	
	void setConfig(Config config) {
		CONFIG = config;
	}
	
	public HttpServletRequest getRequest() {
		return REQUEST_HOLDER.get();
	}
	
	public HttpServletResponse getResponse() {
		return RESPONSE_HOLDER.get();
	}
	
	public ServletContext getServletContext() {
		return SERVLET_CONTEXT;
	}
	
	public Config getConfig() {
		return CONFIG;
	}

}
