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
