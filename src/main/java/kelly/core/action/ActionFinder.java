/*
 * Copyright 2002-2012 Zhuo Ying. All rights reserved.
 * Email: yingzhor@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kelly.core.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kelly.core.Aware;
import kelly.core.RequestMethod;
import kelly.core.exception.MultipleActionFoundException;
import kelly.core.exception.UnsupportedRequestMethodException;
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
		
		int lastDotIndex = uri.lastIndexOf('.');
		if (-1 != lastDotIndex) {
			uri = uri.substring(0, lastDotIndex);
		}
		
		RequestMethod met = null;
		try {
			met = RequestMethod.valueOf(request.getMethod().trim().toUpperCase());
		} catch (Exception e) {
			throw new UnsupportedRequestMethodException("Unsupported request method : " + request.getMethod());
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
			throw new MultipleActionFoundException("Found multiple actions");
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
