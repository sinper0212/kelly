package kelly.core.dispatcher;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.Action;
import kelly.core.action.InvokableAction;
import kelly.core.annotation.ContentType;
import kelly.core.annotation.Header;
import kelly.core.annotation.Headers;
import kelly.core.config.JavaBasedConfig;
import kelly.core.exception.KellyException;
import kelly.core.result.ActionResult;
import kelly.core.view.View;
import kelly.core.view.ViewResolver;
import kelly.util.ClassLoaderUtils;
import kelly.util.ReflectionUtils;

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
	private JavaBasedConfig config = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String className = filterConfig.getInitParameter("kelly.config");
		if (className == null) {
			config = new JavaBasedConfig();
		}
		else {
			Class<?> cls = ClassLoaderUtils.loadClass(className);
			config = (JavaBasedConfig) ReflectionUtils.invokeConstructor(cls);
		}
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
		
		setContentTypeIfNecessary(action, response);
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
		
		logger.trace(actionResult.toString());
		
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
	
	private void setContentTypeIfNecessary(Action action, HttpServletResponse response) {
		ContentType contentType = action.getMethod().getAnnotation(ContentType.class);
		if (contentType != null) {
			response.setContentType(contentType.value());
		}
	}
	
	private void setHeadersIfNecessary(Action action, HttpServletResponse response) {
		Headers headers = action.getMethod().getAnnotation(Headers.class);
		Header header = action.getMethod().getAnnotation(Header.class);
		
		// 用户同时使用了@Headers和@Header
		if (header != null && headers != null) {
			logger.warn("@Header and @Header annotation both exists. @Header will be ignored!");
		}
		
		if (headers != null) {
			for (Header each : headers.value()) {
				response.setHeader(each.headerName(), each.headerValue());
			}
			return; // ignore @Header
		}
		
		if (header != null) {
			response.setHeader(header.headerName(), header.headerValue());
		}
	}

}
