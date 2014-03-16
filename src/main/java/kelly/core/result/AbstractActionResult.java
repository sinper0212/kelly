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
package kelly.core.result;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.argument.ModelHolder;
import kelly.core.model.ImmutableModel;
import kelly.util.StringUtils;
import kelly.util.Validate;

public abstract class AbstractActionResult implements ActionResult {

	protected final Object result;
	protected final Action action;
	protected final HttpServletRequest request;
	protected final HttpServletResponse response;

	public AbstractActionResult(Object result, Action action, HttpServletRequest request, HttpServletResponse response) {
		this.result = Validate.notNull(result);
		this.request = Validate.notNull(request);
		this.response = Validate.notNull(response);
		this.action = Validate.notNull(action);
	}

	@Override
	public Class<?> getObjectClass() {
		return result != null ? result.getClass() : null;
	}

	@Override
	public Object getObject() {
		return result;
	}

	@Override
	public final Action getAction() {
		return action;
	}

	@Override
	public Model getModel() {
		Model model = ModelHolder.getInstance().getModel();
		return model != null ? model : ImmutableModel.INSTANCE;
	}

	@Override
	public String getView() {
		String uri = request.getRequestURI();
		int lastDotIndex = uri.lastIndexOf('.');
		if (-1 != lastDotIndex) {
			uri = uri.substring(0, lastDotIndex);
		}
		return uri;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public Type getType() {
		if (isCommitted()) {
			return Type.COMMITTED;
		} else {
			return startsWithIgnoreCase(getView(), "redirect:") ? Type.REDIRECT : Type.FORWARD;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append("[");
		sb.append("Type: " + getType());
		sb.append("]");
		return sb.toString();
	}

	// -------------------------------------------------------------------------------

	protected final boolean isCommitted() {
		return response.isCommitted();
	}

	@SuppressWarnings("unchecked")
	protected final <T> T getActualResult(Class<T> resultClass) {
		return (T) result;
	}

	// -------------------------------------------------------------------------------

	private boolean startsWithIgnoreCase(String text, String prefix) {
		if (text == null)
			return false;
		return StringUtils.removeWhitespace(text).toLowerCase().startsWith(prefix);
	}

}
