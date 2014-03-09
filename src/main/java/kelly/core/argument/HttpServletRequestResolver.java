package kelly.core.argument;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;

public class HttpServletRequestResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, HttpServletRequest httpServletRequest) {
		Class<?> type = actionArgument.getParameterType();
		return ClassUtils.isAssignable(type, HttpServletRequest.class) || ClassUtils.isAssignable(type, ServletRequest.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		return request;
	}

}
