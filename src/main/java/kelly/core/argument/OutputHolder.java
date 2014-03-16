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
package kelly.core.argument;

import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;

public final class OutputHolder {

	private static final ThreadLocal<PrintWriter> PRINT_WRITER_HODER = new ThreadLocal<PrintWriter>();
	private static final ThreadLocal<ServletOutputStream> SERVLET_OUTPUTSTREAM_HOLDER = new ThreadLocal<ServletOutputStream>();
	private static final OutputHolder INSTANCE = new OutputHolder();
	
	public static OutputHolder getInstance() {
		return INSTANCE;
	}
	
	private OutputHolder() {
	}
	
	// ----------------------------------------------------------------------------------------------
	
	void setPrintWriter(PrintWriter out) {
		PRINT_WRITER_HODER.set(out);
	}
	
	void setServletOutputStream(ServletOutputStream out) {
		SERVLET_OUTPUTSTREAM_HOLDER.set(out);
	}
	
	public PrintWriter getPrintWriter() {
		return PRINT_WRITER_HODER.get();
	}
	
	public ServletOutputStream getServletOutputStream() {
		return SERVLET_OUTPUTSTREAM_HOLDER.get();
	}

}
