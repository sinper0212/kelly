package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;

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
	
	// ----------------------------------------------------------------------------------------------------------------

	// 用户覆盖此方法
	protected abstract void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable;

}
