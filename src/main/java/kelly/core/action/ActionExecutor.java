package kelly.core.action;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Aware;
import kelly.core.Model;
import kelly.core.ModelAndView;
import kelly.core.annotation.Interceptors;
import kelly.core.interceptor.Interceptor;
import kelly.core.interceptor.InterceptorCollection;
import kelly.core.resource.ByteArrayResource;
import kelly.core.resource.FileSystemResource;
import kelly.core.resource.Resource;
import kelly.core.result.ActionResult;
import kelly.core.result.InputStreamActionResult;
import kelly.core.result.ModelActionResult;
import kelly.core.result.ModelAndViewActionResult;
import kelly.core.result.NullActionResult;
import kelly.core.result.ObjectActionResult;
import kelly.core.result.ResourceActionResult;
import kelly.util.ReflectionUtils;


/**
 * Action执行器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionExecutor implements Aware<InterceptorCollection> {
	
	private final Interceptor[] EMPTY_INTERCEPTOR_ARRAY = new Interceptor[0];
	
	private InterceptorCollection interceptorCollection;

	public ActionResult execute(InvokableAction invokableAction, Object[] args, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		Object controllerObject = invokableAction.getControllerObject();
		Method method = invokableAction.getMethod();
		
		// 得到拦截器实例
		Interceptor[] interceptorArray = EMPTY_INTERCEPTOR_ARRAY;
		if (invokableAction.getMethod().getAnnotation(Interceptors.class) != null) {
			interceptorArray = interceptorCollection.getInterceptors(invokableAction.getMethod().getAnnotation(Interceptors.class).value(), true);
		}
		
		int index = 0;
		for (int i = 0; i < interceptorArray.length; i ++) {
			boolean abort = interceptorArray[i].preHandle(request, response);
			index ++;
			if (abort) {
				return (ActionResult) null;
			} 
		}
		
		Object result = ReflectionUtils.invokeMethod(method, controllerObject, args);
		
		while (index-- != 0) {
			interceptorArray[index].postHandle(request, response);
		}
		
		if (result == null) {
			return new NullActionResult(invokableAction, request, response);
		}

		if (result instanceof ActionResult) {
			return (ActionResult) result;
		}
		
		if (result instanceof Resource) {
			return new ResourceActionResult((Resource) result, invokableAction, request, response);
		}
		
		if (result instanceof File) {
			return new ResourceActionResult(new FileSystemResource((File) result), invokableAction, request, response);
		}
		
		if (result.getClass() == byte[].class) {
			return new ResourceActionResult(new ByteArrayResource((byte[]) result), invokableAction, request, response);
		}
		
		if (result.getClass() == Byte[].class) {
			Byte[] src = (Byte[]) result;
			byte buf[] = new byte[src.length];
			for (int i = 0; i < src.length; i++) {
				buf[i] = src[i].byteValue();
			}
			return new ResourceActionResult(new ByteArrayResource(buf), invokableAction, request, response);
		}
		
		if (result instanceof InputStream) {
			return new InputStreamActionResult((InputStream) result, invokableAction, request, response);
		}
		
		if (result instanceof ModelAndView) {
			return new ModelAndViewActionResult((ModelAndView) result, invokableAction, request, response);
		}
		
		if (result instanceof Model) {
			return new ModelActionResult((Model) result, invokableAction, request, response);
		}
		
		return new ObjectActionResult(result, invokableAction, request, response);
	}
	
	// ----------------------------------------------------------------------------------------------------------

	@Override
	public void setComponent(InterceptorCollection interceptorCollection) {
		if (interceptorCollection != null) {
			this.interceptorCollection = interceptorCollection;
		}
	}

}
