package kelly.core.castor;

public class FloatConverter implements Converter<Float> {

	@Override
	public Float convert(String source) {
		return Float.parseFloat(source);
	}

}
