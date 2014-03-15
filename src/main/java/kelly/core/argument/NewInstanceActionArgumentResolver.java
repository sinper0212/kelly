package kelly.core.argument;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.KellyException;
import kelly.util.ReflectionUtils;

public class NewInstanceActionArgumentResolver extends AbstractActionArgumentResolver {

	private static final ThreadLocal<Object> NEW_INSTANCE_HOLDER = new ThreadLocal<Object>();
	
	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		if (getSource(actionArgument) != null) {
			return false;
		}
		try {
			Object object = actionArgument.getParameterType().newInstance();
			NEW_INSTANCE_HOLDER.set(object);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) throws KellyException {
		Object target = NEW_INSTANCE_HOLDER.get();
		try {
			return target;
		} finally {
			init(target, castor);
		}
	}

	private void init(Object target, Castor castor) {
		HttpServletRequest request = WebContextHolder.getInstance().getRequest();
		Class<?> cls = target.getClass();
		
		Set<String> methodNameSet = new HashSet<String>();
		List<Method> methodList = new ArrayList<Method>();
		
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if ( 
				Modifier.isPublic(method.getModifiers()) &&
				Modifier.isStatic(method.getModifiers()) == false &&
				method.getParameterTypes().length == 1 &&
				methodName.length() >= 4 &&
				methodName.startsWith("set") && 
				method.getReturnType() == void.class
				)
			{
				if (methodNameSet.contains(methodName) == false) {
					methodNameSet.add(methodName);
					methodList.add(method);
				}
			}
		}
		
		for (Method setter : methodList) {
			String att = setter.getName().substring(3).toLowerCase();
			String source = null;
			Enumeration<String> paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				if (att.equalsIgnoreCase(paramName)) {
					source = request.getParameter(paramName);
				}
			}
			
			if (source == null) {
				continue;
			}
			
			Class<?> targetType = setter.getParameterTypes()[0];
			Object value = null;
			if (castor.canConvert(targetType)) {
				value = castor.convert(source, targetType);
			}

			if (value != null) {
				try {
					ReflectionUtils.invokeMethod(setter, target, value);
				} catch (Throwable e) {
					// swallow this exception
				}
			}
		}
	}

}
