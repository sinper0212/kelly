package kelly.core.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.Addable;
import kelly.core.Aware;
import kelly.core.castor.ConversionService;
import kelly.core.exception.ActionNotFoundException;
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
		super();
	}
	
	// ---------------------------------------------------------------------------------
	
	public boolean supports(ActionArgument actionArgument, HttpServletRequest request) {
		return findActionArgumentResolver(actionArgument) != null;
	}
	
	public Object[] resolve(ActionArgument[] actionArguments, HttpServletRequest request, HttpServletResponse response) {
		if (actionArguments.length == 0) {
			return EMPTY_OBJECT_ARRAY;
		}
		
		List<Object> params = new ArrayList<Object>();
		for (ActionArgument actionArgument : actionArguments) {
			ActionArgumentResolver resolver = findActionArgumentResolver(actionArgument);
			if (resolver == null) {
				if (actionArgument.isNullable()) {
					Class<?> cls = actionArgument.getParameterType();
					params.add(getDefault(cls));
					continue;
				} else {
					throw new ActionNotFoundException("Cannot find resolver for action-argument : " + actionArgument);
				}
			}
			Object object = resolver.resolve(actionArgument, conversionService);
			params.add(object);
		}
		return params.toArray();
	}

	private Object getDefault(Class<?> cls) {
		if (cls == boolean.class) return false;
		if (cls == byte.class) return 0;
		if (cls == short.class) return 0;
		if (cls == char.class) return 0;
		if (cls == int.class) return 0;
		if (cls == long.class) return 0L;
		if (cls == float.class) return 0F;
		if (cls == double.class) return 0D;
		return null;
	}

	@Override
	public void add(Class<? extends ActionArgumentResolver> resolverClass) {
		if (resolverClass != null) {
			ActionArgumentResolver resolver = ReflectionUtils.invokeConstructor(resolverClass);
			add(resolver);
		}
	}

	public void add(ActionArgumentResolver resolver) {
		if (resolver != null) {
			actionArgumentResolverList.add(resolver);
		}
	}
	
	public List<ActionArgumentResolver> getActionArgumentResolvers() {
		return Collections.unmodifiableList(actionArgumentResolverList);		// immutable list
	}

	// ---------------------------------------------------------------------------------

	private ActionArgumentResolver findActionArgumentResolver(ActionArgument actionArgument) {
		if (actionArgument == null) {
			return null;
		}
		for (ActionArgumentResolver resolver : actionArgumentResolverList) {
			if (resolver.supports(actionArgument, conversionService)) {
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
