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
package kelly.core.injector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kelly.core.annotation.Inject;
import kelly.core.annotation.Nullable;
import kelly.core.exception.InjectException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * kelly与spring的整合点
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class AbstractSpringInjector implements Injector {

	private final ApplicationContext applicationContext;
	
	protected abstract ApplicationContext getApplicationContext();

	public AbstractSpringInjector() {
		this.applicationContext = getApplicationContext();
	}
	
	@Override
	public final void inject(Object bean) {
		if (bean == null) return;
		
		Field[] fields = getFieldsIncludingSuperclass(bean);
		
		for (Field field : fields) {
			Inject inject = field.getAnnotation(Inject.class);
			boolean nullable = field.getAnnotation(Nullable.class) != null;
			if (inject == null) {
				continue;
			} else {
				
				String beanName = inject.value();
				Object com = null;
				if ("".equals(beanName)) {		// 按类型注入
					com = getBeanFromApplicationContext(field.getType());
				} else {
					com = getBeanFromApplicationContext(beanName);
				}
				
				if (com == null) {
					if (nullable) {
						continue;
					} else {
						throw new InjectException("Inject failed for field " + field.toString() );
					}
				}
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, bean, com);
			}
		}
	}
	
	// -------------------------------------------------------------------------

	private Field[] getFieldsIncludingSuperclass(Object bean) {
		Class<?> type = bean.getClass();
		List<Field> fields = new ArrayList<Field>();
		while (type != null) {
			fields.addAll(Arrays.asList(type.getDeclaredFields()));
			type = type.getSuperclass();
		}
		return fields.toArray(new Field[fields.size()]);
	}

	private Object getBeanFromApplicationContext(Class<?> beanType) {
		try {
			return applicationContext.getBean(beanType);
		} catch (BeansException e) {
			return null;
		}
	}
	
	private Object getBeanFromApplicationContext(String beanName) {
		try {
			return applicationContext.getBean(beanName);
		} catch (BeansException e) {
			return null;
		}
	}
}
