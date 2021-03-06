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

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;
import kelly.core.exception.InputOutputException;
import kelly.util.ClassUtils;

public class ServletOutputStreamActionArgumentResolver extends AbstractActionArgumentResolver {

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		Class<?> cls = actionArgument.getClass();
		return ClassUtils.isAssignable(cls, ServletOutputStream.class);
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		try {
			ServletOutputStream out = WebContextHolder.getInstance().getResponse().getOutputStream();
			OutputHolder.getInstance().setServletOutputStream(out);
			return out;
		} catch (IOException e) {
			throw new InputOutputException(e);
		}
	}

}
