package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;

public final class NullActionResult extends AbstractActionResult {

	private static final Object NULL = new Object();
	
	public NullActionResult(Action action, HttpServletRequest request, HttpServletResponse response) {
		super(NULL, action, request, response);
	}

	@Override
	public Class<?> getObjectClass() {
		return null;
	}

	@Override
	public Object getObject() {
		return null;
	}

}
