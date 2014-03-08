package kelly.core.argument;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kelly.core.action.ActionArgument;
import kelly.core.action.ActionArgumentResolver;
import kelly.core.annotation.Nullable;
import kelly.core.castor.Castor;
import kelly.core.exception.KellyException;

public class DateResolver implements ActionArgumentResolver {

	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean supports(ActionArgument actionArgument) {
		return actionArgument.getParameterType() == Date.class;
	}

	@Override
	public Object resolve(ActionArgument aa, Castor castor, HttpServletRequest request, HttpServletResponse response) throws KellyException {
		String param = aa.getHttpRequestParameter();
		boolean nullable = aa.isAnnotatedBy(Nullable.class);
		String pattern = aa.getDateTimePattern();
		
		if (param == null) {
			return new Date();
		}
		
		String source = request.getParameter(param);
		if (source == null) {
			if (nullable) {
				return null;
			} else {
				throw new KellyException("Cannnot resolver http request parameter :" + param);
			}
		} else {
			DateFormat df = null;
			if (pattern == null) {
				df = DEFAULT_DATE_FORMAT;
			} else {
				df = new SimpleDateFormat(pattern);
			}
			
			try {
				return df.parseObject(source);
			} catch (ParseException e) {
				throw new KellyException(e);
			}
		}
		
	}

}
