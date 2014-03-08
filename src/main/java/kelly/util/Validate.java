package kelly.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 断言工具
 * 
 * @author 应卓
 * 
 */
public final class Validate {

	// is true
	// ---------------------------------------------------------------------------------
	
	public static void isTrue(boolean expression) {
		Validate.isTrue(expression, "The expression is false");
	}

	public static void isTrue(boolean expression, String message) {
		if (expression == false) {
			throw new IllegalArgumentException(message);
		}
	}
	
	// not null
	// ---------------------------------------------------------------------------------

	public static <T> T notNull(T obj) {
		return notNull(obj, "The object is null");
	}

	public static <T> T notNull(T obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
		return obj;
	}

	// not empty
	// ---------------------------------------------------------------------------------

	public static <T> T[] notEmpty(T[] array) {
		return notEmpty(array, "The array is null or empty");
	}

	public static <T> T[] notEmpty(T[] array, String message) {
		Validate.notNull(array, message);
		if (array.length == 0) {
			throw new IllegalArgumentException(message);
		}
		return array;
	}
	
	public static <T extends Collection<?>> T notEmpty(T collection) {
		return notEmpty(collection, "The collection is null or empty");
	}
	
	public static <T extends Collection<?>> T notEmpty(T collection, String message) {
		Validate.notNull(collection, message);
		if (collection.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return collection;
	}
	
	public static <T extends Map<?, ?>> T notEmpty(T map) {
		return Validate.notEmpty(map, "The map is null or empty");
	}
	
	public static <T extends Map<?, ?>> T notEmpty(T map, String message) {
		Validate.notNull(map, message);
		if (map.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return map;
	}
	
	public static <T extends CharSequence> T notEmpty(T cs) {
		return Validate.notEmpty(cs, "The chars is null or empty");
	}
	
	public static <T extends CharSequence> T notEmpty(T cs, String message) {
		Validate.notNull(cs, message);
		if (cs.length() == 0) {
			throw new IllegalArgumentException(message);
		}
		return cs;
	}
	
	// not blank
	// ---------------------------------------------------------------------------------
	
	public static <T extends CharSequence> T notBlank(T cs) {
		return notBlank(cs, "The chars is null or blank");
	}
	
	public static <T extends CharSequence> T notBlank(T cs, String message) {
		Validate.notEmpty(cs, message);
		for (int i = 0; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (! Character.isWhitespace(c)) {
				return cs;
			}
		}
		throw new IllegalArgumentException(message);
	}
	
	// has text
	// ---------------------------------------------------------------------------------
	
	public static <T extends CharSequence> T hasText(T cs) {
		return Validate.notBlank(cs);
	}
	
	public static <T extends CharSequence> T hasText(T cs, String message) {
		return Validate.notBlank(cs, message);
	}
	
	// no null elements
	// ---------------------------------------------------------------------------------
	
	public static <T> T[] noNullElements(T[] array) {
		return noNullElements(array, "The array has at least one null element");
	}
	
	public static <T> T[] noNullElements(T[] array, String message) {
		Validate.notNull(array, message);
		for (int i=0; i<array.length; i++) {
			if (null == array[i]) {
				throw new IllegalArgumentException(message);
			}
		}
		return array;
	}

	public static <T extends Iterable<?>> T noNullElements(T iterable) {
		return Validate.noNullElements(iterable, "The iterable contains at least one null element");
	}
	
	public static <T extends Iterable<?>> T noNullElements(T iterable, String message) {
		Validate.notNull(iterable, message);
		for (Iterator<?> it = iterable.iterator(); it.hasNext(); ) {
			if (null == it.next()){
				throw new IllegalArgumentException(message);
			}
		}
		return iterable;
	}
	
	public static <T extends Map<?, ?>> T noNullKeys(T map) {
		return Validate.noNullKeys(map, "The map contains at least one null-key");
	}
	
	public static <T extends Map<?, ?>> T noNullKeys(T map, String message) {
		Validate.notNull(map, message);
		Validate.noNullElements(map.keySet(), message);
		return map;
	}
	
	public static <T extends Map<?, ?>> T noNullValues(T map) {
		return Validate.noNullValues(map, "The map contains at least one null-value");
	}
	
	public static <T extends Map<?, ?>> T noNullValues(T map, String message) {
		Validate.notNull(map, message);
		Validate.noNullElements(map.values(), message);
		return map;
	}
	
	public static <T extends Map<?, ?>> T noNullKeyOrValue(T map) {
		return Validate.noNullKeyOrValue(map, "The map contains null-key or null-value");
	}
	
	public static <T extends Map<?, ?>> T noNullKeyOrValue(T map, String message) {
		Validate.notNull(map, message);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getKey() == null) {
				throw new IllegalArgumentException(message);
			}
			if (entry.getValue() == null) {
				throw new IllegalArgumentException(message);
			}
		}
		return map;
	}

}
