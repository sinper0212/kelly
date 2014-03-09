package kelly.core.argument;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class DateResolver extends AbstractActionArgumentResolver {

	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean supports(ActionArgument actionArgument, Castor castor, HttpServletRequest httpServletRequest) {
		return actionArgument.getParameterType() == Date.class;
	}

	@Override
	public Object resolve(ActionArgument actionArgument, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		String source = getSource(request, actionArgument);
		if (source == null) {
			return actionArgument.isNullable() ? null : new Date();
		}
		String pattern = getDateTimePattern(actionArgument);
		
		DateFormat format = pattern == null ? DEFAULT_DATE_FORMAT : new SimpleDateFormat(pattern);
		try {
			return format.parse(source);
		} catch (ParseException e) {
			throw new KellyException(e);
		}
	}

}
