package kelly.core;

/**
 * 组件感知
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface Aware<T> {

	public void setComponent(T component);

}
