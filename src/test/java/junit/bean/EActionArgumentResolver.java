package junit.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.annotation.Component;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

@Component
public class EActionArgumentResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, HttpServletRequest httpServletRequest) {
		return false;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor,
			HttpServletRequest request, HttpServletResponse response)
			throws KellyException {
		return null;
	}



}
