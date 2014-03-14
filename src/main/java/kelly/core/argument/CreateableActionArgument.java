package kelly.core.argument;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ReflectionUtils;

public class CreateableActionArgument implements ActionArgumentResolver {
	
	private static final ThreadLocal<Object> OBJECT_HOLDER = new ThreadLocal<Object>();
	
	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		try {
			Object obj = actionArgument.getParameterType().newInstance();
			OBJECT_HOLDER.set(obj);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		Object object = OBJECT_HOLDER.get();
		copyBySetter(object, request, castor);
		return object;
	}
	
	private void copyBySetter(Object target, HttpServletRequest request, Castor castor) {
		Class<?> cls = target.getClass();
		
		// 找到所有叶子setter
		Set<String> addedMethodNames = new HashSet<String>();
		Collection<Method> setters = new ArrayList<Method>();
		while (cls != null) {
			Method[] methods = cls.getDeclaredMethods();
			for (Method method : methods) {
				if ( Modifier.isPublic(method.getModifiers()) &&
					 method.getName().length() > 3 &&
					 method.getName().startsWith("set") && 
					 method.getReturnType() == void.class && 
					 method.getParameterTypes().length == 1	
					) 
				{
					if (addedMethodNames.contains(method.getName()) == false) {
						setters.add(method);
					}
				}
			}
			cls = cls.getSuperclass();
		}
		
		for (Method setter : setters) {
			String att = setter.getName().substring(3).toLowerCase();
			String source = null;
			Enumeration<String> paramNames = request.getAttributeNames();
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
