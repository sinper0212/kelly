package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

@Deprecated // 没有写完
public class DownloadViewResolver implements ViewResolver {

	@Override
	public int getOrder() {
		return 100;
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.DOWNLOAD) {
			return null;
		}
		return null;
	}

}
