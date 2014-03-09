package kelly.core.result;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;

public class ByteArrayActionResult extends InputStreamActionResult {

	public ByteArrayActionResult(byte[] bytes, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(new ByteArrayInputStream(bytes), action, request, response);
	}

}
