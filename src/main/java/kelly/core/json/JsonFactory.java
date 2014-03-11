package kelly.core.json;

import kelly.core.Plugin;

/**
 * Json生成组件
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface JsonFactory extends Plugin {

	public String create(Object object) throws Throwable;

}
