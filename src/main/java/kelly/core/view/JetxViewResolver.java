package kelly.core.view;

import java.util.Locale;

import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.ResourceNotFoundException;
import kelly.core.result.ActionResult;


public class JetxViewResolver extends AbstractViewResolver {

	private final JetEngine jetEngine = JetEngine.create();

	public JetxViewResolver() {
		super.setOrder(400);
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		try {
			JetTemplate template = jetEngine.getTemplate(actionResult.getView());
			return new JetxView(template);
		} catch (ResourceNotFoundException e) {
			return null;
		}
	}
	
}
