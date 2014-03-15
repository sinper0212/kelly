package kelly.core.argument;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;

public class ResponseActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, HttpServletResponse.class) ||
				ClassUtils.isAssignable(cls, ServletResponse.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) throws KellyException {
		return WebContextHolder.getInstance().getResponse();
	}

}
