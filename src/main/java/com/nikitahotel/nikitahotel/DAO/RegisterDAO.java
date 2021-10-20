package com.nikitahotel.nikitahotel.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;

import com.nikitahotel.nikitahotel.POJO.Reservation;
import com.nikitahotel.nikitahotel.POJO.Room;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class RegisterDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String registerNewCustomerProcedure(UserDetails reg) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("insertUser");

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("firstname", reg.getFirstname());
		inParamMap.put("lastname", reg.getLastname());
		inParamMap.put("username", reg.getUsername());
		inParamMap.put("passwrd", reg.getPassword());
		inParamMap.put("gender", reg.getGender());
		inParamMap.put("dateofbirth", reg.getDateofbirth());
		inParamMap.put("phone", reg.getPhone());
		inParamMap.put("email", reg.getEmail());
		inParamMap.put("street", reg.getStreet());
		inParamMap.put("city", reg.getCity());
		inParamMap.put("state", reg.getState());
		inParamMap.put("country", reg.getCountry());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
		return "success";
	}

	public Map<String, Object> confirmUserReservation(UserDetails guestInfo, String roomId, SearchRoom searchroom) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("confirm_reservation")
				.returningResultSet("room", new RowMapper<Room>() {
					// Room Details
					// select * from room
					@Override
					public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
						Room room = new Room();
						room.setRoomtype(rs.getString("room_type"));
						room.setRoomarea(rs.getInt("room_area"));
						room.setNoofoccupants(rs.getInt("no_of_occupants"));
						room.setNoofbeds(rs.getInt("no_of_beds"));
						room.setRoomrate(rs.getDouble("room_rate"));

						return room;
					}
				}).returningResultSet("reservation", new RowMapper<Reservation>() {

					@Override
					public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
						Reservation reservationroom = new Reservation();
						reservationroom.setReservationId(rs.getInt("reservation_id"));
						reservationroom.setCheckin_date(rs.getDate("checkin_date"));
						reservationroom.setCheckout_date(rs.getDate("checkout_date"));
						reservationroom.setNo_of_room_reserved(rs.getInt("no_of_room_reserved"));
						reservationroom.setNo_of_adults(rs.getInt("no_of_adults"));
						reservationroom.setNo_of_child(rs.getInt("no_of_child"));
						String cardNumber = rs.getString("card_number");
						cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
						reservationroom.setCardNumber(cardNumber);
						reservationroom.setPaymentMethod(rs.getString("payment_method"));

						long diffInMillies = Math.abs(reservationroom.getCheckout_date().getTime()
								- reservationroom.getCheckin_date().getTime());
						long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

						reservationroom.setNoOfDays(diff);
						return reservationroom;
					}

				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("userId", guestInfo.getUserId());
		inParamMap.put("firstname", guestInfo.getFirstname());
		inParamMap.put("lastname", guestInfo.getLastname());
		inParamMap.put("gender", guestInfo.getGender());

		inParamMap.put("street", guestInfo.getStreet());
		inParamMap.put("city", guestInfo.getCity());
		inParamMap.put("state", guestInfo.getState());
		inParamMap.put("country", guestInfo.getCountry());
		inParamMap.put("phone", guestInfo.getPhone());
		inParamMap.put("email", guestInfo.getEmail());
		inParamMap.put("cardNumber", guestInfo.getCardNumber());
		inParamMap.put("paymentMethod", guestInfo.getPaymentMethod());
		inParamMap.put("rmId", roomId);

		inParamMap.put("arrivalDate", searchroom.getArrivaldate());
		inParamMap.put("departureDate", searchroom.getDeparturedate());
		inParamMap.put("noOfAdults", searchroom.getAdults());
		inParamMap.put("noOfChild", searchroom.getChildren());
		inParamMap.put("noOfRoomReserved", searchroom.getRooms());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

		List<Room> room = (List<Room>) simpleJdbcCallResult.get("room");
		List<Reservation> reserveroom = (List<Reservation>) simpleJdbcCallResult.get("reservation");

		Map<String, Object> returnMap = new HashMap<>();
		// returnMap.put("userDetails", r.get(0));
		returnMap.put("roomDetails", room.get(0));
		returnMap.put("reservationDetails", reserveroom.get(0));
		return returnMap;
	}

	public Room getRoomDetails(String roomId) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRoomDetails")
				.returningResultSet("room", new RowMapper<Room>() {
					// Room Details
					// select * from room
					@Override
					public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
						Room room = new Room();
						room.setRoomrate(rs.getDouble("room_rate"));
						room.setRoomtype(rs.getString("room_type"));
						room.setNoofbeds(rs.getInt("no_of_beds"));
						return room;
					}
				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("rmId", roomId);

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

		List<Room> roomList = (List<Room>) simpleJdbcCallResult.get("room");

		Room room = roomList.get(0);
		return room;
	}

}
