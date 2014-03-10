package kelly.core.view;

import java.util.Locale;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.result.ActionResult;
import kelly.util.Validate;

public abstract class AbstractView implements View {

	@Override
	public final void render(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		setContentType(response);
		doRender(actionResult, request, response, locale);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	
	private void setContentType(HttpServletResponse response) {
		if (getContentType() != null) {
			response.setContentType(getContentType());
		}
	}
	
	protected final void copyModelToRequest(Model model, HttpServletRequest request) {
		Validate.notNull(model);
		Validate.notNull(request);
		for (Entry<String, Object> entry : model.asMap().entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------

	// 用户覆盖此方法
	protected abstract void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable;

}
