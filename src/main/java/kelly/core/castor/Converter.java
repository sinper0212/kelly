package kelly.core.castor;

/**
 * 由用户注册的类型转换器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @param <T> 目标类型
 */
public interface Converter<T> {

	T convert(String source);

}
