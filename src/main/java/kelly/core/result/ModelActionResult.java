package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;
import kelly.core.model.MapModel;

public final class ModelActionResult extends AbstractActionResult {

	public ModelActionResult(MapModel model, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(model, action, request, response);
	}

	@Override
	public MapModel getModel() {
		return getActualResult(MapModel.class);
	}

}
