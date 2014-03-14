package kelly.core.castor;

import kelly.util.ClassLoaderUtils;

public class ClassConverter implements Converter<Class<?>> {

	@Override
	public Class<?> convert(String source) {
		return ClassLoaderUtils.loadClass(source);
	}

}
