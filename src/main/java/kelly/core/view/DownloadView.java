package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;

@Deprecated // 没有写完
public class DownloadView extends AbstractView {

	@Override
	public final String getContentType() {
		return "application/octet-stream";
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		response.setContentLength(actionResult.getInputStream().available());
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + actionResult.getView() + "\"");
	}

}
