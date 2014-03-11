package kelly.core.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import kelly.core.Model;

/**
 * 不可变的Model (内部使用)
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public final class ImmutableModel implements Model {

	public static final Model INSTANCE = new ImmutableModel();
	
	private ImmutableModel() {
		super();
	}
	
	@Override
	public Map<String, Object> asMap() {
		return Collections.emptyMap();			// immutable map
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Model put(String name, Object value) {
		throw new UnsupportedOperationException("Cannot put key-value pair to a immutable model.");
	}

	@Override
	public Object get(String name) {
		return null;
	}

	@Override
	public Object get(String name, Object defaultIfNull) {
		return defaultIfNull;
	}

	@Override
	public Set<String> getKeys() {
		return Collections.emptySet();
	}

}
