package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;

public final class ModelActionResult extends AbstractActionResult {

	public ModelActionResult(Model model, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(model, action, request, response);
	}

	@Override
	public Model getModel() {
		return getActualResult(Model.class);
	}

}
