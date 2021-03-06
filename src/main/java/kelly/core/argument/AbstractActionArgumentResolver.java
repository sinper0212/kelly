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
package kelly.core.argument;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.action.ActionFinder;
import kelly.core.annotation.CookieValue;
import kelly.core.annotation.PathVariable;
import kelly.core.annotation.RequestParam;
import kelly.core.dispatcher.WebContextHolder;

public abstract class AbstractActionArgumentResolver implements ActionArgumentResolver {

	@SuppressWarnings("unchecked")
	protected final String getSource(ActionArgument actionArgument) {
		HttpServletRequest  request  = WebContextHolder.getInstance().getRequest();
		
		if (request == null || actionArgument == null) {
			return null;
		}

		// 支持@RequestParam
		if (actionArgument.isAnnotatedBy(RequestParam.class)) {
			String paramName = actionArgument.getAnnotation(RequestParam.class).value();
			return request.getParameter(paramName);
		}
		
		// 支持@PathVariable
		if (actionArgument.isAnnotatedBy(PathVariable.class)) {
			String templateName = actionArgument.getAnnotation(PathVariable.class).value();
			Map<String, String> uriTemplateVariables = (Map<String, String>) request.getAttribute(ActionFinder.EXTRACT_URI_TEMPLATE_VARIABLES_MAP);
			return uriTemplateVariables != null ? uriTemplateVariables.get(templateName) : null;
		}
		
		// 支持@CookieValue
		if (actionArgument.isAnnotatedBy(CookieValue.class)) {
			String cookieName = actionArgument.getAnnotation(CookieValue.class).value();
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
			return null;
		}

		return null;
	}

}
