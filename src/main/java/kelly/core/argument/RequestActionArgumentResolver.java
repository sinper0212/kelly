package kelly.core.argument;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.util.ClassUtils;

public class RequestActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, HttpServletRequest.class) || 
				ClassUtils.isAssignable(cls, ServletRequest.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		return WebContextHolder.getInstance().getRequest();
	}

}
