package kelly.util;



/**
 * String相关通用工具
 * 
 * @author 应卓
 *
 */
public final class StringUtils {

	// is empty
	// ----------------------------------------------------------------------------
	
	public static <T extends CharSequence> boolean isEmpty(T cs) {
		return cs == null || cs.length() == 0;
	}
	
	public static <T extends CharSequence> boolean isNotEmpty(T cs) {
		return !StringUtils.isEmpty(cs);
	}
	
	// is blank
	// ----------------------------------------------------------------------------
	public static <T extends CharSequence> boolean isBlank(T cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static <T extends CharSequence> boolean isNotBlank(T cs) {
		return !StringUtils.isBlank(cs);
	}
	
	// trim
	// ----------------------------------------------------------------------------
	
	public static String trim(String s) {
		return s == null ? null : s.trim();
	}

    public static String trimToNull(String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }
    
	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}
	
	// repeat
	// ----------------------------------------------------------------------------
	public static String repeat(char ch, int repeat) {
		final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}
	
	public static String repeat(String str, int repeat) {
		if (str == null) {
			return null;
		}
		if (repeat == 0) {
			return "";
		}
		if (repeat < 0) {
			throw new IllegalArgumentException("The number of repeat should greater than 0");
		}
		StringBuilder sb = new StringBuilder();
		while (repeat -- != 0) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	// length
	// ----------------------------------------------------------------------------
	
	public static <T extends CharSequence> int length(T cs) {
		if (cs == null) return 0;
		return cs.length();
	}

	public static <T extends CharSequence> int size(T cs) {
		return StringUtils.length(cs);
	}
	
	// reversing
    //-----------------------------------------------------------------------------
	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}
	
	// replace
	// ----------------------------------------------------------------------------
	
	@Deprecated
	public static String replaceAll(String text, String regex, String replacement) {
		return text == null ? null : text.replaceAll(regex, replacement);
	}
	
	public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }
	
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

	// remove
	// ----------------------------------------------------------------------------
	
	public static String removeWhitespace(String text) {
		if (text == null) return null;
		StringBuilder sb = new StringBuilder("");
		for (int i=0; i<text.length(); i++) {
			char ch = text.charAt(i);
			if (! Character.isWhitespace(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	// default
    //-----------------------------------------------------------------------------

	public static <T extends CharSequence> T defaultIfNull(T cs, T defaultValue) {
		return cs == null ? defaultValue : cs;
	}
	
	public static <T extends CharSequence> T defaultIfEmpty(T cs, T defaultValue) {
		if (StringUtils.isNotEmpty(cs)) {
			return cs;
		}
		return defaultValue;
	}
	
	public static <T extends CharSequence> T defaultIfBlank(T cs, T defaultValue) {
		if (StringUtils.isNotBlank(cs)) {
			return cs;
		}
		return defaultValue;
	}

}
