package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class CastorForwardingResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		String source = getSource(request, actionArgument);
		return source != null ? castor.canConvert(actionArgument.getParameterType()) : false;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		return castor.convert(getSource(request, actionArgument), actionArgument.getParameterType());
	}

}
