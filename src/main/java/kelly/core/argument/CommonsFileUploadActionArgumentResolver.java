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

import java.io.File;
import java.util.List;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.annotation.Multipart;
import kelly.core.castor.Castor;
import kelly.core.dispatcher.WebContextHolder;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CommonsFileUploadActionArgumentResolver implements ActionArgumentResolver {

	private static final File DEFAULT_TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));
	
	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor) {
		return actionArgument.isAnnotatedBy(Multipart.class) && actionArgument.getParameterType() == FileItem[].class;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor) {
		Multipart annotation = actionArgument.getAnnotation(Multipart.class);
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository("".equals(annotation.tempdir()) ? DEFAULT_TEMP_DIR : new File(annotation.tempdir()));
		factory.setSizeThreshold(annotation.sizeThreshold());
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		upload.setSizeMax(annotation.maxSize());
		
		try {
			List<FileItem> items = upload.parseRequest(WebContextHolder.getInstance().getRequest());
			return items.toArray(new FileItem[items.size()]);
		} catch (FileUploadException e) {
			throw new kelly.core.exception.FileUploadException(e);
		}
	}

}
