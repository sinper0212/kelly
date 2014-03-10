package kelly.core.injector;


/**
 * 无动作注入器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class NOPInjector implements Injector {

	public static final Injector INSTANCE = new NOPInjector();
	
	private NOPInjector() {
	}

	// --------------------------------------------------------------------------
	
	@Override
	public void inject(Object bean) {
		// NO OPERATION
	}

}
