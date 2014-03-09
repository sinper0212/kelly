package kelly.core.view;

import java.util.Locale;

import kelly.core.Ordered;
import kelly.core.result.ActionResult;

public interface ViewResolver extends Ordered {

	View resolve(ActionResult actionResult, Locale locale);

}
