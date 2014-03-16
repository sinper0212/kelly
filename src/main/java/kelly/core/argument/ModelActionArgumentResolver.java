package kelly.core.argument;

import kelly.core.Model;
import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.model.MapModel;
import kelly.util.ClassUtils;

public class ModelActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getParameterType();
		return ClassUtils.isAssignable(cls, Model.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		Model cached = ModelHolder.getInstance().getModel();
		if (cached != null) {
			return cached;
		} else {
			Model model = new MapModel();
			ModelHolder.getInstance().setModel(model);
			return model;
		}
	}

}
