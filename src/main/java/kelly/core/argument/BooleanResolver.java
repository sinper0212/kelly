package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class BooleanResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument) {
		Class<?> type = actionArgument.getParameterType();
		return type == boolean.class || type == Boolean.class;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		Class<?> type = actionArgument.getParameterType();
		String param = actionArgument.getHttpRequestParameter();
		boolean nullable = actionArgument.isNullable();
		
		String source = request.getParameter(param);
		if (source == null) {
			if (nullable) {
				return type == Boolean.class ? null : Boolean.FALSE;
			} else {
				throw new KellyException("Cannnot resolver http request parameter :" + param);
			}
		}
		else {
			return Boolean.parseBoolean(source);
		}
	}

}
