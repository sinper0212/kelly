package kelly.core.json;

import com.alibaba.fastjson.JSON;

/**
 * 使用Alibaba的fastjson生成json字符串
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class FastjsonJsonFactory implements JsonFactory {

	@Override
	public String create(Object object) throws Throwable {
		return JSON.toJSONString(object);
	}

}
