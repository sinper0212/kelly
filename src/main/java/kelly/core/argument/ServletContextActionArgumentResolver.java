package kelly.core.argument;

import javax.servlet.ServletContext;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.util.ClassUtils;

public class ServletContextActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, ServletContext.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		return WebContextHolder.getInstance().getServletContext();
	}

}
