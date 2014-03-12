package kelly.core.config;

/**
 * 其他开源软件依赖检查工具(内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
class Dependencies {

	private static final String SPRING_CLASS_NAME = "org.springframework.context.ApplicationContext";
	private static final String FASTJSON_CLASS_NAME = "com.alibaba.fastjson.JSON";
	private static final String JETBRICK_TEMPLATE_CLASS_NAME = "jetbrick.template.JetEngine";

	// ------------------------------------------------------------------------------------------------
	
	public static boolean checkFastjson() {
		try {
			getClassLoader().loadClass(FASTJSON_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static boolean checkJetx() {
		try {
			getClassLoader().loadClass(JETBRICK_TEMPLATE_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static boolean checkSpring() {
		try {
			getClassLoader().loadClass(SPRING_CLASS_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
