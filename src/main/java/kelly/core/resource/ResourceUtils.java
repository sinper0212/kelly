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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import kelly.util.StringUtils;
import kelly.util.Validate;

/**
 * Resource相关通用工具
 * 
 * @author 应卓(yingzhor@gmail.com)
 * 
 */
public final class ResourceUtils {

	private ResourceUtils() {
		super();
	}

	// --------------------------------------------------------------

	public static final class UnsupportedPatternException extends
			RuntimeException {
		public UnsupportedPatternException(Throwable cause) {
			super(cause);
		}
	}

	// --------------------------------------------------------------

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable e) {
		}
		if (cl == null) {
			cl = ResourceUtils.class.getClassLoader();
		}
		return cl;
	}
	
	// --------------------------------------------------------------

	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	public static final String FILE_URL_PREFIX = "file:";
	public static final String URL_PROTOCOL_FILE = "file";
	public static final String URL_PROTOCOL_JAR = "jar";
	public static final String URL_PROTOCOL_ZIP = "zip";
	public static final String JAR_URL_SEPARATOR = "!/";

	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}

	public static URL getURL(String resourceLocation) throws IOException {
		Validate.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = getDefaultClassLoader().getResource(path);
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new IOException(description + " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			// try URL
			return new URL(resourceLocation);
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(resourceLocation).toURI().toURL();
			} catch (MalformedURLException ex2) {
				throw new IOException("Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
			}
		}
	}
	
	public static File getFile(String resourceLocation) throws IOException {
		Validate.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			URL url = getDefaultClassLoader().getResource(path);
			if (url == null) {
				throw new IOException(description
						+ " cannot be resolved to absolute file path "
						+ "because it does not reside in the file system");
			}
			return getFile(url, description);
		}
		try {
			return getFile(new URL(resourceLocation));
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}
	
	public static File getFile(URL resourceUrl) throws IOException {
		return getFile(resourceUrl, "URL");
	}

	public static File getFile(URI resourceUri, String description) throws IOException {
		Validate.notNull(resourceUri, "Resource URI must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
			throw new FileNotFoundException(description
					+ " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: "
					+ resourceUri);
		}
		return new File(resourceUri.getSchemeSpecificPart());
	}
	
	public static File getFile(URI resourceUri) throws IOException {
		return getFile(resourceUri, "URI");
	}

	public static File getFile(URL resourceUrl, String description) throws IOException {
		Validate.notNull(resourceUrl, "Resource URL must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(description
					+ " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: "
					+ resourceUrl);
		}
		try {
			return new File(toURI(resourceUrl).getSchemeSpecificPart());
		} catch (URISyntaxException ex) {
			return new File(resourceUrl.getFile());
		}
	}
	
	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtils.replace(location, " ", "%20"));
	}
	
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return URL_PROTOCOL_FILE.equals(protocol);
	}
	
	public static boolean isJarURL(URL url) {
		String up = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(up) || URL_PROTOCOL_ZIP.equals(up));
	}
	
	public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
		if (separatorIndex != -1) {
			String jarFile = urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			} catch (MalformedURLException ex) {
				// Probably no protocol in original jar URL, like
				// "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file system.
				if (!jarFile.startsWith("/")) {
					jarFile = "/" + jarFile;
				}
				return new URL(FILE_URL_PREFIX + jarFile);
			}
		} else {
			return jarUrl;
		}
	}
	
	public static void useCachesIfNecessary(URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}
	
	//~ ------------------------------------------------------------------------------------
	static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf("/");
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith("/")) {
				newPath += "/";
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf("/");
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

}
