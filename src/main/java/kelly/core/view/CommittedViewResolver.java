package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class CommittedViewResolver extends AbstractViewResolver {

	public CommittedViewResolver() {
		super.setOrder(Integer.MIN_VALUE);
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.COMMITTED) {
			return new CommittedView();
		}
		return null;
	}

}
