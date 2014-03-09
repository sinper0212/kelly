package kelly.core;

import java.io.Serializable;

import kelly.core.argument.ModelHolder;

public final class ModelAndView implements Serializable {

	private final Model model;
	private String viewName;
	
	public ModelAndView() {
		Model cached = ModelHolder.getInstance().getModel();
		if (cached == null) {
			model = new Model();
		} else {
			model = cached;
		}
	}
	
	public ModelAndView(String viewName) {
		this();
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Model getModel() {
		return model;
	}

}
