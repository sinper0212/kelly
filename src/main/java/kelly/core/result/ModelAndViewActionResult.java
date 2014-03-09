package kelly.core.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.ModelAndView;
import kelly.core.action.Action;

public final class ModelAndViewActionResult extends AbstractActionResult {

	public ModelAndViewActionResult(ModelAndView mav, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(mav, action, request, response);
	}
	
	@Override
	public String getView() {
		return (getActualResult(ModelAndView.class).getViewName() != null) ? 
					getActualResult(ModelAndView.class).getViewName() : 
					super.getView();
	}

	@Override
	public Model getModel() {
		return getActualResult(ModelAndView.class).getModel();
	}

}
