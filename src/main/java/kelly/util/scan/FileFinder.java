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
package kelly.util.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 文件查找器
 * 
 * @author 陈国强(subchen@gmail.com)
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public abstract class FileFinder {
	
	public static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
	public static final String APP_CLASS_LOADER_NAME = "sun.misc.Launcher$AppClassLoader";

	/**
	 * 默认构造方法
	 */
	public FileFinder() {
		super();
	}
	
	/**
	 * 搜索文件系统.
	 */
	public final void lookupFileSystem(File dir, boolean recursive) {
		doLookupInFileSystem(dir, null, null, recursive);
	}
	
	/**
	 * 搜索所有的ClassPath (递归查找子目录/子包)
	 */
	public final void lookupClasspath() {
		lookupClasspath((String[]) null, true);
	}
	
	/**
	 * 搜索ClassPath
	 * 
	 * @param packageNames 指定包名
	 * @param recursive 为true时递归查找子目录/子包
	 */
	public final void lookupClasspath(List<String> packageNames, boolean recursive) {
		if (packageNames == null || packageNames.isEmpty()) {
			lookupClasspath((String[]) null, true);
		}
	}
	
	/**
	 * 搜索ClassPath
	 * 
	 * @param packageNames 指定包名
	 * @param recursive 为true时递归查找子目录/子包
	 */
	public final void lookupClasspath(String[] packageNames, boolean recursive) {
		if (packageNames == null || packageNames.length == 0) {
			Collection<URL> urls = getClasspathURLs(null);
			doGetClasspathResources(urls, null, recursive);
		} else {
			for (String pkg : packageNames) {
				Collection<URL> urls = getClasspathURLs(null, pkg);
				doGetClasspathResources(urls, pkg.replace('.', '/'), recursive);
			}
		}
	}
	
	private void doLookupInFileSystem(File dir, String pkgdir, String relativeName, boolean recursive) {
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
			SystemFileEntry entry = new SystemFileEntry(file, pkgdir, name);
			if (file.isDirectory()) {
				if (visitSystemDirEntry(entry)) {
					if (recursive) {
						doLookupInFileSystem(file, pkgdir, name, true);
					}
				}
			} else {
				visitSystemFileEntry(entry);
			}
		}
	}
	
	private void doGetClasspathResources(Collection<URL> urls, String pkgdir, boolean recursive) {
		for (URL url : urls) {
			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				File file = toFile(url);
				if (file.isDirectory()) {
					doLookupInFileSystem(file, pkgdir, null, recursive);
				} else {
					String name = file.getName().toLowerCase();
					if (name.endsWith(".jar") || name.endsWith(".zip")) {
						doLookupInZipFile(url, pkgdir, recursive);
					}
				}
			} else if ("jar".equals(protocol) || "zip".equals(protocol)) {
				doLookupInZipFile(url, pkgdir, recursive);
			} else {
				throw new IllegalStateException("Unsupported url format: " + url.toString());
			}
		}
	}
	
	private void doLookupInZipFile(URL url, String pkgdir, boolean recursive) {
		ZipFile zip = null;
		try {
			if ("jar".equals(url.getProtocol())) {
				zip = ((JarURLConnection) url.openConnection()).getJarFile();
			} else {
				File file = toFile(url);
				if (!file.exists()) {
					return;
				}
				zip = new ZipFile(file);
			}
			doLookupInZipFile(zip, pkgdir, recursive);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { zip.close(); } catch (IOException e) {}
		}
	}
	
	private void doLookupInZipFile(ZipFile zip, String pkgdir, boolean recursive) {
		if (pkgdir == null || pkgdir.length() == 0) {
			pkgdir = null;
		} else {
			pkgdir = pkgdir + '/';
		}
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			// 获取jar里的一个实体, 可以是目录和一些jar包里的其他文件 如META-INF等文件
			ZipEntry entry = entries.nextElement();
			String entryName = entry.getName();
			if (entry.isDirectory()) {
				entryName = entryName.substring(0, entryName.length() - 1);
			}

			if (pkgdir == null) {
				if (entry.isDirectory()) {
					visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
				} else {
					visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
				}
			} else if (entryName.startsWith(pkgdir)) {
				entryName = entryName.substring(pkgdir.length());
				if (recursive || entryName.indexOf('/') == -1) {
					if (entry.isDirectory()) {
						visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
					} else {
						visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
					}
				}
			}
		}
	}
	
	// 在子类中应该有选择性的覆盖下面的方法
	// ----------------------------------------------------------------
	protected boolean visitSystemDirEntry(SystemFileEntry dir) {
		return visitDirEntry(dir);
	}

	protected void visitSystemFileEntry(SystemFileEntry file) {
		visitFileEntry(file);
	}

	protected void visitZipDirEntry(ZipFileEntry dir) {
		visitDirEntry(dir);
	}

	protected void visitZipFileEntry(ZipFileEntry file) {
		visitFileEntry(file);
	}

	protected boolean visitDirEntry(FileEntry dir) {
		return true;
	}

	protected void visitFileEntry(FileEntry file) {
	}
	
	// -----------------------------------------------------------------------------
	
	private ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable e) {
		}
		if (cl == null) {
			cl = FileFinder.class.getClassLoader();
		}
		return cl;
	}

	private Collection<URL> getClasspathURLs(ClassLoader cl) {
		if (cl == null) {
			cl = getDefaultClassLoader();
		}
		Collection<URL> urls = new HashSet<URL>(32);
		while (cl != null) {
			if (EXT_CLASS_LOADER_NAME.equals(cl.getClass().getName())) {
				break;
			}
			if (cl instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader) cl).getURLs()) {
					urls.add(url);
				}
			}
			cl = cl.getParent();
		}
		return urls;
	}
	
	private Collection<URL> getClasspathURLs(ClassLoader classLoader, String packageName) {
		if (packageName == null) {
			throw new IllegalArgumentException("PackageName must be not null.");
		}
		if (classLoader == null) {
			classLoader = getDefaultClassLoader();
		}
		Collection<URL> urls = new ArrayList<URL>();
		String dirname = packageName.replace('.', '/');
		try {
			Enumeration<URL> dirs = classLoader.getResources(dirname);
			while (dirs.hasMoreElements()) {
				urls.add(dirs.nextElement());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return urls;
	}
	
	private File toFile(URL url) {
		try {
			String file = url.getFile();
			int ipos = file.indexOf("!/");
			if (ipos > 0) {
				file = file.substring(0, ipos);
			}
			String decodeUrl = URLDecoder.decode(file, "utf-8");
			return new File(decodeUrl);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	// inner class
	// ------------------------------------------------------------------------------------

	public static interface FileEntry {
		public boolean isDirectory();

		public boolean isJavaClass();

		public String getName();

		public String getRelativePathName();

		public String getQualifiedJavaName();

		public long length();

		public long lastModified();

		public InputStream getInputStream() throws IOException;
	}

	public static class SystemFileEntry implements FileEntry {
		private final File file;
		private final String pkgdir;
		private final String relativeName;

		public SystemFileEntry(File file, String pkgdir, String relativeName) {
			this.file = file;
			this.pkgdir = pkgdir;
			this.relativeName = relativeName;
		}

		public File getFile() {
			return file;
		}
		public boolean isDirectory() {
			return file.isDirectory();
		}
		public boolean isJavaClass() {
			return !file.isDirectory() && file.getName().endsWith(".class");
		}
		public String getName() {
			return file.getName();
		}
		public String getRelativePathName() {
			return relativeName;
		}
		public String getQualifiedJavaName() {
			String name;
			if (pkgdir != null) {
				name = pkgdir + '/' + relativeName;
			} else {
				name = relativeName;
			}
			if (file.isDirectory()) {
				return name.replace('/', '.');
			} else {
				if (name.endsWith(".class")) {
					return name.substring(0, name.length() - 6).replace('/','.');
				}
				throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
			}
		}
		public long length() {
			return file.length();
		}
		public long lastModified() {
			return file.lastModified();
		}
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(file);
		}
		public String toString() {
			return file.toString();
		}
	}

	public static class ZipFileEntry implements FileEntry {
		private final ZipFile zip;
		private final ZipEntry entry;
		private final String relativeName;

		public ZipFileEntry(ZipFile zip, ZipEntry entry, String relativeName) {
			this.zip = zip;
			this.entry = entry;
			this.relativeName = relativeName;
		}
		public ZipFile getZipFile() {
			return zip;
		}
		public ZipEntry getZipEntry() {
			return entry;
		}
		public boolean isDirectory() {
			return entry.isDirectory();
		}
		public boolean isJavaClass() {
			return entry.getName().endsWith(".class");
		}
		public String getName() {
			int ipos = relativeName.lastIndexOf('/');
			return ipos != -1 ? relativeName.substring(ipos + 1) : relativeName;
		}
		public String getRelativePathName() {
			return relativeName;
		}
		public String getQualifiedJavaName() {
			String name = entry.getName();
			if (entry.isDirectory()) {
				return name.substring(0, name.length() - 1).replace('/', '.');
			} else {
				if (name.endsWith(".class")) {
					return name.substring(0, name.length() - 6).replace('/', '.');
				}
				throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
			}
		}
		public long length() {
			return entry.getSize();
		}
		public long lastModified() {
			return entry.getTime();
		}
		public InputStream getInputStream() throws IOException {
			return zip.getInputStream(entry);
		}
		public String toString() {
			return entry.toString();
		}
	}

}
