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
package kelly.util.io;

import java.io.Serializable;
import java.io.Writer;

/**
 * StringBuilderWriter
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class StringBuilderWriter extends Writer implements Serializable {

	private static final long serialVersionUID = 6597804551110334090L;

	private final StringBuilder builder;

	public StringBuilderWriter() {
		this.builder = new StringBuilder();
	}

	public StringBuilderWriter(int capacity) {
		this.builder = new StringBuilder(capacity);
	}

	public StringBuilderWriter(StringBuilder builder) {
		this.builder = builder != null ? builder : new StringBuilder();
	}

	@Override
	public Writer append(char value) {
		builder.append(value);
		return this;
	}

	@Override
	public Writer append(CharSequence value) {
		builder.append(value);
		return this;
	}

	@Override
	public Writer append(CharSequence value, int start, int end) {
		builder.append(value, start, end);
		return this;
	}

	@Override
	public void close() {
	}

	@Override
	public void flush() {
	}

	@Override
	public void write(String value) {
		if (value != null) {
			builder.append(value);
		}
	}

	@Override
	public void write(char[] value, int offset, int length) {
		if (value != null) {
			builder.append(value, offset, length);
		}
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
