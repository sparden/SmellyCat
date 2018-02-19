package mfa.t2;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import mfa.t2.p1.InvoiceService1;

@Controller
class NormalController {

	@Autowired
	private InvoiceService1 service1;

	@RequestMapping
	public void m1() {

		service1.action1();
	}

}
