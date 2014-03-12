package kelly.core;


/**
 * Kelly框架的版本
 * 
 * @author 应卓(yingzhor@gmail.com)
 * @since 1.0.0
 */
public final class Version {

	public static final Version KELLY_1_0_0 = new Version(1, 0, 0);
	public static final Version CURRENT = KELLY_1_0_0;

	private final int x;
	private final int y;
	private final int z;

	private Version(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// --------------------------------------------------------------------------------
	public boolean atLeast(String versionName) {
		if (versionName == null) {
			throw new NullPointerException();
		}
		return this.toString().compareTo(versionName) >= 0;
	}
	
	// --------------------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
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
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public String toString() {
		return String.format("%d.%d.%d", x, y, z);
	}

}
