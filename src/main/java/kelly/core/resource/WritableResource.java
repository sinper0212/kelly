package kelly.core.resource;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 可写的Resource
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface WritableResource extends Resource {

	public boolean isWritable();

	public OutputStream getOutputStream() throws IOException;

}
