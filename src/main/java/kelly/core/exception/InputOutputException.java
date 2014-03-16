package kelly.core.exception;

import java.io.IOException;

public class InputOutputException extends KellyException {

	public InputOutputException(IOException cause) {
		super(cause);
	}

}
