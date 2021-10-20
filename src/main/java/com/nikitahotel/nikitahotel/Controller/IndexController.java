package com.nikitahotel.nikitahotel.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nikitahotel.nikitahotel.POJO.Reservation;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;

@Controller
public class IndexController {

	@RequestMapping(value = "/dining", method = RequestMethod.GET)
	public String dinig(Model model) {
		return "dining";
	}

	@RequestMapping(value = "/spa", method = RequestMethod.GET)
	public String spa(Model model) {
		return "spa";
	}

	@RequestMapping(value = "/location", method = RequestMethod.GET)
	public String location(Model model) {
		return "location";
	}

	@RequestMapping(value = "/localattraction", method = RequestMethod.GET)
	public String localattraction(Model model) {
		return "localattraction";
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String menu(Model model) {
		return "menu";
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String email(Model model) {
		return "email-templates/bookingconfirmedTemplate";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHomePage(Model model, HttpServletRequest request) {
		model.addAttribute("search", new SearchRoom());
		model.addAttribute("reservation", new Reservation());
		/*
		 * HttpSession session = request.getSession(true);
		 * session.setAttribute("isUserLoggedIn", false); // User is Logged in
		 */		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void searchRoom1(Model model, HttpServletRequest request) {
		showHomePage(model, request);
	}

}
