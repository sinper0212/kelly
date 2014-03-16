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

import java.util.Comparator;

import kelly.core.view.ViewResolver;


/**
 * 实现本接口的组件是有序的
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
 * @see ViewResolver
 *
 * @since 1.0.0
 */
public interface Ordered {

	/**
	 * 默认顺序比较器(数值小在前)
	 */
	Comparator<Ordered> DEFAULT_COMPARATOR = new Comparator<Ordered>() {
		public int compare(Ordered o1, Ordered o2) {
			int i1 = o1.getOrder();
			int i2 = o2.getOrder();
			if (i1 == i2) return 0;
			else if (i1 < i2) return -1;
			else return 1;
		}
	};
	
	// ------------------------------------------------------------------------------------

	int getOrder();

}
