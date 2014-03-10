package kelly.core.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebContextHolder {

	private static final WebContextHolder INSTANCE = new WebContextHolder();
	private static final ThreadLocal<HttpServletRequest>  REQUEST_HOLDER  = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new ThreadLocal<HttpServletResponse>();
	
	public static WebContextHolder getInstance() {
		return INSTANCE;
	}

	private WebContextHolder() {
		super();
	}
	
	// -------------------------------------------------------------------------------------------------
	
	void setRequest(HttpServletRequest request) {
		REQUEST_HOLDER.set(request);
	}
	
	void setResponse(HttpServletResponse response) {
		RESPONSE_HOLDER.set(response);
	}
	
	public HttpServletRequest getRequest() {
		return REQUEST_HOLDER.get();
	}
	
	public HttpServletResponse getResponse() {
		return RESPONSE_HOLDER.get();
	}

}
