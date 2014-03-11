package kelly.core.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.core.model.MapModel;
import kelly.util.ClassUtils;

public class ModelResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		Class<?> type = actionArgument.getParameterType();
		return ClassUtils.isAssignable(type, Model.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		MapModel model = new MapModel();
		ModelHolder.getInstance().setModel(model);
		return model;
	}

}
