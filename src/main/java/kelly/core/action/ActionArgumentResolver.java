package kelly.core.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

/**
 * 可以由用户注册的Action参数解析器
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface ActionArgumentResolver {

	public boolean supports(ActionArgument actionArgument);

	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException;

}
