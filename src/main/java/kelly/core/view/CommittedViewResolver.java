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

import kelly.core.result.ActionResult;
import kelly.core.result.ActionResult.Type;

public class CommittedViewResolver extends AbstractViewResolver {

	public CommittedViewResolver() {
		super.setOrder(Integer.MIN_VALUE);
	}

	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		if (actionResult.getType() == Type.COMMITTED) {
			return new CommittedView();
		}
		return null;
	}

}
