package kelly.core.argument;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.annotation.DateTimePattern;

/**
 * 提供一些通用方法
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class AbstractActionArgumentResolver implements ActionArgumentResolver {
	
	protected String getDateTimePattern(ActionArgument actionArgument) {
		if (actionArgument != null && actionArgument.isAnnotatedBy(DateTimePattern.class)) {
			return actionArgument.getAnnotation(DateTimePattern.class).value();
		}
		return null;
	}

}
