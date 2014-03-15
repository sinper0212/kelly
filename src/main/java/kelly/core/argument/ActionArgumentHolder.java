package kelly.core.argument;

import kelly.core.action.ActionArgument;

public final class ActionArgumentHolder {

	private static final ThreadLocal<ActionArgument> HOLDER = new ThreadLocal<ActionArgument>();
	private static final ActionArgumentHolder INSTANCE = new ActionArgumentHolder();
	
	public static ActionArgumentHolder getInstance() {
		return INSTANCE;
	}
	
	// ------------------------------------------------------------------------------

	private ActionArgumentHolder() {
	}
	
	// ------------------------------------------------------------------------------
	
	void setActionArgument(ActionArgument actionArgument) {
		HOLDER.set(actionArgument);
	}
	
	public ActionArgument getActionArgument() {
		return HOLDER.get();
	}
}
