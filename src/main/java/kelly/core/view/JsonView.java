package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.json.JsonFactory;
import kelly.core.result.ActionResult;

public class JsonView extends AbstractView {

	private final JsonFactory jsonFactory;
	
	public JsonView(JsonFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}
	
	@Override
	public String getContentType() {
		return "application/json; charset=utf-8";
	}

	@Override
	public void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		Object target = actionResult.getObject() != null ? actionResult.getObject() : actionResult.getModel().asMap();
		String json = jsonFactory.create(target);
		response.getWriter().print(json);
	}

}
