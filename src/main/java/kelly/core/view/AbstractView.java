package kelly.core.view;

import java.util.Locale;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.annotation.ContentType;
import kelly.core.dispatcher.DispatcherFilter;
import kelly.core.result.ActionResult;
import kelly.util.Validate;

public abstract class AbstractView implements View {

	@Override
	public final void render(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		setContentTypeIfNecessary(actionResult.getAction(), response);
		doRender(actionResult, request, response, locale);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * @see DispatcherFilter#setContentTypeIfNecessary
	 */
	private void setContentTypeIfNecessary(Action action, HttpServletResponse response) {
		// 如果方法上有@ContentType的话，视图不再设置ContentType
		// 由DispatcherFilter设置
		ContentType contentType = action.getMethod().getAnnotation(ContentType.class);
		if (getContentType() != null && contentType == null) {
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
