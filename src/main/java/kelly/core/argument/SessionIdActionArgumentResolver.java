package kelly.core.argument;

import kelly.core.action.ActionArgument;
import kelly.core.annotation.SessionId;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.KellyException;

public class SessionIdActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return cls == String.class && actionArgument.isAnnotatedBy(SessionId.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) throws KellyException {
		return WebContextHolder.getInstance().getRequest().getSession().getId();
	}

}
