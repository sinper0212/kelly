package kelly.core.exception;

public class BeanException extends RuntimeException {

	public BeanException() {
	}
	
	public BeanException(String msg) {
		super(msg);
	}
	
	public BeanException(Throwable cause) {
		super(cause);
	}

	public BeanException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
