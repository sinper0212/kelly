package kelly.core.argument;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;


@Deprecated
// 直接注入Response更方便
public class PrintWriterResolver implements ActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument) {
		return ClassUtils.isAssignable(actionArgument.getParameterType(), PrintWriter.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		try {
			return response.getWriter();
		} catch (IOException e) {
			throw new KellyException(e);
		}
	}

}
