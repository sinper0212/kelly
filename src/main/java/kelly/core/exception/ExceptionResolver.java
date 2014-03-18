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
package kelly.core.exception;

import java.util.Comparator;
import java.util.Locale;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Aware;
import kelly.core.action.Action;
import kelly.core.result.ActionResult;
import kelly.core.result.CharSequenceActionResult;
import kelly.core.view.View;
import kelly.core.view.ViewResolver;
import kelly.util.ClassUtils;

/**
 * 异常解析器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @since 1.0.0
 * 
 */
public class ExceptionResolver implements Aware<SortedSet<ViewResolver>> {
	
	private static final Comparator<Class<? extends Throwable>> DEFAULT_COMPARATOR = new Comparator<Class<? extends Throwable>>() {
		public int compare(Class<? extends Throwable> o1, Class<? extends Throwable> o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	private SortedSet<ViewResolver> viewSortedSet;
	private final SortedMap<Class<? extends Throwable>, String> sortedMap = new TreeMap<Class<? extends Throwable>, String>(DEFAULT_COMPARATOR);

	@Override
	public void setComponent(SortedSet<ViewResolver> viewSortedSet) {
		this.viewSortedSet = viewSortedSet;
	}

	public void add(Class<? extends Throwable> exType, String view) {
		sortedMap.put(exType, view);
	}
	
	public void resolve(Throwable ex, Action action, HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServletException {
		String viewName = sortedMap.get(ex.getClass());			// 先精确查找类型
		
		// 如果无法精确找到则类型UP
		if (viewName == null) {
			for (Class<? extends Throwable> each : sortedMap.keySet()) {
				if (ClassUtils.isAssignable(ex.getClass(), each)) {
					viewName = sortedMap.get(each);
					break;
				}
			}
		}
		
		if (viewName == null) {
			throw new ServletException(ex);
		}
		
		ActionResult result = new CharSequenceActionResult(viewName, action, request, response);
		View view = null;
		
		for (ViewResolver resolver : viewSortedSet) {
			view = resolver.resolve(result, locale);
			if (view != null) break;
		}
		
		if (view == null) {
			throw new ServletException(ex);
		}

		try {
			view.render(result, request, response, locale);
		} catch (Throwable ex1) {
			throw new ServletException(ex1);
		}
	}

}
