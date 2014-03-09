package kelly.core.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.result.ActionResult;

import com.alibaba.fastjson.JSON;

public class JsonView extends AbstractView {

	@Override
	public String getContentType() {
		return "application/json; charset=utf-8";
	}

	@Override
	public void doRender(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Throwable {
		Object target = actionResult.getObject() != null ? actionResult.getObject() : actionResult.getModel().asMap();
		String json = JSON.toJSONString(target);
		response.getWriter().print(json);
	}

}
