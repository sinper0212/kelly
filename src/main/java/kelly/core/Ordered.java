package kelly.core;

import java.util.Comparator;

/**
 * 实现本接口的组件是有序的
 * 
 * @author 应卓(yingzhor@gmail.com)
 *
 */
public interface Ordered {

	/**
	 * 默认顺序比较器(数值小在前)
	 */
	Comparator<Ordered> DEFAULT_COMPARATOR = new Comparator<Ordered>() {
		public int compare(Ordered o1, Ordered o2) {
			return (int)((long)o1.getOrder() - (long)o2.getOrder());
		}
	};
	
	/**
	 * 与默认相反的比较器(数值大在前)
	 */
	Comparator<Ordered> REVERSE_COMPARATOR = new Comparator<Ordered>() {
		public int compare(Ordered o1, Ordered o2) {
			return (int)((long)o2.getOrder() - (long)o1.getOrder());
		}
	};
	
	// ------------------------------------------------------------------------------------

	int getOrder();

}
