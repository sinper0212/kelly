package junit.bean;

import kelly.core.Model;
import kelly.core.ModelAndView;
import kelly.core.annotation.Controller;
import kelly.core.annotation.Mapping;
import kelly.core.annotation.Singleton;
import kelly.util.Validate;

@Controller(" ")
@Singleton
public class AController {

	@Mapping(" / shi t")
	public void action1 (String str1, Object obje2) {
		
	}
	
	@Mapping
	public ModelAndView action2(Model model) {
		ModelAndView mav = new ModelAndView();
		Validate.isTrue(mav.getModel() == model);
		return mav;
	}

}
