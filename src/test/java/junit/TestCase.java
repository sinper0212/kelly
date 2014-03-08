package junit;

import kelly.core.config.JavaConfig;
import kelly.core.path.AntStylePathMatcher;
import kelly.core.path.PathMatcher;

import org.junit.Test;


public class TestCase {

	PathMatcher pm = new AntStylePathMatcher();
	
	@Test
	public void test1() {
		new JavaConfig();
	}

}
