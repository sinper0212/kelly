package kelly.core.json;

/**
 * 无法生成Json字符串的生成类
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public class NOPJsonFactory implements JsonFactory {

	public static final JsonFactory INSTANCE = new NOPJsonFactory();

	private NOPJsonFactory() {
		super();
	}
	
	@Override
	public String create(Object object) throws Throwable {
		return "";
	}

}
