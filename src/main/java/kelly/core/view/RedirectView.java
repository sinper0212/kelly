package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;
import kelly.util.Validate;

public class RedirectView extends AbstractView {

	private final String location;
	
	public RedirectView(String location) {
		this.location = Validate.notNull(location);
	}
	
	@Override
	public String getContentType() {
		return null;
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		response.sendRedirect(location);
	}

}
