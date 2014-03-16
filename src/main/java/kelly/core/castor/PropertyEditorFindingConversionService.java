package kelly.core.castor;

import java.beans.PropertyEditor;

import kelly.util.ClassLoaderUtils;
import kelly.util.ReflectionUtils;

public class PropertyEditorFindingConversionService extends ConversionService implements Castor {

	private final ThreadLocal<PropertyEditor> editorHolder = new ThreadLocal<PropertyEditor>();
	
	@Override
	public boolean canConvert(Class<?> targetType) {
		if (super.canConvert(targetType)) {
			return true;
		}
		
		PropertyEditor editor = findPropertyEditor(targetType);
		if (editor != null) {
			editorHolder.set(editor);
			return true;
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T convert(String source, Class<T> targetType) {
		if (super.canConvert(targetType)) {
			return super.convert(source, targetType);
		}
		PropertyEditor editor = editorHolder.get();
		editor.setAsText(source);
		try {
			return (T) editor.getValue();
		} finally {
			editorHolder.set(null);
		}
	}
	
	// ------------------------------------------------------------------
	
	private PropertyEditor findPropertyEditor(Class<?> targetType) {
		try {
			Class<?> cls = ClassLoaderUtils.loadClass(targetType.getName() + "PropertyEditor");
			return (PropertyEditor) ReflectionUtils.invokeConstructor(cls);
		} catch (Throwable e) {
			return null;
		}
	}
	
}
