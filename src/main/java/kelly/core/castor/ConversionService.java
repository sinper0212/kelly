package kelly.core.castor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import kelly.core.Addable;
import kelly.util.ReflectionUtils;
import kelly.util.Validate;

@SuppressWarnings("unchecked")
public final class ConversionService implements Addable<Class<? extends Converter<?>>>, Castor {

	private final String CONVERTER_CLASS_NAME = Converter.class.getName();
	
	private final Map<Class<?>, Converter<?>> cache = new IdentityHashMap<Class<?>, Converter<?>>();
	
	// --------------------------------------------------------------------------------------------

	public ConversionService() {
		super();
	}

	// --------------------------------------------------------------------------------------------
	
	@Override
	public boolean canConvert(Class<?> targetType) {
		return cache.containsKey(targetType);
	}

	@Override
	public <T> T convert(String source, Class<T> targetType) {
		Converter<?> converter = cache.get(targetType);
		return (T) converter.convert(source);
	}
	
	public Set<Class<?>> getTargetTypes() {
		return cache.keySet();			// immutable set
	}
	
	public Set<Map.Entry<Class<?>, Converter<?>>> getConverterPairs() {
		return cache.entrySet();		//  immutable set
	}

	@Override
	public void add(Class<? extends Converter<?>> converterClass) {
		Converter<?> converter = ReflectionUtils.invokeConstructor(converterClass);
		add(converter);
	}
	
	public void add(Converter<?> converter) {
		if (converter != null) {
			Class<?> targetType = getTargetClass(converter);
			cache.put(targetType, converter);
		}
	}
	
	// --------------------------------------------------------------------------------------------

	private Class<?> getTargetClass(Converter<?> converter) {
		Validate.notNull(converter);
		Type[] interfaces = converter.getClass().getGenericInterfaces();
		for (Type interfacs : interfaces) {
			if (interfacs.toString().startsWith(CONVERTER_CLASS_NAME)) {
				if (interfacs instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) interfacs;
					return (Class<?>) parameterizedType.getActualTypeArguments()[0];
				}
			}
		}
		return null;
	}
}
