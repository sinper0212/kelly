package kelly.core.castor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kelly.core.annotation.DateTimePattern;
import kelly.core.argument.ActionArgumentHolder;
import kelly.core.exception.KellyException;

public class DateConverter implements Converter<Date> {

	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Date convert(String source) {
		DateFormat format;
		DateTimePattern annotation = ActionArgumentHolder.getInstance().getActionArgument().getAnnotation(DateTimePattern.class);
		if (annotation != null) {
			format = new SimpleDateFormat(annotation.value());
		} else {
			format = DEFAULT_DATE_FORMAT;
		}
		
		try {
			return format.parse(source);
		} catch (ParseException e) {
			throw new KellyException(e);
		}
	}

}
