package kelly.core.castor;

/**
 * 暴露给用户的类型转换器
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @see ConversionService
 */
public interface Castor {

	boolean canConvert(Class<?> targetType);

	<T> T convert(String source, Class<T> targetType);

}
