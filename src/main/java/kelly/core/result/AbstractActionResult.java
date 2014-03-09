package kelly.core.result;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.ActionResult;
import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.argument.ModelHolder;
import kelly.util.StringUtils;
import kelly.util.Validate;

public abstract class AbstractActionResult implements ActionResult {

	protected final Object result;
	protected final Action action;
	protected final HttpServletRequest request;
	protected final HttpServletResponse response;

	public AbstractActionResult(Object result, Action action, HttpServletRequest request, HttpServletResponse response) {
		this.result = Validate.notNull(result);
		this.request = Validate.notNull(request);
		this.response = Validate.notNull(response);
		this.action = Validate.notNull(action);
	}

	@Override
	public Class<?> getObjectClass() {
		return result != null ? result.getClass() : null;
	}

	@Override
	public Object getObject() {
		return result;
	}

	@Override
	public final Action getAction() {
		return action;
	}

	@Override
	public Model getModel() {
		Model model = ModelHolder.getInstance().getModel();
		return model != null ? model : Model.IMMUTABLE_EMPTY_MODEL;
	}

	@Override
	public String getView() {
		return request.getRequestURI();
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public Type getType() {
		if (isCommitted()) {
			return Type.COMMITTED;
		} else {
			return startsWithIgnoreCase(getView(), "redirect:") ? Type.REDIRECT : Type.FORWARD;
		}
	}

	// -------------------------------------------------------------------------------

	protected final boolean isCommitted() {
		return response.isCommitted();
	}

	@SuppressWarnings("unchecked")
	protected final <T> T getActualResult(Class<T> resultClass) {
		return (T) result;
	}

	// -------------------------------------------------------------------------------

	private boolean startsWithIgnoreCase(String text, String prefix) {
		if (text == null)
			return false;
		return StringUtils.removeWhitespace(text).toLowerCase().startsWith(prefix);
	}

}
