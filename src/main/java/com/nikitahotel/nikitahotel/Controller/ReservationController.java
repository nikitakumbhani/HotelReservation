package com.nikitahotel.nikitahotel.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import com.nikitahotel.nikitahotel.DAO.RegisterDAO;
import com.nikitahotel.nikitahotel.DAO.ReservationDao;
import com.nikitahotel.nikitahotel.POJO.Reservation;
import com.nikitahotel.nikitahotel.POJO.Room;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class ReservationController {
	@Autowired
	ReservationDao reservationDao;

	@Autowired
	RegisterDAO registerDao;

	@Autowired
	EmailService emailService;

	@RequestMapping(value = "/viewreservation", method = RequestMethod.POST)
	public String viewreservation(Model model, @ModelAttribute("reservation") Reservation reservation) {

		Map<String, Object> returnMap = reservationDao.viewReservations(reservation.getReservationId(), "view");
		model.addAttribute("reservationDetails", returnMap.get("reservationDetails"));
		model.addAttribute("registrationDetails", returnMap.get("userDetails"));
		model.addAttribute("roomDetails", returnMap.get("roomDetails"));

		return "viewreservation";
	}

	@RequestMapping(value = "/reserve", method = RequestMethod.GET)
	public String reservSelected(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String roomId = (String) session.getAttribute("userInterestedRoom");
		UserDetails userDetails = (UserDetails) session.getAttribute("loggedInUserDetails");

		if (null == userDetails) {
			userDetails = new UserDetails();
		}

		if (roomId == null) {
			return "redirect:/";
		}
		SearchRoom searchRequest = (SearchRoom) session.getAttribute("searchRequest");

		Room room = registerDao.getRoomDetails(roomId);
		model.addAttribute("roomDetails", room);
		model.addAttribute("SearchRequestDetails", searchRequest);
		model.addAttribute("guestinfo", userDetails);

		return "guestinfo";
	}

	@RequestMapping(value = "/reserve", method = RequestMethod.POST)
	public String reservSelected(Model model, @RequestParam("roomid") String roomId, HttpServletRequest request)
			throws ParseException {

		HttpSession session = request.getSession(false);
		UserDetails userDetails = (UserDetails) session.getAttribute("loggedInUserDetails");

		if (null == userDetails) {
			userDetails = new UserDetails();
		}
		/*
		 * if(userDetails == null) { session.setAttribute("isUserLoggedIn", false);
		 * session.invalidate(); return "redirect:/login"; }
		 */
		session.setAttribute("userInterestedRoom", roomId);
		SearchRoom searchRequest = (SearchRoom) session.getAttribute("searchRequest");

		String patternFrom = "yyyy-mm-dd";
		SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(patternFrom);

		Date checkinDate = simpleDateFormatFrom.parse(searchRequest.getArrivaldate());
		System.out.println(checkinDate.toString());
		Date checkOutDate = simpleDateFormatFrom.parse(searchRequest.getDeparturedate());

		long diffInMillies = Math.abs(checkOutDate.getTime() - checkinDate.getTime());
		System.out.println(diffInMillies);
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		System.out.println(diff);
		searchRequest.setNoOfDays(diff);

		searchRequest.setNoOfDays(diff);
		Room room = registerDao.getRoomDetails(roomId);
		model.addAttribute("roomDetails", room);
		model.addAttribute("SearchRequestDetails", searchRequest);
		model.addAttribute("guestinfo", userDetails);

		return "guestinfo";
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public String registationConfirmation(Model model, @ModelAttribute("guestinfo") UserDetails guestInfo,
			HttpServletRequest request) throws MessagingException {

		HttpSession session = request.getSession(false);
		String roomId = (String) session.getAttribute("userInterestedRoom");
		SearchRoom searchRequest = (SearchRoom) session.getAttribute("searchRequest");

		UserDetails userDetails = (UserDetails) session.getAttribute("loggedInUserDetails");

		guestInfo.setUserId(userDetails.getUserId());

		Map<String, Object> returnMap = registerDao.confirmUserReservation(guestInfo, roomId, searchRequest);
		model.addAttribute("userDetails", guestInfo);
		model.addAttribute("roomDetails", returnMap.get("roomDetails"));
		model.addAttribute("reservationDetails", returnMap.get("reservationDetails"));

		emailService.sendSimpleMessage(guestInfo, returnMap, false);
		return "reservationconfirmation";
	}
}
