package kelly.core.argument;

import java.beans.PropertyEditor;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionFinder;
import kelly.core.annotation.DateTimePattern;
import kelly.core.annotation.PathVariable;
import kelly.core.annotation.RequestParam;
import kelly.util.ClassLoaderUtils;
import kelly.util.ClassUtils;

/**
 * 提供一些通用方法
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
@SuppressWarnings("unchecked")
public abstract class AbstractActionArgumentResolver implements ActionArgumentResolver {

	protected String getSource(HttpServletRequest request, ActionArgument actionArgument) { // short method name
		return getSourceFromRequest(request, actionArgument);
	}

	protected String getSourceFromRequest(HttpServletRequest request, ActionArgument actionArgument) {
		if (request == null || actionArgument == null) {
			return null;
		}
		if (actionArgument.isAnnotatedBy(RequestParam.class)) {
			String paramName = actionArgument.getAnnotation(RequestParam.class).value();
			return request.getParameter(paramName);
		}
		if (actionArgument.isAnnotatedBy(PathVariable.class)) {
			String templateName = actionArgument.getAnnotation(PathVariable.class).value();
			Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(ActionFinder.EXTRACT_URI_TEMPLATE_VARIABLES_MAP);
			return uriTemplateVariables != null ? uriTemplateVariables.get(templateName) : null;
		}
		return null;
	}
	
	protected String getDateTimePattern(ActionArgument actionArgument) {
		if (actionArgument != null && actionArgument.isAnnotatedBy(DateTimePattern.class)) {
			return actionArgument.getAnnotation(DateTimePattern.class).value();
		}
		return null;
	}
	
	protected Class<? extends PropertyEditor> loadPropertyEditor(HttpServletRequest request, ActionArgument actionArgument) {
		String source = getSource(request, actionArgument);
		
		// 没有source,有PropertyEditor也没有意义
		if (source == null) {
			return null;
		}
		
		String editorName = actionArgument.getParameterType().getName() + "PropertyEditor";
		try {
			Class<?> cls = ClassLoaderUtils.loadClass(editorName);
			if (ClassUtils.isAssignable(cls, PropertyEditor.class)) {
				return (Class<? extends PropertyEditor>) cls;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
