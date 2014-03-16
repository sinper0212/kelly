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
package kelly.core.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kelly.util.ClassLoaderUtils;
import kelly.util.FilenameUtils;
import kelly.util.Validate;

public class ClassPathResource extends AbstractFileResolvingResource {

	private final String path;

	private ClassLoader classLoader;

	private Class<?> clazz;

	public ClassPathResource(String path) {
		this(path, (ClassLoader) null);
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		Validate.notNull(path, "Path must not be null");
		String pathToUse = FilenameUtils.normalize(path, true);
		if (pathToUse.startsWith("/")) {
			pathToUse = pathToUse.substring(1);
		}
		this.path = pathToUse;
		this.classLoader = (classLoader != null ? classLoader : ClassLoaderUtils.getDefaultClassLoader());
	}

	public ClassPathResource(String path, Class<?> clazz) {
		Validate.notNull(path, "Path must not be null");
		this.path = FilenameUtils.normalize(path, true);
		this.clazz = clazz;
	}

	protected ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
		this.path = FilenameUtils.normalize(path, true);
		this.classLoader = classLoader;
		this.clazz = clazz;
	}

	public final String getPath() {
		return this.path;
	}

	public final ClassLoader getClassLoader() {
		return (this.classLoader != null ? this.classLoader : this.clazz.getClassLoader());
	}

	@Override
	public boolean exists() {
		URL url;
		if (this.clazz != null) {
			url = this.clazz.getResource(this.path);
		}
		else {
			url = this.classLoader.getResource(this.path);
		}
		return (url != null);
	}

	public InputStream getInputStream() throws IOException {
		InputStream is;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		}
		else {
			is = this.classLoader.getResourceAsStream(this.path);
		}
		if (is == null) {
			throw new FileNotFoundException("Cannot be opened because it does not exist");
		}
		return is;
	}

	@Override
	public URL getURL() throws IOException {
		URL url;
		if (this.clazz != null) {
			url = this.clazz.getResource(this.path);
		}
		else {
			url = this.classLoader.getResource(this.path);
		}
		if (url == null) {
			throw new FileNotFoundException("Cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	@Override
	public Resource createRelative(String relativePath) {
		String pathToUse = ResourceUtils.applyRelativePath(this.path, relativePath);
		return new ClassPathResource(pathToUse, this.classLoader, this.clazz);
	}

	@Override
	public String getFilename() {
		return ResourceUtils.getFilename(this.path);
	}

}
