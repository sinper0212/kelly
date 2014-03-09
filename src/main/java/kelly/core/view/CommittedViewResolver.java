package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class CommittedViewResolver implements ViewResolver {

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.COMMITTED) {
			return null;
		}
		return null;
	}

}
