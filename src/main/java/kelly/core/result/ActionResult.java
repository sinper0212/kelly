package kelly.core.result;

import java.io.InputStream;

import kelly.core.Model;
import kelly.core.action.Action;

/**
 * InvokableAction的执行结果
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface ActionResult {

	public static enum Type {
		COMMITTED, DOWNLOAD, REDIRECT, FORWARD;
	}

	Type getType();

	Class<?> getObjectClass();

	Object getObject();

	Model getModel();

	String getView();

	Action getAction();

	InputStream getInputStream();

}
