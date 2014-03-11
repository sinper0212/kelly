package kelly.core;

/**
 * Kelly框架的版本
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public enum Version {

	CURRENT_VERSION("1.0.0"),

	KELLY_1_0_0("1.0.0");

	private String name;

	Version(final String name) {
		this.name = name;
	}

	// -----------------------------------------------------------------------
	public boolean atLeast(String version) {
		if (version == null) {
			throw new IllegalArgumentException("Version must not be null.");
		}
		if (version.matches("\\d+\\.\\d+\\.\\d+") == false) {
			throw new IllegalArgumentException("Version pattern error.");
		}
		return this.name.compareTo(version) >= 0;
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return name;
	}

}
