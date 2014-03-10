package kelly.core.view;

import java.io.File;
import java.util.Locale;

import kelly.core.dispatcher.WebContextHolder;
import kelly.core.result.ActionResult;

public class JspViewResolver extends AbstractViewResolver implements ViewResolver {

	private String prefix = "/WEB-INF/jsp/";
	private String suffix = ".jsp";
	
	public JspViewResolver() {
		super.setOrder(300);
	}
	
	@Override
	public View resolve(ActionResult actionResult, Locale locale) {
		String path = prefix + actionResult.getView() + suffix;
		String realPath = WebContextHolder.getInstance().getRequest().getSession().getServletContext().getRealPath(path); // 不使用Sevlet3.0的API
		if (new File(realPath).exists()) {
			return new JspView(path);
		}
		return null;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
