package kelly.core.view;

import java.util.Locale;

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class RedirectViewResolver extends AbstractViewResolver {

	public RedirectViewResolver() {
		setOrder(0);
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.REDIRECT) {
			String location = actionResult.getView().substring("redirect:".length());
			return new RedirectView(location);
		}
		return null;
	}

}
