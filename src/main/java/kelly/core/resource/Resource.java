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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 本接口完成对文件或URL的抽象
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see ResourceLoader
 * @see ResourcePatternResolver
 * @see SmartResourceLoader
 * @see ClassPathResource
 * @see FileSystemResource
 * @see UrlResource
 * @see ByteArrayResource
 */
public interface Resource {

	InputStream getInputStream() throws IOException;

	boolean exists();

	boolean isReadable();

	boolean isOpen();

	URL getURL() throws IOException;

	URI getURI() throws IOException;

	File getFile() throws IOException;

	long contentLength() throws IOException;

	long lastModified() throws IOException;

	Resource createRelative(String relativePath) throws IOException;

	String getFilename();

}
