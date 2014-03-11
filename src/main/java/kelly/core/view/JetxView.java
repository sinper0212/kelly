package kelly.core.view;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetbrick.template.JetTemplate;
import kelly.core.result.ActionResult;
import kelly.util.Validate;

public class JetxView extends AbstractView {

	private final JetTemplate jetTemplate;
	
	public JetxView(JetTemplate jetTemplate) {
		this.jetTemplate = Validate.notNull(jetTemplate);
	}
	
	@Override
	public String getContentType() {
		return "text/html; charset=utf-8";
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		PrintWriter writer = response.getWriter();
		jetTemplate.render(actionResult.getModel().asMap(), writer);
	}

}
