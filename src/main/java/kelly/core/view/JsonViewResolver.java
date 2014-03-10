package kelly.core.view;

import java.util.Locale;

import kelly.core.annotation.Json;
import kelly.core.result.ActionResult;

public class JsonViewResolver extends AbstractViewResolver {

	public JsonViewResolver() {
		super.setOrder(200);
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getAction().getMethod().getAnnotation(Json.class) != null) {
			return new JsonView();
		}
		return null;
	}

}
