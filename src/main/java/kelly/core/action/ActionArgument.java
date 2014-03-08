package kelly.core.action;

import java.lang.annotation.Annotation;

import kelly.core.annotation.DateTimePattern;
import kelly.core.annotation.Nullable;
import kelly.core.annotation.RequestParam;
import kelly.util.Validate;

/**
 * ActionArgument用来描述Action的参数
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionArgument {

	private final int index;
	private final Class<?> type;
	private final Annotation[] annotations;
	private final Action action;				// just for log
	
	public ActionArgument(int index, Class<?> type, Annotation[] annotations, Action action) {
		Validate.isTrue(index >= 0);
		Validate.notNull(type);
		Validate.noNullElements(annotations);
		Validate.notNull(action);
		this.index = index;
		this.type = type;
		this.annotations = annotations;
		this.action = action;
	}

	public int getParameterIndex() {
		return index;
	}

	public Class<?> getParameterType() {
		return type;
	}

	public Annotation[] getParameterAnnotations() {
		return annotations;
	}
	
	// ---------------------------------------------------------------------------------------

	public final boolean isNullable() {		// @Nullable
		return isAnnotatedBy(Nullable.class);
	}
	
	public final String getHttpRequestParameter() { // @RequestParam
		RequestParam anno = getAnnotation(RequestParam.class);
		return anno != null ? anno.value() : null;
	}
	
	public final String getDateTimePattern() {			// @DateTimePattern
		DateTimePattern anno = getAnnotation(DateTimePattern.class);
		return anno != null ? anno.value() : null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		if (annotations.length == 0 || annotationClass == null) return null;
		for (Annotation annotation : getParameterAnnotations()) {
			if (annotation.annotationType() == annotationClass) {
				return (T) annotation;
			}
		}
		return null;
	}

	public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return getAnnotation(annotationClass) != null;
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

}
