package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;
import kelly.util.Validate;

public class JspView extends AbstractView {

	private final String path;
	
	public JspView(String path) {
		this.path = Validate.notNull(path);
	}
	
	@Override
	public String getContentType() {
		return "text/html; charset=utf-8";
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		copyModelToRequest(actionResult.getModel(), request);
		request.getRequestDispatcher(path).forward(request, response);
	}

}
