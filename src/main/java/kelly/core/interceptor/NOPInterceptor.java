package kelly.core.interceptor;

/**
 * 不做任何动作的拦截器，为避免空指针异常 (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class NOPInterceptor extends AbstractInterceptorAdapter {

	public static final Interceptor INSTANCE = new NOPInterceptor();
	
	private NOPInterceptor() {
		super();
	}

}
