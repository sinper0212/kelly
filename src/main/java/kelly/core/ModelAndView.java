package kelly.core;

import java.io.Serializable;

import kelly.core.argument.ModelHolder;
import kelly.core.model.MapModel;

/**
 * 模型与视图
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see MapModel
 * @see ModelHolder
 */
public final class ModelAndView implements Serializable {

	private final Model model;
	private String viewName;

	public ModelAndView() {
		Model cachedModel = ModelHolder.getInstance().getModel();
		
		if (cachedModel == null) {
			model = new MapModel();
		} else {
			model = cachedModel;
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
