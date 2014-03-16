package kelly.core.path;

import kelly.core.Predicate;

public class DefaultStaticResourcePredicate implements Predicate<String> {

	private static final String[] ANT_STYLE_PATTERNS = new String[] {
		"/static/**",
		"/statics/**",
		"/icon/**",
		"/icons/**",
		"/css/**",
		"/js/**",
		"/javascript/**",
		"/images/**",
		"/image/**",
		"/swf/**",
		"/flash/**",
		"/**/*.css",
		"/**/*.js",
		"/**/*.ico",
		"/**/*.png",
		"/**/*.gif",
		"/**/*.jpg",
		"/**/*.jpeg",
		"/**/*.bmp"
	};
	
	private static final PathMatcher PATH_MATCHER = new AntStylePathMatcher();
	
	// -----------------------------------------------------------------------------------------

	@Override
	public boolean evaluate(String input) {
		for (String pattern : ANT_STYLE_PATTERNS) {
			if (PATH_MATCHER.match(pattern, input)) {
				return true;
			}
		}
		return false;
	}
}
