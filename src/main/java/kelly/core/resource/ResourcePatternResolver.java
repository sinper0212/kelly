package kelly.core.resource;

import java.io.IOException;

import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

/**
 * ResourceLoader的加强版本，默认实现为SmartResourceLoader
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 * @see SmartResourceLoader
 * @see PathMatcher
 * @see AntStylePathMatcher
 */
public interface ResourcePatternResolver extends ResourceLoader {

	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	
	ResourcePatternResolver DEFAULT = new SmartResourceLoader();

	Resource[] getResources(String locationPattern) throws IOException;

}
