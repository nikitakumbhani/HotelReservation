package com.nikitahotel.nikitahotel.Controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nikitahotel.nikitahotel.DAO.SearchRoomDao;
import com.nikitahotel.nikitahotel.POJO.Room;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;

@Controller
public class SearchRoomController {
	@Autowired
	SearchRoomDao searchroomdao;

	@RequestMapping(value = "/SearchRoom", method = RequestMethod.GET)
	public String searchRoomRequest() {
		return "redirect:/index";
	}

	@RequestMapping(value = "/SearchRoom", method = RequestMethod.POST)
	public String searchRoomResponse(Model model, @ModelAttribute("search") SearchRoom searchroom,
			HttpServletRequest request) throws ParseException {

		HttpSession session = request.getSession(true);
		session.setAttribute("searchRequest", searchroom);
		System.out.println(searchroom.toString());

		Map<String, Object> returnMap = searchroomdao.searchRoom(searchroom);

		model.addAttribute("availablerooms", returnMap.get("roomDetails"));
		model.addAttribute("extraavailablerooms", returnMap.get("extraRoomDetails"));

		System.out.println("-----------" + returnMap.get("roomDetails"));
		return "AvailableRooms";
	}

}