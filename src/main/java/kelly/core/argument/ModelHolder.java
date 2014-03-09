package kelly.core.argument;

import kelly.core.Model;

/**
 * Model传递工具 (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ModelHolder {

	private static final ModelHolder INSTANCE = new ModelHolder();
	private static final ThreadLocal<Model> INNER_HOLDER = new ThreadLocal<Model>();


	// -------------------------------------------------------------------------------
	
	public static ModelHolder getInstance() {
		return INSTANCE;
	}

	// --------------------------------------------------------------------------------
	
	private ModelHolder() {
		super();
	}
	
	// --------------------------------------------------------------------------------

	// public
	void setModel(Model model) {
		INNER_HOLDER.set(model);
	}
	
	public Model getModel() {
		return INNER_HOLDER.get();
	}

}
