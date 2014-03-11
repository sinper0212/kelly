package kelly.core.argument;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionFinder;
import kelly.core.annotation.CookieValue;
import kelly.core.annotation.PathVariable;
import kelly.core.annotation.RequestParam;
import kelly.core.castor.Castor;

public abstract class AbstractNeedSourceAciontArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public final boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		return (getSource(actionArgument, request) != null || actionArgument.isNullable())
					&& doSupports(actionArgument, castor, request);
	}

	protected abstract boolean doSupports(ActionArgument actionArgument, Castor castor, HttpServletRequest request);

	@SuppressWarnings("unchecked")
	protected final String getSource(ActionArgument actionArgument, HttpServletRequest request) {
		if (request == null || actionArgument == null) {
			return null;
		}
		
		// 支持@RequestParam
		if (actionArgument.isAnnotatedBy(RequestParam.class)) {
			String paramName = actionArgument.getAnnotation(RequestParam.class).value();
			return request.getParameter(paramName);
		}
		
		// 支持@PathVariable
		if (actionArgument.isAnnotatedBy(PathVariable.class)) {
			String templateName = actionArgument.getAnnotation(PathVariable.class).value();
			Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(ActionFinder.EXTRACT_URI_TEMPLATE_VARIABLES_MAP);
			return uriTemplateVariables != null ? uriTemplateVariables.get(templateName) : null;
		}
		
		// 支持@CookieValue
		if (actionArgument.isAnnotatedBy(CookieValue.class)) {
			String cookieName = actionArgument.getAnnotation(CookieValue.class).value();
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
			return null;
		}
		
		return null;
	}
}