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
package kelly.core.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayResource extends AbstractResource {

	private final byte[] byteArray;

	public ByteArrayResource(byte[] byteArray) {
		if (byteArray == null) {
			throw new IllegalArgumentException("Byte array must not be null");
		}
		this.byteArray = byteArray;
	}

	public final byte[] getByteArray() {
		return this.byteArray;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public long contentLength() {
		return this.byteArray.length;
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.byteArray);
	}

}
