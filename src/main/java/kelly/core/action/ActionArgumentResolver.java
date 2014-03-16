package kelly.core.action;

import kelly.core.castor.Castor;

/**
 * 可以由用户注册的Action参数解析器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface ActionArgumentResolver {

	public boolean supports(ActionArgument actionArgument, Castor castor);

	public Object resolve(ActionArgument actionArgument, Castor castor);

}
