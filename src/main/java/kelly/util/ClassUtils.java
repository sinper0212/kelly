package kelly.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/**
 * Class相关工具
 * 
 * @author 应卓
 * 
 */
public class ClassUtils {

	private ClassUtils() {
	}

	// -------------------------------------------------------------------
	private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
	static {
		primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
		primitiveWrapperMap.put(Byte.TYPE, Byte.class);
		primitiveWrapperMap.put(Character.TYPE, Character.class);
		primitiveWrapperMap.put(Short.TYPE, Short.class);
		primitiveWrapperMap.put(Integer.TYPE, Integer.class);
		primitiveWrapperMap.put(Long.TYPE, Long.class);
		primitiveWrapperMap.put(Double.TYPE, Double.class);
		primitiveWrapperMap.put(Float.TYPE, Float.class);
		primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
	}

	private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
	static {
		for (final Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
			final Class<?> wrapperClass = primitiveWrapperMap
					.get(primitiveClass);
			if (!primitiveClass.equals(wrapperClass)) {
				wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
			}
		}
	}

	// --------------------------------------------------------------------------------------------------

	public static Class<?>[] toClass(final Object... array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		final Class<?>[] classes = new Class[array.length];
		for (int i = 0; i < array.length; i++) {
			classes[i] = array[i] == null ? null : array[i].getClass();
		}
		return classes;
	}

	// --------------------------------------------------------------------------------------------------

	public static Class<?> wrapperToPrimitive(final Class<?> cls) {
		return wrapperPrimitiveMap.get(cls);
	}

	public static Class<?> primitiveToWrapper(final Class<?> cls) {
		Class<?> convertedClass = cls;
		if (cls != null && cls.isPrimitive()) {
			convertedClass = primitiveWrapperMap.get(cls);
		}
		return convertedClass;
	}

	public static Class<?>[] wrappersToPrimitives(final Class<?>... classes) {
		if (classes == null) {
			return null;
		}

		if (classes.length == 0) {
			return classes;
		}

		final Class<?>[] convertedClasses = new Class[classes.length];
		for (int i = 0; i < classes.length; i++) {
			convertedClasses[i] = wrapperToPrimitive(classes[i]);
		}
		return convertedClasses;
	}

	public static Class<?>[] primitivesToWrappers(final Class<?>... classes) {
		if (classes == null) {
			return null;
		}

		if (classes.length == 0) {
			return classes;
		}

		final Class<?>[] convertedClasses = new Class[classes.length];
		for (int i = 0; i < classes.length; i++) {
			convertedClasses[i] = primitiveToWrapper(classes[i]);
		}
		return convertedClasses;
	}
	
	// --------------------------------------------------------------------------------------------------

	public static boolean isAssignable(Class<?> cls, final Class<?> toClass) {
		return isAssignable(cls, toClass, true);
	}

	public static boolean isAssignable(Class<?> cls, final Class<?> toClass, final boolean autoboxing) {
		if (toClass == null) {
			return false;
		}
		// have to check for null, as isAssignableFrom doesn't
		if (cls == null) {
			return !toClass.isPrimitive();
		}
		// autoboxing:
		if (autoboxing) {
			if (cls.isPrimitive() && !toClass.isPrimitive()) {
				cls = primitiveToWrapper(cls);
				if (cls == null) {
					return false;
				}
			}
			if (toClass.isPrimitive() && !cls.isPrimitive()) {
				cls = wrapperToPrimitive(cls);
				if (cls == null) {
					return false;
				}
			}
		}
		if (cls.equals(toClass)) {
			return true;
		}
		if (cls.isPrimitive()) {
			if (toClass.isPrimitive() == false) {
				return false;
			}
			if (Integer.TYPE.equals(cls)) {
				return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			if (Long.TYPE.equals(cls)) {
				return Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			if (Boolean.TYPE.equals(cls)) {
				return false;
			}
			if (Double.TYPE.equals(cls)) {
				return false;
			}
			if (Float.TYPE.equals(cls)) {
				return Double.TYPE.equals(toClass);
			}
			if (Character.TYPE.equals(cls)) {
				return Integer.TYPE.equals(toClass)
						|| Long.TYPE.equals(toClass)
						|| Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			if (Short.TYPE.equals(cls)) {
				return Integer.TYPE.equals(toClass)
						|| Long.TYPE.equals(toClass)
						|| Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			if (Byte.TYPE.equals(cls)) {
				return Short.TYPE.equals(toClass)
						|| Integer.TYPE.equals(toClass)
						|| Long.TYPE.equals(toClass)
						|| Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			// should never get here
			return false;
		}
		return toClass.isAssignableFrom(cls);
	}
	
    // Inner class
	// --------------------------------------------------------------------------------------------------
	public static boolean isInnerClass(final Class<?> cls) {
		return cls != null && cls.getEnclosingClass() != null;
	}
	
    // Superclasses/Superinterfaces
	// --------------------------------------------------------------------------------------------------
    public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
        doGetAllInterfaces(cls, interfacesFound);

        return new ArrayList<Class<?>>(interfacesFound);
    }
   
    private static void doGetAllInterfaces(Class<?> cls, final HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            final Class<?>[] interfaces = cls.getInterfaces();

            for (final Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    doGetAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
         }
     }
}
