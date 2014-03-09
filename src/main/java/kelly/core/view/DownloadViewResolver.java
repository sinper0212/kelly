package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class DownloadViewResolver implements ViewResolver {

	@Override
	public int getOrder() {
		return 100;
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.DOWNLOAD) {
			return new DownloadView();
		}
		return null;
	}

}
