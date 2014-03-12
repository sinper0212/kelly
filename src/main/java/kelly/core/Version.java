package kelly.core;


/**
 * Kelly框架的版本
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public final class Version 
	implements Comparable<Version>
{

	public static final Version KELLY_1_0_0 = new Version(1, 0, 0);
	public static final Version CURRENT = KELLY_1_0_0;

	private final String name;

	private Version(int x, int y, int z) {
		this.name = ("" + x + "." + y + "." + z);
	}
	
	// --------------------------------------------------------------------------------
	public boolean atLeast(String versionName) {
		if (versionName == null) {
			throw new NullPointerException();
		}
		versionName = versionName.trim();
		if (versionName.matches("\\d+\\.\\d+\\.\\d") == false) {
			throw new VersionPatternException("Name of version must matches \\d+\\.\\d+\\.\\d");
		}
		return this.toString().compareTo(versionName) >= 0;
	}
	
	// --------------------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		return this.name.equals(other.name);
	}

	@Override
	public int compareTo(Version other) {
		return this.name.compareTo(other.name);
	}

	public String toString() {
		return name;
	}

	// -----------------------------------------------------------------
	
	public static final class VersionPatternException extends RuntimeException {
		public VersionPatternException(String msg) { super(msg); }
	}

}
