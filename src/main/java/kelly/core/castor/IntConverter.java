package kelly.core.castor;

public class IntConverter implements Converter<Integer> {

	@Override
	public Integer convert(String source) {
		return Integer.parseInt(source);
	}

}
