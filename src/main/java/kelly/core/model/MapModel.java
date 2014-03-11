package kelly.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kelly.core.Model;

/**
 * 模型类 (对map的简单封装)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public final class MapModel implements Model {

	private Map<String, Object> map = new HashMap<String, Object>();
	
	public MapModel() {
		super();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public MapModel put(String name, Object value) {
		map.put(name, value);
		return this;
	}
	
	public Object get(String name) {
		return map.get(name);
	}
	
	public Object get(String name, Object defaultIfNull) {
		return get(name) == null ? defaultIfNull : get(name);
	}
	
	public Set<String> getKeys() {
		return map.keySet();			// immutable set
	}

	public Map<String, Object> asMap() {
		return Collections.unmodifiableMap(map);	// immutable
	}

}
