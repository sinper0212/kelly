/*
 * Copyright 2002-2012 Zhuo Ying. All rights reserved.
 * Email: yingzhor@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kelly.core;


/**
 * Kelly框架的版本
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
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

	@Override
	public String toString() {
		return name;
	}

	// -----------------------------------------------------------------
	
	public static final class VersionPatternException extends RuntimeException {
		public VersionPatternException(String msg) { super(msg); }
	}

}
