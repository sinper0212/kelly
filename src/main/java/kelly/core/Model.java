package kelly.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Model extends Serializable {

	Map<String, Object> asMap();
	
	boolean isEmpty();
	
	Model put(String name, Object value);
	
	Object get(String name);
	
	Object get(String name, Object defaultIfNull);
	
	Set<String> getKeys();

}
