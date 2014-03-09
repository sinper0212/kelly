package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class BooleanResolver extends AbstractActionArgumentResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest httpServletRequest) {
		Class<?> type = actionArgument.getParameterType();
		return type == boolean.class || type == Boolean.class;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		Class<?> type = actionArgument.getParameterType();
		String source = getSource(request, actionArgument);
		boolean nullable = actionArgument.isNullable();

		if (source == null) {
			if (nullable) {
				return type == Boolean.class ? null : Boolean.FALSE;
			} else {
				throw new KellyException("Cannnot resolver action argument");
			}
		}
		else {
			return Boolean.parseBoolean(source);
		}
	}

}
