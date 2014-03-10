package kelly.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 模型类 (对map的简单封装)
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class Model implements Serializable {

	private static final long serialVersionUID = -7214271609779612656L;
	
	public static final Model IMMUTABLE_EMPTY_MODEL = new Model() {
		public Model put(String name, Object value) {
			throw new UnsupportedOperationException("This is a immutable Model's instance");
		}
	};

	private Map<String, Object> map = new HashMap<String, Object>();
	
	public Model() {
		super();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public Model put(String name, Object value) {
		map.put(name, value);
		return this;
	}
	
	public Object get(String name) {
		return map.get(name);
	}
	
	public Object get(String name, Object defaultIfNull) {
		return get(name) == null ? defaultIfNull : get(name);
	}
	
	public Set<String> getNames() {
		return map.keySet();			// immutable set
	}

	public Map<String, Object> asMap() {
		return Collections.unmodifiableMap(map);	// immutable
	}

}
