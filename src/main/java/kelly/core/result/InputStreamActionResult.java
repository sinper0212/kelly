package kelly.core.result;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.model.ImmutableModel;

public final class InputStreamActionResult extends AbstractActionResult {

	public InputStreamActionResult(InputStream is, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(is, action, request, response);
	}

	@Override
	public InputStream getInputStream() {
		return getActualResult(InputStream.class);
	}

	@Override
	public Type getType() {
		return Type.DOWNLOAD;
	}

	@Override
	public Model getModel() {
		return ImmutableModel.INSTANCE;
	}

}
