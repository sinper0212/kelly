package kelly.core.dispatcher;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;
import kelly.core.action.InvokableAction;
import kelly.core.config.JavaBaseConfig;
import kelly.core.exception.KellyException;
import kelly.core.result.ActionResult;
import kelly.core.view.View;
import kelly.core.view.ViewResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kelly框架核心
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public class DispatcherFilter extends AbstractDispatchFilter {

	private static final Logger logger = LoggerFactory.getLogger(DispatcherFilter.class);
	private JavaBaseConfig config = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//TODO 支持用户扩展的JavaBaseConfig
		config = new JavaBaseConfig();
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		String uri = request.getRequestURI();
		
		if (isStaticUri(request)) {
			logger.trace("'{}' is for static resource.", uri);
			chain.doFilter(request, response);
			return;
		}
		
		Action action = config.getActionFinder().find(request);
		if (action == null) {
			logger.debug("Cannot find action for uri: '{}'", uri);
			chain.doFilter(request, response);
			return;
		}
		
		setHeadersIfNecessary(action, response);
		
		Object[] args = config.getActionArgumentResolverCollection()
				.resolve(action.getActionArguments(), request, response);
		
		
		InvokableAction invokableAction = config.getInvokableActionFactory().create(action);
		
		ActionResult actionResult = null;
		try {
			actionResult = config.getActionExecutor().execute(invokableAction, args, request, response);
		} catch (Throwable e) {
			logger.error("action executor failed.", e);
			throw new KellyException(e);
		}
		
		if (actionResult == null) {
			logger.trace("action aborted");
			chain.doFilter(request, response);
			return;
		}
		
		View view = null;
		for (ViewResolver vr : config.getViewResolverSet()) {
			view = vr.resolve(actionResult, request.getLocale());
			if (view != null) {
				break;
			}
		}
		
		if (view == null) {
			throw new KellyException("Cannot find a view to render.");
		}
		try {
			view.render(actionResult, request, response, request.getLocale());
		} catch (Throwable e) {
			throw new KellyException(e);
		}
	}

	private boolean isStaticUri(HttpServletRequest request) {
		return config.getStaticResourcePredicate().evaluate(request.getRequestURI());
	}
	
	private void setHeadersIfNecessary(Action action, HttpServletResponse response) {
		// TODO 查找@Headers/@Header标注
	}

}
