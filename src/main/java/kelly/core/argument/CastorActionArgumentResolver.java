package kelly.core.argument;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class CastorActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		return getSource(actionArgument) != null && castor.canConvert(actionArgument.getParameterType());
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) throws KellyException {
		try {
			ActionArgumentHolder.getInstance().setActionArgument(actionArgument);
			return castor.convert(getSource(actionArgument), actionArgument.getParameterType());
		} finally {
			ActionArgumentHolder.getInstance().setActionArgument(null);
		}
	}

}
