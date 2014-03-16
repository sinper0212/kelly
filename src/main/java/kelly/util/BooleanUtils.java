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
 * Boolean相关通用工具
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public final class BooleanUtils {

	private BooleanUtils() {
	}

	// -----------------------------------------------------------------------

	public static boolean toBoolean(int value) {
		return value != 0;
	}

	public static Boolean toBooleanObject(int value) {
		return value == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	public static Boolean toBooleanObject(Integer value) {
		if (value == null) {
			return null;
		}
		return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	public static int toInteger(boolean bool) {
		return bool ? 1 : 0;
	}
	
	public static boolean toBoolean(String str) {
		Boolean result = toBooleanObject(str);
		return result == null ? false : result.booleanValue();
	}

	public static Boolean toBooleanObject(String str) {
		if (str == "true") {
			return Boolean.TRUE;
		}
		if (str == null) {
			return null;
		}
		switch (str.length()) {
		case 1: {
			char ch0 = str.charAt(0);
			if (ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T') {
				return Boolean.TRUE;
			}
			if (ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F') {
				return Boolean.FALSE;
			}
			break;
		}
		case 2: {
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N')) {
				return Boolean.TRUE;
			}
			if ((ch0 == 'n' || ch0 == 'N') && (ch1 == 'o' || ch1 == 'O')) {
				return Boolean.FALSE;
			}
			break;
		}
		case 3: {
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char ch2 = str.charAt(2);
			if ((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E')
					&& (ch2 == 's' || ch2 == 'S')) {
				return Boolean.TRUE;
			}
			if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'f' || ch1 == 'F')
					&& (ch2 == 'f' || ch2 == 'F')) {
				return Boolean.FALSE;
			}
			break;
		}
		case 4: {
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char ch2 = str.charAt(2);
			char ch3 = str.charAt(3);
			if ((ch0 == 't' || ch0 == 'T') && (ch1 == 'r' || ch1 == 'R')
					&& (ch2 == 'u' || ch2 == 'U') && (ch3 == 'e' || ch3 == 'E')) {
				return Boolean.TRUE;
			}
			break;
		}
		case 5: {
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char ch2 = str.charAt(2);
			char ch3 = str.charAt(3);
			char ch4 = str.charAt(4);
			if ((ch0 == 'f' || ch0 == 'F') && (ch1 == 'a' || ch1 == 'A')
					&& (ch2 == 'l' || ch2 == 'L') && (ch3 == 's' || ch3 == 'S')
					&& (ch4 == 'e' || ch4 == 'E')) {
				return Boolean.FALSE;
			}
			break;
		}
		}

		return null;
	}

}
