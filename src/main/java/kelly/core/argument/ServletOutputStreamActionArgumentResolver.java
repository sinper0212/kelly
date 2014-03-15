package kelly.core.argument;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.KellyException;
import kelly.util.ClassUtils;

public class ServletOutputStreamActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getClass();
		return ClassUtils.isAssignable(cls, ServletOutputStream.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) throws KellyException {
		try {
			ServletOutputStream out = WebContextHolder.getInstance().getResponse().getOutputStream();
			OutputHolder.getInstance().setServletOutputStream(out);
			return out;
		} catch (IOException e) {
			throw new KellyException(e);
		}
	}

}
