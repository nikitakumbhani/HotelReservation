package com.nikitahotel.nikitahotel.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nikitahotel.nikitahotel.DAO.RegisterDAO;
import com.nikitahotel.nikitahotel.POJO.Reservation;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class RegisterController {

	@Autowired
	RegisterDAO registerDao;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("register", new UserDetails());
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String Gohome(Model model, @ModelAttribute("register") UserDetails userDetails, BindingResult result) {
		registerDao.registerNewCustomerProcedure(userDetails);
		model.addAttribute("search", new SearchRoom());
		model.addAttribute("reservation", new Reservation());
		model.addAttribute("isReservationComplete", true);
		return "index";
	}

}
