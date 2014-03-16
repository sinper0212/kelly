package kelly.core.argument;

import javax.servlet.http.HttpSession;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.util.ClassUtils;

public class SessionActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, HttpSession.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		return WebContextHolder.getInstance().getRequest().getSession(true);
	}

}
