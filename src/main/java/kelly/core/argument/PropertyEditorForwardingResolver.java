package kelly.core.argument;

import java.beans.PropertyEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;
import kelly.util.ClassLoaderUtils;
import kelly.util.ClassUtils;
import kelly.util.ReflectionUtils;

public class PropertyEditorForwardingResolver extends AbstractNeedSourceAciontArgumentResolver {

	private final ThreadLocal<Class<? extends PropertyEditor>> cache = new ThreadLocal<Class<? extends PropertyEditor>>();

	@Override
	protected boolean doSupports(ActionArgument actionArgument, Castor castor, HttpServletRequest request) {
		Class<? extends PropertyEditor> cls = loadPropertyEditor(actionArgument);
		if (cls != null) {
			cache.set(cls);
			return true;
		}
		return false;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		PropertyEditor editor = ReflectionUtils.invokeConstructor(cache.get());
		editor.setAsText(getSource(actionArgument, request));

		try {
			return editor.getValue();
		} finally {
			cache.set(null);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends PropertyEditor> loadPropertyEditor(ActionArgument actionArgument) {
		String editorName = actionArgument.getParameterType().getName() + "PropertyEditor";
		try {
			Class<?> cls = ClassLoaderUtils.loadClass(editorName);
			if (ClassUtils.isAssignable(cls, PropertyEditor.class)) {
				return (Class<? extends PropertyEditor>) cls;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

}
