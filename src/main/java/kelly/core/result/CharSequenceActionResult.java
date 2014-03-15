package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;

public final class CharSequenceActionResult extends AbstractActionResult {

	public CharSequenceActionResult(CharSequence cs, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(cs, action, request, response);
	}

	@Override
	public String getView() {
		return getActualResult(CharSequence.class).toString();
	}

}
