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
package kelly.core.castor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kelly.core.annotation.DateTimePattern;
import kelly.core.argument.ActionArgumentHolder;
import kelly.core.exception.ConvertException;

public class DateConverter implements Converter<Date> {

	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Date convert(String source) {
		DateFormat format;
		DateTimePattern annotation = ActionArgumentHolder.getInstance().getActionArgument().getAnnotation(DateTimePattern.class);
		if (annotation != null) {
			format = new SimpleDateFormat(annotation.value());
		} else {
			format = DEFAULT_DATE_FORMAT;
		}
		
		try {
			return format.parse(source);
		} catch (ParseException e) {
			throw new ConvertException(e);
		}
	}

}
