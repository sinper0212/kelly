package junit.bean;

import java.util.Date;

import kelly.core.annotation.Component;
import kelly.core.castor.Converter;

@Component
public class DConverter implements Converter<Date> {

	@Override
	public Date convert(String source) {
		return null;
	}

}
