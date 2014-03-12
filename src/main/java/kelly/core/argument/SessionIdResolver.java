package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.annotation.SessionId;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class SessionIdResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		return String.class == actionArgument.getParameterType()
					&& actionArgument.isAnnotatedBy(SessionId.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		return request.getSession(true).getId();
	}

}
