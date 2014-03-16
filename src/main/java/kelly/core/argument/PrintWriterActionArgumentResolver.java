package kelly.core.argument;

import java.io.IOException;
import java.io.PrintWriter;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.InputOutputException;
import kelly.util.ClassUtils;

public class PrintWriterActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, PrintWriter.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		try {
			PrintWriter out = WebContextHolder.getInstance().getResponse().getWriter();
			OutputHolder.getInstance().setPrintWriter(out);
			return out;
		} catch (IOException ex) {
			throw new InputOutputException(ex);
		}
	}

}
