package kelly.core.view;

import java.util.Locale;

import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.ResourceNotFoundException;
import jetbrick.template.web.JetWebEngineLoader;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.result.ActionResult;


public class JetxViewResolver extends AbstractViewResolver {

	public JetxViewResolver() {
		JetWebEngineLoader.setServletContext(WebContextHolder.getInstance().getServletContext());
		super.setOrder(400);
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		String view = actionResult.getView();
		if (view == null) {
			return null;
		}

		JetEngine jetEngine = JetWebEngineLoader.getJetEngine();
		String suffix = jetEngine.getConfig().getTemplateSuffix();
		
		if (view.endsWith(suffix) == false) {
			view += suffix;
		}

		try {
			JetTemplate template = jetEngine.getTemplate(view);
			return new JetxView(template);
		} catch (ResourceNotFoundException e) {
			return null;
		}
	}

}
