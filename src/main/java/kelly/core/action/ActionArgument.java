package kelly.core.action;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import kelly.core.annotation.DateTimePattern;
import kelly.core.annotation.Nullable;
import kelly.core.annotation.PathVariable;
import kelly.core.annotation.RequestParam;
import kelly.core.exception.KellyException;
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
		annotationConflictedCheck();
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

	public boolean isNullable() {		// @Nullable
		return isAnnotatedBy(Nullable.class);
	}
	
	public String getHttpRequestParameter() { // @RequestParam
		RequestParam anno = getAnnotation(RequestParam.class);
		return anno != null ? anno.value() : null;
	}
	
	public String getDateTimePattern() {			// @DateTimePattern
		DateTimePattern anno = getAnnotation(DateTimePattern.class);
		return anno != null ? anno.value() : null;
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

	private void annotationConflictedCheck() {
		if (isAnnotatedBy(RequestParam.class) && isAnnotatedBy(PathVariable.class)) {
			throw new KellyException("Cannot use @RequestParam and @PathVariable at same time");
		}
	}
}
