package kelly.core.castor;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(String source) {
		return Double.parseDouble(source);
	}

}
