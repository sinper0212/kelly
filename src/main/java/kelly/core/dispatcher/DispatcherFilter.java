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
import kelly.core.exception.ViewNotFoundException;
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
 * @since 1.0.0
 */
public class DispatcherFilter extends AbstractDispatchFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherFilter.class);
	private JavaBasedConfig config = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebContextHolder.getInstance().setServletContext(filterConfig.getServletContext());

		String className = filterConfig.getInitParameter("kelly.config");
		if (className == null) {
			config = new JavaBasedConfig();
		} else {
			Class<?> cls = ClassLoaderUtils.loadClass(className);
			config = (JavaBasedConfig) ReflectionUtils.invokeConstructor(cls);
		}

		WebContextHolder.getInstance().setConfig(config);
		LOGGER.trace("初始化WebContextHolder实例");
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		String uri = request.getRequestURI();
		
		if (isStaticUri(request)) {
			LOGGER.trace("'{}'被判定为静态资源，不做处理", uri);
			chain.doFilter(request, response);
			return;
		}
		
		Action action = config.getActionFinder().find(request);
		if (action == null) {
			LOGGER.trace("无法找到与'{}'相匹配的Action", uri);
			chain.doFilter(request, response);
			return;
		}
		
		LOGGER.trace("找到与'{}'请求相匹配的Action", uri);
		
		setContentTypeIfNecessary(action, response);
		setHeadersIfNecessary(action, response);

		Object[] args = config.getActionArgumentResolverCollection().resolve(action.getActionArguments(), request, response);
		LOGGER.trace("完成ActionArgument解析");
		
		InvokableAction invokableAction = config.getInvokableActionFactory().create(action);
		LOGGER.trace("生成InvokableAction");
		
		ActionResult actionResult = null;
		try {
			actionResult = config.getActionExecutor().execute(invokableAction, args, request, response);
			LOGGER.trace("InvokableAction执行成功");
		} catch (Throwable e) {
			LOGGER.trace("InvokableAction执行失败");
			LOGGER.info("action executor failed.", e);
			config.getExceptionResolver().resolve(e, invokableAction, request, response, request.getLocale());
			LOGGER.trace("异常解析结束");
			return;
		}
		
		if (actionResult == null) {
			LOGGER.trace("Action终止");
			chain.doFilter(request, response);
			return;
		}
		
		View view = null;
		for (ViewResolver vr : config.getViewResolverSet()) {
			LOGGER.trace("尝试使用{}的实例解析视图", vr.getClass().getName());
			view = vr.resolve(actionResult, request.getLocale());
			if (view != null) {
				break;
			}
		}
		
		if (view == null) {
			LOGGER.trace("无法解析生成视图");
			throw new ViewNotFoundException("Cannot find a view to render.");
		}
		
		LOGGER.trace("使用{}的实例渲染视图", view.getClass().getName());

		try {
			view.render(actionResult, request, response, request.getLocale());
			LOGGER.trace("视图渲染成功");
		} catch (Throwable e) {
			// 视图渲染失败
			LOGGER.trace("视图渲染失败");
			throw new ServletException(e);
		}
	}

	private boolean isStaticUri(HttpServletRequest request) {
		return config.getStaticResourcePredicate().evaluate(request.getRequestURI());
	}
	
	private void setContentTypeIfNecessary(Action action, HttpServletResponse response) {
		ContentType contentType = action.getMethod().getAnnotation(ContentType.class);
		if (contentType != null) {
			LOGGER.trace("发现@ContentType标注，设置应答内容{}", contentType.value());
			response.setContentType(contentType.value());
		}
	}
	
	private void setHeadersIfNecessary(Action action, HttpServletResponse response) {
		Headers headers = action.getMethod().getAnnotation(Headers.class);
		Header header = action.getMethod().getAnnotation(Header.class);
		
		// 用户同时使用了@Headers和@Header
		if (header != null && headers != null) {
			LOGGER.warn("@Header and @Header annotation both exists. @Header will be ignored!");
		}
		
		if (headers != null) {
			for (Header each : headers.value()) {
				LOGGER.trace("发现@Header标注，设置应答头 {}={}", each.headerName(), each.headerValue());
				response.setHeader(each.headerName(), each.headerValue());
			}
			return; // ignore @Header
		}
		
		if (header != null) {
			LOGGER.trace("发现@Header标注，设置应答头 {}={}", header.headerName(), header.headerValue());
			response.setHeader(header.headerName(), header.headerValue());
			return;
		}
	}

}
