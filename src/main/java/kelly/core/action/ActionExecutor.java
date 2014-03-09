package kelly.core.action;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.ActionResult;
import kelly.core.Model;
import kelly.core.ModelAndView;
import kelly.core.result.ByteArrayActionResult;
import kelly.core.result.FileActionResult;
import kelly.core.result.InputStreamActionResult;
import kelly.core.result.ModelActionResult;
import kelly.core.result.ModelAndViewActionResult;
import kelly.core.result.NullActionResult;
import kelly.core.result.ObjectActionResult;
import kelly.util.ReflectionUtils;


/**
 * Action执行器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionExecutor {

	public ActionResult execute(InvokableAction invokableAction, Object[] args, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		Object controllerObject = invokableAction.getControllerObject();
		Method method = invokableAction.getMethod();
		
		Object result = ReflectionUtils.invokeMethod(method, controllerObject, args);
		
		if (result == null) {
			return new NullActionResult(invokableAction, request, response);
		}

		if (result instanceof ActionResult) {
			return (ActionResult) result;
		}
		
		if (result instanceof InputStream) {
			return new InputStreamActionResult((InputStream) result, invokableAction, request, response);
		}
		
		if (result instanceof File) {
			return new FileActionResult((File) result, invokableAction, request, response);
		}
		
		if (result.getClass() == byte[].class) {
			return new ByteArrayActionResult((byte[]) result, invokableAction, request, response);
		}
		
		if (result instanceof ModelAndView) {
			return new ModelAndViewActionResult((ModelAndView) result, invokableAction, request, response);
		}
		
		if (result instanceof Model) {
			return new ModelActionResult((Model) result, invokableAction, request, response);
		}
		
		return new ObjectActionResult(result, invokableAction, request, response);
	}

}
