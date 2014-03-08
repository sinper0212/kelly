package kelly.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;



/**
 * 反射相关通用工具
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
 */
public class ReflectionUtils {

	private ReflectionUtils() {
	}

	// access
	// ------------------------------------------------------------------------------------

	public static boolean isAccessible(Member member) {
		return member != null && Modifier.isPublic(member.getModifiers())
				&& !member.isSynthetic();
	}

	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
					.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier
				.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	public static void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor
				.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}

	// Constructor
	// ------------------------------------------------------------------------------------
	
	public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
		Validate.notNull(cls, "class cannot be null");
		try {
			return ReflectionUtils.getAccessibleConstructor(cls.getConstructor(parameterTypes));
		} catch (final NoSuchMethodException e) {
			return null;
		}
	}

	private static <T> Constructor<T> getAccessibleConstructor(
			Constructor<T> ctor) {
		Validate.notNull(ctor, "constructor cannot be null");
		return ReflectionUtils.isAccessible(ctor)
				&& Modifier.isPublic(ctor.getDeclaringClass().getModifiers()) ? ctor : null;
	}
	
	public static <T> T invokeConstructor(final Class<T> cls) {
		return invokeConstructor(cls, null, null);
	}
	
	public static <T> T invokeConstructor(final Class<T> cls, Object[] args) {
		args = ArrayUtils.nullToEmpty(args);
		final Class<?> parameterTypes[] = ClassUtils.toClass(args);
		return invokeConstructor(cls, args, parameterTypes);
	}
	
	public static <T> T invokeConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) {
		try {
			args = ArrayUtils.nullToEmpty(args);
			parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
			final Constructor<T> ctor = cls.getConstructor(parameterTypes);
			return ctor.newInstance(args);
		} catch (Exception e) {
			throw ExceptionUtils.rethrow(e);
		}
	}

	// Method
	// ------------------------------------------------------------------------------------
	
	public static Method getMethod(Class<?> clazz, String name) {
		return getMethod(clazz, name, ArrayUtils.EMPTY_CLASS_ARRAY);
	}
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Validate.notNull(clazz, "Class must not be null");
		Validate.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes,
								method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, ArrayUtils.EMPTY_OBJECT_ARRAY);
	}
	
	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (Exception ex) {
			throw ExceptionUtils.rethrow(ex);
		}
	}
	
	// Field 
	// ------------------------------------------------------------------------------------
	
	public static Field getField(Class<?> clazz, String name) {
		return getField(clazz, name, null);
	}
	
	public static Field getField(Class<?> clazz, String name, Class<?> type) {
		Validate.notNull(clazz, "Class must not be null");
		Validate.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName()))
						&& (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	public static void setField(Field field, Object target, Object value) {
		try {
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			throw ExceptionUtils.rethrow(ex);
		}
	}

	public static Object getField(Field field, Object target) {
		try {
			return field.get(target);
		} catch (IllegalAccessException ex) {
			throw ExceptionUtils.rethrow(ex);
		}
	}
}
