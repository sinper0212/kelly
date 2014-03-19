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
package kelly.core.security;


import java.util.Arrays;

import kelly.core.Plugin;
import kelly.core.action.Action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

public class ShiroPlugin implements Plugin {

	public ShiroPlugin() {
	}

	public boolean granted(Action action) {
		return _authentication(action) &&
				_guest(action) &&
				_permission(action) &&
				_role(action) &&
				_user(action)
				;
	}
	
	private boolean _authentication(Action action) {
		RequiresAuthentication annotation = action.getMethod().getAnnotation(RequiresAuthentication.class);
		if (annotation == null) return true;
		return SecurityUtils.getSubject().isAuthenticated();
	}
	
	private boolean _guest(Action action) {
		RequiresGuest annotation = action.getMethod().getAnnotation(RequiresGuest.class);
		if (annotation == null) return true;
		return SecurityUtils.getSubject().getPrincipal() == null;
	}
	
	private boolean _permission(Action action) {
		RequiresPermissions annotation = action.getMethod().getAnnotation(RequiresPermissions.class);
		if (annotation == null) return true;
		Subject subject = SecurityUtils.getSubject();
		boolean[] array = subject.isPermitted(annotation.value());
		return annotation.logical() == Logical.AND ? and(array) : or(array);
	}
	
	private boolean _role(Action action) {
		RequiresRoles annotation = action.getMethod().getAnnotation(RequiresRoles.class);
		if (annotation == null) return true;
		Subject subject = SecurityUtils.getSubject();
		boolean[] array = subject.hasRoles(Arrays.asList(annotation.value()));
		return annotation.logical() == Logical.AND ? and(array) : or(array);
	}
	
	private boolean _user(Action action) {
		RequiresUser annotation = action.getMethod().getAnnotation(RequiresUser.class);
		if (annotation == null) return true;
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated() || subject.isRemembered();
	}
	
	private boolean and(boolean... bs) {
		boolean result = true;
		for (boolean b : bs) {
			result = result && b;
		}
		return result;
	}
	
	private boolean or(boolean... bs) {
		boolean result = false;
		for (boolean b : bs) {
			result = result || b;
		}
		return result;
	}
}
