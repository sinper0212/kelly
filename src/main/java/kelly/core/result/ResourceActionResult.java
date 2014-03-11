package kelly.core.result;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.exception.KellyException;
import kelly.core.model.ImmutableModel;
import kelly.core.resource.Resource;

public class ResourceActionResult extends AbstractActionResult {

	public ResourceActionResult(Resource resource, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(resource, action, request, response);
	}

	@Override
	public Type getType() {
		return Type.DOWNLOAD;
	}

	@Override
	public Model getModel() {
		return ImmutableModel.INSTANCE;
	}

	@Override
	public InputStream getInputStream() {
		try {
			return super.getActualResult(Resource.class).getInputStream();
		} catch (IOException e) {
			throw new KellyException(e);
		}
	}

}
