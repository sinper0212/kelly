package kelly.util;

/**
 * 异常相关通用工具
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ExceptionUtils {

	private ExceptionUtils() {
	}
	
	// ---------------------------------------------------------------------

	public static RuntimeException rethrow(Throwable e) {
		if (e == null) throw new NullPointerException();
		
		if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		}
		else {
			throw new RuntimeException(e);
		}
	}

}
