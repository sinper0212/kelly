package kelly.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

	private final String method;

	public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
		super(request);
		this.method = method;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

}
