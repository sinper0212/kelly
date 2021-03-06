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
package kelly.util.scan;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class文件分析工具类
 * (内部使用)
 * 
 * @author 陈国强(subchen@gmail.com)
 * @author 应卓(yingzhor@gmail.com)
 *
 */
final class ClassFileReader {

	// Constant Pool type tags
	private static final int CP_UTF8 = 1;
	private static final int CP_INTEGER = 3;
	private static final int CP_FLOAT = 4;
	private static final int CP_LONG = 5;
	private static final int CP_DOUBLE = 6;
	private static final int CP_CLASS = 7;
	private static final int CP_STRING = 8;
	private static final int CP_REF_FIELD = 9;
	private static final int CP_REF_METHOD = 10;
	private static final int CP_REF_INTERFACE = 11;
	private static final int CP_NAME_AND_TYPE = 12;

	// AnnotationElementValue
	private static final int BYTE = 'B';
	private static final int CHAR = 'C';
	private static final int DOUBLE = 'D';
	private static final int FLOAT = 'F';
	private static final int INT = 'I';
	private static final int LONG = 'J';
	private static final int SHORT = 'S';
	private static final int BOOLEAN = 'Z';

	// belows are used for AnnotationElement only
	private static final int STRING = 's';
	private static final int ENUM = 'e';
	private static final int CLASS = 'c';
	private static final int ANNOTATION = '@';
	private static final int ARRAY = '[';

	private Map<String, Class<? extends Annotation>> annotationMap = new HashMap<String, Class<? extends Annotation>>();
	private Object[] constantPool;

	public boolean isAnnotationed(File file) {
		try {
			return isAnnotationed(new FileInputStream(file), file.length());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAnnotationed(ZipFile file, ZipEntry entry) {
		try {
			return isAnnotationed(file.getInputStream(entry), entry.getSize());
		} catch (IOException e) {
			return false;
		}
	}

	public boolean isAnnotationed(InputStream classInputStream) {
		return isAnnotationed(classInputStream, 0);
	}

	private boolean isAnnotationed(InputStream classInputStream, long length) {
		try {
			ClassFileDataInput input = new ClassFileDataInput(classInputStream, length);
			return readClassFile(input);
		} catch (Throwable e) {
			return false;
		} finally {
			try {
				classInputStream.close();
			} catch (IOException e) {
			}
		}
	}

	public void addAnnotation(Class<? extends Annotation> annoClass) {
		annotationMap.put('L' + annoClass.getName().replace('.', '/') + ';', annoClass);
	}

	/**
	 * Inspect the given (Java) class file in streaming mode.
	 */
	private boolean readClassFile(final ClassFileDataInput di) throws IOException {
		if (!readMagicCode(di)) {
			return false;
		}
		if (!readVersion(di)) {
			return false;
		}
		readConstantPoolEntries(di);
		if (!readAccessFlags(di)) {
			return false;
		}
		readThisClass(di);
		readSuperClass(di);
		readInterfaces(di);
		readFields(di);
		readMethods(di);
		return readAttributes(di, ElementType.TYPE);
	}

	private boolean readMagicCode(final ClassFileDataInput di)
			throws IOException {
		return di.size() > 4 && di.readInt() == 0xCAFEBABE;
	}

	private boolean readVersion(final ClassFileDataInput di) throws IOException {
		// sequence: minor version, major version (argument_index is 1-based)
		@SuppressWarnings("unused")
		int minor = di.readUnsignedShort();
		int major = di.readUnsignedShort();
		return major >= 49;
	}

	private void readConstantPoolEntries(final DataInput di) throws IOException {
		final int count = di.readUnsignedShort();
		constantPool = new Object[count];
		for (int i = 1; i < count; ++i) {
			if (readConstantPoolEntry(di, i)) {
				// double slot
				++i;
			}
		}
	}

	/**
	 * Return true if a double slot is read (in case of Double or Long
	 * constant).
	 */
	private boolean readConstantPoolEntry(final DataInput di, final int index)
			throws IOException {
		final int tag = di.readUnsignedByte();
		switch (tag) {
		case CP_UTF8:
			constantPool[index] = di.readUTF();
			return false;
		case CP_INTEGER:
			di.skipBytes(4); // readInt()
			return false;
		case CP_FLOAT:
			di.skipBytes(4); // readFloat()
			return false;
		case CP_LONG:
			di.skipBytes(8); // readLong()
			return true;
		case CP_DOUBLE:
			di.skipBytes(8); // readDouble()
			return true;
		case CP_CLASS:
		case CP_STRING:
			// reference to CP_UTF8 entry. The referenced index can have a
			// higher number!
			constantPool[index] = di.readUnsignedShort();
			return false;
		case CP_REF_FIELD:
		case CP_REF_METHOD:
		case CP_REF_INTERFACE:
		case CP_NAME_AND_TYPE:
			di.skipBytes(4); // readUnsignedShort() * 2
			return false;
		default:
			throw new ClassFormatError(
					"Unknown tag value for constant pool entry: " + tag);
		}
	}

	private boolean readAccessFlags(final DataInput di) throws IOException {
		int flags = di.readUnsignedShort(); // u2
		if (!Modifier.isPublic(flags)) {
			return false;
		}
		if (Modifier.isInterface(flags) || Modifier.isAbstract(flags)) {
			return false;
		}
		return true;
	}

	private void readThisClass(final DataInput di) throws IOException {
		di.skipBytes(2); // u2
	}

	private void readSuperClass(final DataInput di) throws IOException {
		di.skipBytes(2); // u2
	}

	private void readInterfaces(final DataInput di) throws IOException {
		final int count = di.readUnsignedShort();
		di.skipBytes(count * 2); // count * u2
	}

	private void readFields(final DataInput di) throws IOException {
		final int count = di.readUnsignedShort();
		for (int i = 0; i < count; ++i) {
			// AccessFlags(u2), memberName(u2), memberDescriptor(u2)
			di.skipBytes(6);
			readAttributes(di, ElementType.FIELD);
		}
	}

	private void readMethods(final DataInput di) throws IOException {
		final int count = di.readUnsignedShort();
		for (int i = 0; i < count; ++i) {
			// AccessFlags(u2), memberName(u2), memberDescriptor(u2)
			di.skipBytes(6);
			readAttributes(di, ElementType.METHOD);
		}
	}

	private boolean readAttributes(DataInput di, ElementType type)
			throws IOException {
		final int count = di.readUnsignedShort();

		for (int i = 0; i < count; ++i) {
			final String name = resolveUtf8(di);
			// in bytes, use this to skip the attribute info block
			final int length = di.readInt();

			if (type == ElementType.TYPE
					&& ("RuntimeVisibleAnnotations".equals(name) || "RuntimeInvisibleAnnotations"
							.equals(name))) {
				if (readAnnotations(di)) {
					return true;
				}
			} else {
				di.skipBytes(length);
			}
		}
		return false;
	}

	private boolean readAnnotations(DataInput di) throws IOException {
		// the number of Runtime(In)VisibleAnnotations
		final int count = di.readUnsignedShort();

		for (int i = 0; i < count; ++i) {
			String annotation = readAnnotation(di);
			if (annotationMap.containsKey(annotation)) {
				return true;
			}
		}
		return false;
	}

	private String readAnnotation(final DataInput di) throws IOException {
		final String annotation = resolveUtf8(di);
		// num_element_value_pairs
		final int count = di.readUnsignedShort();

		for (int i = 0; i < count; ++i) {
			di.skipBytes(2);
			readAnnotationElementValue(di);
		}
		return annotation;
	}

	private void readAnnotationElementValue(final DataInput di)
			throws IOException {
		final int tag = di.readUnsignedByte();
		switch (tag) {
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case BOOLEAN:
		case STRING:
			di.skipBytes(2);
			break;
		case ENUM:
			di.skipBytes(4); // 2 * u2
			break;
		case CLASS:
			di.skipBytes(2);
			break;
		case ANNOTATION:
			readAnnotation(di);
			break;
		case ARRAY:
			final int count = di.readUnsignedShort();
			for (int i = 0; i < count; ++i) {
				readAnnotationElementValue(di);
			}
			break;
		default:
			throw new ClassFormatError(
					"Not a valid annotation element type tag: 0x"
							+ Integer.toHexString(tag));
		}
	}

	/**
	 * Look up the String value, identified by the u2 index value from constant
	 * pool (direct or indirect).
	 */
	private String resolveUtf8(final DataInput di) throws IOException {
		final int index = di.readUnsignedShort();
		final Object value = constantPool[index];
		final String s;
		if (value instanceof Integer) {
			s = (String) constantPool[(Integer) value];
		} else {
			s = (String) value;
		}
		return s;
	}

	static class ClassFileDataInput implements DataInput {
		private byte[] buffer;
		private int size; // the number of significant bytes read
		private int pointer; // the "read pointer"

		ClassFileDataInput(InputStream is, long length) throws IOException {
			if (length > Integer.MAX_VALUE) {
				throw new RuntimeException("Unsupported file size >= 2GB.");
			}
			if (length > 0) {
				this.buffer = new byte[(int) length];
				while (this.size < length) {
					int n = is.read(buffer, size, buffer.length - size);
					if (n <= 0)
						break;
					this.size += n;
				}
			} else {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					output.write(buffer, 0, n);
				}
				this.buffer = output.toByteArray();
				this.size = this.buffer.length;
			}
			this.pointer = 0;
		}

		/**
		 * Sets the file-pointer offset, measured from the beginning of this
		 * file, at which the next read or write occurs.
		 */
		public void seek(final int position) throws IOException {
			if (position < 0) {
				throw new IllegalArgumentException("position < 0: " + position);
			}
			if (position > size) {
				throw new EOFException();
			}
			this.pointer = position;
		}

		/**
		 * Return the size (in bytes) of this Java ClassFile file.
		 */
		public int size() {
			return size;
		}

		@Override
		public void readFully(final byte[] bytes) throws IOException {
			readFully(bytes, 0, bytes.length);
		}

		@Override
		public void readFully(final byte[] bytes, final int offset,
				final int length) throws IOException {
			if (length < 0 || offset < 0 || offset + length > bytes.length) {
				throw new IndexOutOfBoundsException();
			}
			if (pointer + length > size) {
				throw new EOFException();
			}
			System.arraycopy(buffer, pointer, bytes, offset, length);
			pointer += length;
		}

		@Override
		public int skipBytes(final int n) throws IOException {
			seek(pointer + n);
			return n;
		}

		@Override
		public byte readByte() throws IOException {
			if (pointer >= size) {
				throw new EOFException();
			}
			return buffer[pointer++];
		}

		@Override
		public boolean readBoolean() throws IOException {
			return readByte() != 0;
		}

		@Override
		public int readUnsignedByte() throws IOException {
			if (pointer >= size) {
				throw new EOFException();
			}
			return read();
		}

		@Override
		public int readUnsignedShort() throws IOException {
			if (pointer + 2 > size) {
				throw new EOFException();
			}
			return (read() << 8) + read();
		}

		@Override
		public short readShort() throws IOException {
			return (short) readUnsignedShort();
		}

		@Override
		public char readChar() throws IOException {
			return (char) readUnsignedShort();
		}

		@Override
		public int readInt() throws IOException {
			if (pointer + 4 > size) {
				throw new EOFException();
			}
			// @formatter:off
			return (read() << 24) + (read() << 16) + (read() << 8) + read();
			// @formatter:on
		}

		@Override
		public long readLong() throws IOException {
			if (pointer + 8 > size) {
				throw new EOFException();
			}
			// @formatter:off
			return ((long) read() << 56) + ((long) read() << 48)
					+ ((long) read() << 40) + ((long) read() << 32)
					+ (read() << 24) + (read() << 16) + (read() << 8) + read();
			// @formatter:on
		}

		@Override
		public float readFloat() throws IOException {
			return Float.intBitsToFloat(readInt());
		}

		@Override
		public double readDouble() throws IOException {
			return Double.longBitsToDouble(readLong());
		}

		@Override
		@Deprecated
		public String readLine() throws IOException {
			throw new UnsupportedOperationException(
					"readLine() is deprecated and not supported");
		}

		@Override
		public String readUTF() throws IOException {
			return DataInputStream.readUTF(this);
		}

		private int read() {
			return buffer[pointer++] & 0xff;
		}
	}
}