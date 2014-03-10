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
			int i1 = o1.getOrder();
			int i2 = o2.getOrder();
			if (i1 == i2) return 0;
			else if (i1 < i2) return -1;
			else return 1;
		}
	};
	
	// ------------------------------------------------------------------------------------

	int getOrder();

}
