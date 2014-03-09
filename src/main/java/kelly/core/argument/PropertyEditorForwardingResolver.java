package kelly.core.argument;

import java.beans.PropertyEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ReflectionUtils;

public class PropertyEditorForwardingResolver extends AbstractActionArgumentResolver {

	private final ThreadLocal<Class<? extends PropertyEditor>> cache = new ThreadLocal<Class<? extends PropertyEditor>>();

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		Class<? extends PropertyEditor> cls = loadPropertyEditor(request, actionArgument);
		if (cls != null) {
			cache.set(cls);
			return true;
		}
		return false;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		PropertyEditor editor = ReflectionUtils.invokeConstructor(cache.get());
		editor.setAsText(getSource(request, actionArgument));

		try {
			return editor.getValue();
		} finally {
			cache.set(null);
		}
	}

}
