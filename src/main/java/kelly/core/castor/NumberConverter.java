package kelly.core.castor;

import java.text.DecimalFormat;
import java.text.ParseException;

import kelly.core.annotation.NumberPattern;
import kelly.core.argument.ActionArgumentHolder;
import kelly.core.exception.ConvertException;

public class NumberConverter implements Converter<Number> {

	private static final DecimalFormat DEFAULT_NUMBER_FORMAT = new DecimalFormat("###.##");
	
	@Override
	public Number convert(String source) {
		NumberPattern annotation = ActionArgumentHolder.getInstance().getActionArgument().getAnnotation(NumberPattern.class);
		
		DecimalFormat format;
		
		if (annotation != null) {
			format = new DecimalFormat(annotation.value());
		} else {
			format = DEFAULT_NUMBER_FORMAT;
		}
		
		try {
			return format.parse(source);
		} catch (ParseException e) {
			throw new ConvertException(e);
		}
	}

}
