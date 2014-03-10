package kelly.core.view;


public abstract class AbstractViewResolver implements ViewResolver {

	private int order = 0;

	@Override
	public int getOrder() {
		return order;
	}
	
	// -----------------------------------------------------------------------------

	public void setOrder(int order) {
		this.order = order;
	}

}
