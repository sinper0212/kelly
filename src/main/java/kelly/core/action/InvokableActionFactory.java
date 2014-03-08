package kelly.core.action;



/**
 * 可执行Action发生装置
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface InvokableActionFactory {

	InvokableAction create(Action action);

}
