package kelly.core.castor;

import java.beans.PropertyEditor;
import java.util.IdentityHashMap;
import java.util.Map;

import kelly.util.ClassLoaderUtils;
import kelly.util.ReflectionUtils;

public class PropertyEditorFindingConversionService 
			extends ConversionService implements Castor
{
	private Map<Class<?>, PropertyEditor> propertyEditorCache = new IdentityHashMap<Class<?>, PropertyEditor>();

	@Override
	public boolean canConvert(Class<?> targetType) {
		if (super.canConvert(targetType)) {
			return true;
		}
		
		PropertyEditor editor = propertyEditorCache.get(targetType);
		if (editor != null) {
			return true;
		}
		else {
			editor = findPropertyEditor(targetType);
			if (editor == null) return false;
			
			propertyEditorCache.put(targetType, editor);
			return true;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T convert(String source, Class<T> targetType) {
		if (super.canConvert(targetType)) {
			return super.convert(source, targetType);		
		}
		
		PropertyEditor editor = propertyEditorCache.get(targetType);
		editor.setAsText(source);
		return (T) editor.getValue();
	}
	
	// ------------------------------------------------------------------
	
	private PropertyEditor findPropertyEditor(Class<?> targetType) {
		try {
			Class<?> cls = ClassLoaderUtils.loadClass(targetType.getName() + "PropertyEditor");
			return (PropertyEditor) ReflectionUtils.invokeConstructor(cls);
		} catch (Exception e) {
			return null;
		}
	}
	
}
