package kelly.core.exception;

/**
 * kelly框架抛出的异常
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class KellyException extends RuntimeException {

	private Throwable cause = null;
	private String message = null;
	
	public static KellyException create() {
		return new KellyException();
	}
	
	public static KellyException create(String message) {
		return new KellyException(message);
	}
	
	public static KellyException create(Throwable cause) {
		return new KellyException(cause);
	}
	
	// ------------------------------------------------------------------------

	public KellyException() {
	}
	
	public KellyException(String msg) {
		this.message = msg;
	}
	
	public KellyException(Throwable cause) {
		if (cause == null) {
			return;
		} else {
			this.message = cause.getMessage();
			this.cause = cause;
		}
	}
	
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return getClass().getSimpleName() + " : " + message == null ? "" : message;
	}
	
	// ------------------------------------------------------------------------

	public Throwable getCause() {
		return cause;
	}

	public String getMessage() {
		return message;
	}

}
