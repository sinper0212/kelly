package kelly.core.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kelly.core.Aware;
import kelly.core.RequestMethod;
import kelly.core.exception.KellyException;
import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

/**
 * Action查找器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionFinder implements Aware<ActionCollection> {
	
	public static final String EXTRACT_URI_TEMPLATE_VARIABLES_MAP = ActionFinder.class.getName() + "#EXTRACT_URI_TEMPLATE_VARIABLES_MAP";

	private PathMatcher pathMatcher = new AntStylePathMatcher();
	private ActionCollection actionCollection = null;

	// ------------------------------------------------------------------------------

	public ActionFinder() {
		super();
	}
	
	// ------------------------------------------------------------------------------

	public Action find(HttpServletRequest request) {
		if (request == null || actionCollection == null) {
			return null;
		}
		
		List<Action> all = actionCollection.getActionList();
		List<Action> found = new ArrayList<Action>();
		String uri = request.getRequestURI();
		RequestMethod met = null;
		try {
			met = RequestMethod.valueOf(request.getMethod().trim().toUpperCase());
		} catch (Exception e) {
			throw new KellyException("Unsupported request method : " + request.getMethod());
		}
		
		for (Action action : all) {
			String pattern = action.getPattern();
			if (pathMatcher.match(pattern, uri)) {
				if (action.getRequestMethods().contains(met)) {
					found.add(action);
				}
			}
		}
		
		if (found.isEmpty()) {
			return null;
		}
		
		if (found.size() >= 2) {
			throw new KellyException("Found multiple actions");
		}

		Action result = found.get(0);
		
		try {
			return result;
		} finally {
			Map<String, String> map = pathMatcher.extractUriTemplateVariables(result.getPattern(), uri);
			request.setAttribute(EXTRACT_URI_TEMPLATE_VARIABLES_MAP, map);
		}
	}

	@Override
	public void setComponent(ActionCollection actionCollection) {
		this.actionCollection = actionCollection;
	}

}
