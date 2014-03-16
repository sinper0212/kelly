package kelly.core.argument;

import kelly.core.action.ActionArgument;
import kelly.core.annotation.SessionId;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;

public class SessionIdActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return cls == String.class && actionArgument.isAnnotatedBy(SessionId.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		return WebContextHolder.getInstance().getRequest().getSession().getId();
	}

}
