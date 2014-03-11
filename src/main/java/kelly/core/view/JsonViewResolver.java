package kelly.core.view;

import java.util.Locale;

import kelly.core.annotation.Json;
import kelly.core.json.JsonFactory;
import kelly.core.result.ActionResult;

public class JsonViewResolver extends AbstractViewResolver {

	private final JsonFactory jsonFactory;
	
	public JsonViewResolver(JsonFactory jsonFactory) {
		super.setOrder(200);
		this.jsonFactory = jsonFactory;
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getAction().getMethod().getAnnotation(Json.class) != null) {
			return new JsonView(this.jsonFactory);
		}
		return null;
	}

}
