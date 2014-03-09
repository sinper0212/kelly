package kelly.core.result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;

public class FileActionResult extends InputStreamActionResult {

	public FileActionResult(File file, Action action, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		super(new FileInputStream(file), action, request, response);
	}

}
