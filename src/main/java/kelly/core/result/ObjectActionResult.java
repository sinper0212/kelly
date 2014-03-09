package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;

public class ObjectActionResult extends AbstractActionResult {

	public ObjectActionResult(Object result, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(result, action, request, response);
	}

}
