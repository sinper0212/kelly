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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.annotation.Header;
import kelly.core.annotation.Headers;
import kelly.core.result.ActionResult;
import kelly.util.IOUtils;

public class DownloadView extends AbstractView {

	@Override
	public final String getContentType() {
		return "application/octet-stream";
	}

	@Override
	protected void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		response.setContentLength(actionResult.getInputStream().available());
		
		Headers headers = actionResult.getAction().getMethod().getAnnotation(Headers.class);
		Header header = actionResult.getAction().getMethod().getAnnotation(Header.class);
		
		if (header == null && headers == null) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + actionResult.getView() + "\"");
		}

		ServletOutputStream out = response.getOutputStream();
		IOUtils.copy(actionResult.getInputStream(), out);
		out.flush();
		IOUtils.closeQuietly(out);
		IOUtils.closeQuietly(actionResult.getInputStream());
	}

}
