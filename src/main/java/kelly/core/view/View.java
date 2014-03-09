package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;

public interface View {

	public String getContentType();

	public void render(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable;

}
