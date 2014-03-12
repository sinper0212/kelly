package kelly.core.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import kelly.core.RequestMethod;
import kelly.core.annotation.Controller;
import kelly.core.annotation.Delete;
import kelly.core.annotation.Get;
import kelly.core.annotation.Head;
import kelly.core.annotation.Interceptors;
import kelly.core.annotation.Mapping;
import kelly.core.annotation.Options;
import kelly.core.annotation.Patch;
import kelly.core.annotation.Post;
import kelly.core.annotation.Put;
import kelly.core.annotation.Singleton;
import kelly.core.annotation.Trace;
import kelly.core.interceptor.Interceptor;
import kelly.util.StringUtils;


/**
 * 表示每一个HTTP请求处理方法
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class Action implements Comparable<Action>{
	
	private static final ActionArgument[] EMPTY_ACTION_ARGUMENT_ARRAY = new ActionArgument[0];
	
	private final Class<?> controllerClass;
	private final Method method;
	private final String clearControllerKey;
	private final String clearActionKey;
	private final Set<RequestMethod> requestMethodSet = new LinkedHashSet<RequestMethod>();
	private final List<Class<? extends Interceptor>> interceptorClassList = new ArrayList<Class<? extends Interceptor>>();

	public Action(Class<?> controllerClass, Method method) {
		if (controllerClass == null || method == null) {
			throw new NullPointerException("controllerClass or method is null");
		}
		this.controllerClass = controllerClass;
		this.method = method;
		this.clearControllerKey = cleanKey(controllerClass.getAnnotation(Controller.class).value());
		this.clearActionKey = cleanKey(method.getAnnotation(Mapping.class).value());

		initRequestMethodSet();
		initInterceptorClassList();
	}

	private void initRequestMethodSet() {
		if (method.getAnnotation(Get.class) != null) {
			requestMethodSet.add(RequestMethod.GET);
		}
		if (method.getAnnotation(Head.class) != null) {
			requestMethodSet.add(RequestMethod.HEAD);
		}
		if (method.getAnnotation(Post.class) != null) {
			requestMethodSet.add(RequestMethod.POST);
		}
		if (method.getAnnotation(Put.class) != null) {
			requestMethodSet.add(RequestMethod.PUT);
		}
		if (method.getAnnotation(Patch.class) != null) {
			requestMethodSet.add(RequestMethod.PATCH);
		}
		if (method.getAnnotation(Delete.class) != null) {
			requestMethodSet.add(RequestMethod.DELETE);
		}
		if (method.getAnnotation(Options.class) != null) {
			requestMethodSet.add(RequestMethod.OPTIONS);
		}
		if (method.getAnnotation(Trace.class) != null) {
			requestMethodSet.add(RequestMethod.TRACE);
		}
		
		// 如果以上8个标注都没有，就认为所有method类型都支持
		if (requestMethodSet.isEmpty()) {
			requestMethodSet.addAll(Arrays.asList(RequestMethod.values()));
		}

	}
	
	private void initInterceptorClassList() {
		Interceptors annotation = method.getAnnotation(Interceptors.class);
		if (annotation != null) {
			interceptorClassList.addAll(Arrays.asList(annotation.value()));
		}
	}

	// ----------------------------------------------------------------

	public final Class<?> getControllerClass() {
		return this.controllerClass;
	}

	public final Method getMethod() {
		return this.method;
	}
	
	public final Class<?>[] getMethodParameterTypes() {
		return getMethod().getParameterTypes();
	}

	public final String getControllerKey() {
		return this.clearControllerKey;
	}
	
	public final String getActionKey() {
		return this.clearActionKey;
	}

	public final Set<RequestMethod> getRequestMethods() {
		return Collections.unmodifiableSet(this.requestMethodSet);
	}
	
	public final String getPattern() {
		return getControllerKey() + getActionKey();
	}
	
	public final boolean isSingletonController() {
		return getControllerClass().getAnnotation(Singleton.class) != null;
	}
	
	public final ActionArgument[] getActionArguments() {
		Class<?>[] parameterTypes = getMethodParameterTypes();
		if (parameterTypes == null || parameterTypes.length == 0) {
			return EMPTY_ACTION_ARGUMENT_ARRAY;
		}
		List<ActionArgument> result = new ArrayList<ActionArgument>(parameterTypes.length);
		for (int i = 0; i < parameterTypes.length; i ++) {
			result.add(new ActionArgument(i, parameterTypes[i], method.getParameterAnnotations()[i], this));
		}
		return result.toArray(new ActionArgument[result.size()]);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\'');
		sb.append(getPattern());
		sb.append('\'');
		sb.append(" -> ");
		sb.append(getControllerClass().getName() + ".");
		sb.append(getMethod().getName());
		sb.append("(");
		for (int i = 0; i < getMethodParameterTypes().length; i++) {
			sb.append(getMethodParameterTypes()[i].getSimpleName());
			if (i != getMethodParameterTypes().length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		if (isSingletonController()) {
			sb.append(" <singleton>");
		}
		return sb.toString();
	}
	
	@Override
	public final int compareTo(Action o) {
		return this.getPattern().compareTo(o.getPattern());
	}

	// ----------------------------------------------------------------
	
	private String cleanKey(String key) {
		key = StringUtils.removeWhitespace(key);
		if (key.equals("/") || key.equals("")) {
			return "";
		}
		if (key.startsWith("/") == false) {
			return "/" + key;
		}
		return key;
	}

}
