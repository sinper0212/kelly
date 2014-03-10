package junit;

import java.io.IOException;

import kelly.core.config.JavaBasedConfig;
import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;
import kelly.core.resource.Resource;
import kelly.core.resource.SmartResourceLoader;

import org.junit.Test;


public class TestCase {

	PathMatcher pm = new AntStylePathMatcher();
	
	@Test
	public void test1() {
		new JavaBasedConfig();
	}
	
	@Test
	public void test2() throws IOException {
		SmartResourceLoader loader = new SmartResourceLoader();
		Resource[] resources = loader.getResources("classpath*:/**/*.txt");
		
		if (resources != null) {
			for (Resource r : resources) {
				System.out.println(r.getFile().getPath());
			}
		}
	}

}
