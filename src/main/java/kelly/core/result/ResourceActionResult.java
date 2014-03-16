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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Model;
import kelly.core.action.Action;
import kelly.core.exception.InputOutputException;
import kelly.core.model.ImmutableModel;
import kelly.core.resource.Resource;

public class ResourceActionResult extends AbstractActionResult {

	public ResourceActionResult(Resource resource, Action action, HttpServletRequest request, HttpServletResponse response) {
		super(resource, action, request, response);
	}

	@Override
	public Type getType() {
		return Type.DOWNLOAD;
	}
	
	@Override
	public String getView() {
		try {
			return super.getActualResult(Resource.class).getFile().getName();
		} catch (IOException e) {
			return super.getView();
		}
	}

	@Override
	public Model getModel() {
		return ImmutableModel.INSTANCE;
	}

	@Override
	public InputStream getInputStream() {
		try {
			return super.getActualResult(Resource.class).getInputStream();
		} catch (IOException e) {
			throw new InputOutputException(e);
		}
	}

}
