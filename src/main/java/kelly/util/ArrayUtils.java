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

package kelly.util;

/**
 * 数组通用工具
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
 */
public final class ArrayUtils {

	private ArrayUtils() {
	}

	// --------------------------------------------------------------------------

	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	// nullToEmpty
	// -----------------------------------------------------------------------
	public static Object[] nullToEmpty(final Object[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_OBJECT_ARRAY;
		}
		return array;
	}

	public static Class<?>[] nullToEmpty(final Class<?>[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CLASS_ARRAY;
		}
		return array;
	}

	public static String[] nullToEmpty(final String[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_STRING_ARRAY;
		}
		return array;
	}

	public static long[] nullToEmpty(final long[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		return array;
	}

	public static int[] nullToEmpty(final int[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		return array;
	}

	public static short[] nullToEmpty(final short[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		return array;
	}

	public static char[] nullToEmpty(final char[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		return array;
	}

	public static byte[] nullToEmpty(final byte[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		return array;
	}

	public static double[] nullToEmpty(final double[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		return array;
	}

	public static float[] nullToEmpty(final float[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		return array;
	}

	public static boolean[] nullToEmpty(final boolean[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		return array;
	}

	public static Long[] nullToEmpty(final Long[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_LONG_OBJECT_ARRAY;
		}
		return array;
	}

	public static Integer[] nullToEmpty(final Integer[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_INTEGER_OBJECT_ARRAY;
		}
		return array;
	}

	public static Short[] nullToEmpty(final Short[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_SHORT_OBJECT_ARRAY;
		}
		return array;
	}

	public static Character[] nullToEmpty(final Character[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		}
		return array;
	}

	public static Byte[] nullToEmpty(final Byte[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BYTE_OBJECT_ARRAY;
		}
		return array;
	}

	public static Double[] nullToEmpty(final Double[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_DOUBLE_OBJECT_ARRAY;
		}
		return array;
	}

	public static Float[] nullToEmpty(final Float[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_FLOAT_OBJECT_ARRAY;
		}
		return array;
	}

	public static Boolean[] nullToEmpty(final Boolean[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		}
		return array;
	}
}
