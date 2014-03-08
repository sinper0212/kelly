package kelly.core.action;

import java.lang.reflect.Method;

/**
 * 可执行的Action
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class InvokableAction extends Action {

	private final Object controllerObject;
	
	// ----------------------------------------------------------------------------------------
	
	public InvokableAction(Object controllerObject, Action action) {
		this(controllerObject, action.getControllerClass(), action.getMethod());
	}

	public InvokableAction(Object controllerObject, Class<?> controllerClass, Method method) {
		super(controllerClass, method);
		if (controllerObject == null) {
			throw new NullPointerException();
		}
		this.controllerObject = controllerObject;
	}

	// ----------------------------------------------------------------------------------------

	public Object getControllerObject() {
		return controllerObject;
	}

}
