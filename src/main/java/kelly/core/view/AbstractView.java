package kelly.core.view;

import java.io.Closeable;
import java.util.Locale;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.annotation.ContentType;
import kelly.core.dispatcher.DispatcherFilter;
import kelly.core.result.ActionResult;
import kelly.util.IOUtils;
import kelly.util.Validate;

public abstract class AbstractView implements View {

	@Override
	public final void render(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		setContentTypeIfNecessary(actionResult.getAction(), response);
		doRender(actionResult, request, response, locale);
		close(actionResult);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	
	protected final void copyModelToRequest(Model model, HttpServletRequest request) {
		Validate.notNull(model);
		Validate.notNull(request);
		for (Entry<String, Object> entry : model.asMap().entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
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
	
	private final void close(ActionResult actionResult) {
		IOUtils.closeQuietly((Closeable) actionResult.getInputStream());
		
		Model model = actionResult.getModel();
		if (model != null && !model.isEmpty()) {
			for (Entry<String, Object> entry : actionResult.getModel().asMap().entrySet()) {
				if (entry.getValue() instanceof Closeable) {
					Closeable closeable = (Closeable) entry.getValue();
					IOUtils.closeQuietly(closeable);
				}
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------

	// 用户覆盖此方法
	protected abstract void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable;

}
