package kelly.core.castor;

import kelly.util.BooleanUtils;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean convert(String source) {
		return BooleanUtils.toBooleanObject(source);
	}

}
