package kelly.core.exception;

/**
 * kelly框架抛出的异常
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class KellyException extends RuntimeException {

	public KellyException() {
		super();
	}
	
	public KellyException(String msg) {
		super(msg);
	}

	public KellyException(Throwable cause) {
		super(cause);
	}
	
	public KellyException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
