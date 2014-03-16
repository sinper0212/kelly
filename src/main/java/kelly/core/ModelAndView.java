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
package kelly.core;

import java.io.Serializable;

import kelly.core.argument.ModelHolder;
import kelly.core.model.ImmutableModel;
import kelly.core.model.MapModel;

/**
 * 模型与视图
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see Model
 * @see MapModel
 * @see ImmutableModel
 * @see ModelHolder
 */
public final class ModelAndView implements Serializable {

	private final Model model;
	private String viewName;

	public ModelAndView() {
		Model cachedModel = ModelHolder.getInstance().getModel();
		
		if (cachedModel == null) {
			model = new MapModel();
		} else {
			model = cachedModel;
		}
	}

	public ModelAndView(String viewName) {
		this();
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Model getModel() {
		return model;
	}

}
