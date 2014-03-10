package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class DownloadViewResolver extends AbstractViewResolver {

	public DownloadViewResolver() {
		super.setOrder(100);
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.DOWNLOAD) {
			return new DownloadView();
		}
		return null;
	}

}
