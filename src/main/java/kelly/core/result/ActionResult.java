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

import kelly.core.Model;
import kelly.core.action.Action;

/**
 * InvokableAction的执行结果
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface ActionResult {

	public static enum Type {
		COMMITTED, DOWNLOAD, REDIRECT, FORWARD;
	}

	Type getType();

	Class<?> getObjectClass();

	Object getObject();

	Model getModel();

	String getView();

	Action getAction();

	InputStream getInputStream();

	String toString();

}
