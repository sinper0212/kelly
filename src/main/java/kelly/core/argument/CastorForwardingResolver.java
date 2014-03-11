package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class CastorForwardingResolver extends AbstractNeedSourceAciontArgumentResolver {

	@Override
	protected boolean doSupports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		String source = getSource(actionArgument, request);
		return source != null ? castor.canConvert(actionArgument.getParameterType()) : false;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		return castor.convert(getSource(actionArgument, request), actionArgument.getParameterType());
	}

}
