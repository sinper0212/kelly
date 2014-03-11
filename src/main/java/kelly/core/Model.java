package kelly.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Model extends Serializable {

	Map<String, Object> asMap();
	
	public boolean isEmpty();
	
	public Model put(String name, Object value);
	
	public Object get(String name);
	
	public Object get(String name, Object defaultIfNull);
	
	public Set<String> getKeys();

}
