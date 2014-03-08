package kelly.core;

import java.io.Serializable;

public final class ModelAndView implements Serializable {

	private static final long serialVersionUID = 1092588180796752524L;

	private final Model model = new Model();
	private String viewName;
	
	public ModelAndView() {
		super();
	}
	
	public ModelAndView(String viewName) {
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
