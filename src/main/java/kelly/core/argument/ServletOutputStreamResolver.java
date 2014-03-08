package kelly.core.argument;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;

@Deprecated
public class ServletOutputStreamResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument) {
		Class<?> type = actionArgument.getParameterType();
		return ClassUtils.isAssignable(type, ServletOutputStream.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			throw new KellyException(e);
		}
	}

}
