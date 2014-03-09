package kelly.core.view;

import java.util.Locale;

import kelly.core.annotation.Json;
import kelly.core.result.ActionResult;

public class JsonViewResolver implements ViewResolver {

	@Override
	public int getOrder() {
		return 200;
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getAction().getMethod().getAnnotation(Json.class) != null) {
			return new JsonView();
		}
		return null;
	}

}
