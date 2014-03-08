package kelly.core.functor;

/**
 * 谓词
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface Predicate<T> {

	boolean evaluate(T input);

}
