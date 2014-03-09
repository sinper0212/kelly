package kelly.core.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.castor.ConversionService;
import kelly.core.exception.KellyException;
import kelly.util.ReflectionUtils;
import kelly.util.Validate;

/**
 * ActionArgumentResolver收集器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class ActionArgumentResolverCollection 
			implements Addable<Class<? extends ActionArgumentResolver>>, Aware<ConversionService>

{
	private final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	
	// ---------------------------------------------------------------------------------
	

	private final List<ActionArgumentResolver> actionArgumentResolverList = new ArrayList<ActionArgumentResolver>();
	private ConversionService conversionService;
	
	// ---------------------------------------------------------------------------------

	public ActionArgumentResolverCollection() {
	}
	
	// ---------------------------------------------------------------------------------
	
	public boolean supports(ActionArgument actionArgument, HttpServletRequest request) {
		return findActionArgumentResolver(actionArgument, request) != null;
	}
	
	public Object[] resolve(ActionArgument[] actionArguments, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		if (actionArguments.length == 0) {
			return EMPTY_OBJECT_ARRAY;
		}
		
		List<Object> params = new ArrayList<Object>();
		for (ActionArgument actionArgument : actionArguments) {
			ActionArgumentResolver resolver = findActionArgumentResolver(actionArgument, request);
			if (resolver == null) {
				if (actionArgument.isNullable()) {
					params.add(null);
					continue;
				} else {
					throw new KellyException("Cannot find resolver for action-argument : " + actionArgument);
				}
			}
			Object object = resolver.resolve(actionArgument, conversionService, request, response);
			params.add(object);
		}
		
		return params.toArray();
	}

	@Override
	public void add(Class<? extends ActionArgumentResolver> actionArgumentResolverClass) {
		if (actionArgumentResolverClass != null) {
			ActionArgumentResolver resolver = ReflectionUtils.invokeConstructor(actionArgumentResolverClass);
			actionArgumentResolverList.add(resolver);
		}
	}
	
	public List<ActionArgumentResolver> getActionArgumentResolvers() {
		return Collections.unmodifiableList(actionArgumentResolverList);		// immutable list
	}
	
	// ---------------------------------------------------------------------------------

	private ActionArgumentResolver findActionArgumentResolver(ActionArgument actionArgument, HttpServletRequest request) {
		if (actionArgument == null) {
			return null;
		}
		for (ActionArgumentResolver resolver : actionArgumentResolverList) {
			if (resolver.supports(actionArgument, request)) {
				return resolver;
			}
		}
		return null;
	}

	@Override
	public void setComponent(ConversionService conversionService) {
		this.conversionService = Validate.notNull(conversionService);
	}
}
