package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;

public class HttpSessionResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument) {
		Class<?> type = actionArgument.getParameterType();
		return ClassUtils.isAssignable(type, HttpSession.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor,
			HttpServletRequest request, HttpServletResponse response)
			throws KellyException {
		return request.getSession(true);
	}

}
