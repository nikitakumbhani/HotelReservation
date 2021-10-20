package com.nikitahotel.nikitahotel.Controller;

import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nikitahotel.nikitahotel.DAO.LoginDAO;
import com.nikitahotel.nikitahotel.DAO.ReservationDao;
import com.nikitahotel.nikitahotel.POJO.LoginUser;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class LoginController {
	@Autowired
	LoginDAO loginDao;

	@Autowired
	ReservationDao reservationDao;
	
	@Autowired
	EmailService emailService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request, @RequestParam(value = "id", required = false) String id)
			throws MessagingException {

		HttpSession session = request.getSession(true);
		session.setAttribute("navigationAttribute", id);

		Boolean isUserLoggedIn = (Boolean) session.getAttribute("isUserLoggedIn");
		if (null != isUserLoggedIn && isUserLoggedIn == true) {
			return "redirect:/";
		}
		model.addAttribute("LoginUser", new LoginUser());

		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String verifyLogin(Model model, @ModelAttribute("LoginUser") LoginUser login, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		String navigation = (String) session.getAttribute("navigationAttribute");

		UserDetails userDetails = loginDao.verifyUserLogin(login);
		System.out.println(userDetails);
		if (userDetails != null) {
			session.setAttribute("isUserLoggedIn", true); // User is Logged in
			session.setAttribute("loggedInUserDetails", userDetails);
			if (null == navigation) {
				return "redirect:/";
			} else if (navigation.equalsIgnoreCase("guest")) {
				return "redirect:/reserve";
			}

		} else {
			session.setAttribute("isUserLoggedIn", false);// Invalid ID password, Not logged in
			model.addAttribute("error", 0);
			return "login";
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.setAttribute("isUserLoggedIn", false);
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/myprofile", method = RequestMethod.GET)
	public String myprofile(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		UserDetails userDetails = (UserDetails) session.getAttribute("loggedInUserDetails");
		if (userDetails == null) {
			session.setAttribute("isUserLoggedIn", false);
			session.invalidate();
			return "redirect:/login";
		}

		Map<String, Object> returnMap = reservationDao.viewReservations(userDetails.getUserId(), "profile");
		model.addAttribute("reservationDetails", returnMap.get("reservationDetails"));
		model.addAttribute("registrationDetails", returnMap.get("userDetails"));
		model.addAttribute("roomDetails", returnMap.get("roomDetails"));

		model.addAttribute("loginDetails", userDetails);
		return "myprofile";
	}
	
	@RequestMapping(value = "/myprofile", method = RequestMethod.POST)
	public String myProfileWithCancelReservation(Model model, HttpServletRequest request,
			@RequestParam(value = "reservationId", required = false) int reservationId) throws MessagingException {

		HttpSession session = request.getSession(false);
		UserDetails userDetails = (UserDetails) session.getAttribute("loggedInUserDetails");
		if (userDetails == null) {
			session.setAttribute("isUserLoggedIn", false);
			session.invalidate();
			return "redirect:/login";
		}
		
		// Cancel Reservation
		Map<String, Object> returnMap = reservationDao.cancelReservation(reservationId);
		emailService.sendSimpleMessage(userDetails, returnMap, true);

		returnMap = reservationDao.viewReservations(userDetails.getUserId(), "profile");
		model.addAttribute("reservationDetails", returnMap.get("reservationDetails"));
		model.addAttribute("registrationDetails", returnMap.get("userDetails"));
		model.addAttribute("roomDetails", returnMap.get("roomDetails"));
		model.addAttribute("isReservationCanceled", true);
		model.addAttribute("canceledReservation", reservationId);
		model.addAttribute("loginDetails", userDetails);
		
		
		
		return "myprofile";
	}
}
