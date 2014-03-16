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
package kelly.core.action;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import kelly.core.annotation.Nullable;
import kelly.util.Validate;

/**
 * ActionArgument用来描述Action的参数
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 *
 */
public final class ActionArgument {

	private final int index;
	private final Class<?> type;
	private final Map<Class<? extends Annotation>, Annotation> annotationCache = new IdentityHashMap<Class<? extends Annotation>, Annotation>();
	private final Action action;

	public ActionArgument(int index, Class<?> type, Annotation[] annotations, Action action) {
		Validate.isTrue(index >= 0);
		Validate.notNull(type);
		Validate.noNullElements(annotations);
		Validate.notNull(action);
		this.index = index;
		this.type = type;
		this.action = action;

		for (Annotation annotation : annotations) {
			annotationCache.put(annotation.annotationType(), annotation);
		}		
//		annotationConflictedCheck();
	}

	public int getParameterIndex() {
		return index;
	}

	public Class<?> getParameterType() {
		return type;
	}

	public Annotation[] getParameterAnnotations() {
		Collection<Annotation> collection = annotationCache.values();
		return collection.toArray(new Annotation[collection.size()]);
	}
	
	public Action getAction() {
		return action;
	}

	// ---------------------------------------------------------------------------------------

	public boolean isNullable() {
		return isAnnotatedBy(Nullable.class);
	}
	
	public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return getAnnotation(annotationClass) != null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return (T) annotationCache.get(annotationClass);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(action.getControllerClass().getName());
		sb.append('.');
		sb.append(action.getMethod().getName());
		sb.append("(");
		for (int i = 0; i < action.getMethodParameterTypes().length; i++) {
			sb.append(action.getMethodParameterTypes()[i].getName());
			if (i != action.getMethodParameterTypes().length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		sb.append(" (" + index + ")");
		sb.append(getParameterAnnotations());
		return sb.toString();
	}
	
	// ---------------------------------------------------------------------------------------

	// 不再判断同一个Action参数上的标注是否有冲突
//	@Deprecated
//	@SuppressWarnings("unused")
//	private void annotationConflictedCheck() {
//		if (isAnnotatedBy(RequestParam.class) && isAnnotatedBy(PathVariable.class)) {
//			throw new KellyException("Cannot use @RequestParam and @PathVariable at same time");
//		}
//	}

}
