/*
 * Copyright 2002-2012 Zhuo Ying. All rights reserved.
 * Email: yingzhor@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
