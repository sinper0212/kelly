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
