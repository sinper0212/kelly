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
