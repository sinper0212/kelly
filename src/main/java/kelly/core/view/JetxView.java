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
package kelly.core.view;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetbrick.template.JetTemplate;
import kelly.core.result.ActionResult;
import kelly.util.Validate;

public class JetxView extends AbstractView {

	private final JetTemplate jetTemplate;
	
	public JetxView(JetTemplate jetTemplate) {
		this.jetTemplate = Validate.notNull(jetTemplate);
	}
	
	@Override
	public String getContentType() {
		return "text/html; charset=utf-8";
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		PrintWriter writer = response.getWriter();
		jetTemplate.render(actionResult.getModel().asMap(), writer);
	}

}
