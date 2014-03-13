package kelly.core.action;



/**
 * 可执行Action发生装置
 * 
 * @see ActionCollection
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public interface InvokableActionFactory {

	InvokableAction create(Action action);

}
