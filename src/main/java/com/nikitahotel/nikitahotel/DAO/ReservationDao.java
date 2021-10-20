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
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class ReservationDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Map<String, Object> viewReservations(int viewId, String paramType) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("view_reservation")
				.returningResultSet("reservation", new RowMapper<Reservation>() {
					@Override
					public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
						Reservation reservation = new Reservation();
						reservation.setReservationId(rs.getInt("reservation_id"));
						reservation.setCheckin_date(rs.getDate("checkin_date"));
						reservation.setCheckout_date(rs.getDate("checkout_date"));
						reservation.setNo_of_room_reserved(rs.getInt("no_of_room_reserved"));
						reservation.setRoomId(rs.getInt("room_id"));
						String cardNumber = rs.getString("card_number");
						if(!cardNumber.isEmpty() && cardNumber.length()>=4 ) {
							cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());	
						}
						reservation.setCardNumber(cardNumber);

						reservation.setPaymentMethod(rs.getString("payment_method"));
						long diffInMillies = Math.abs(reservation.getCheckout_date().getTime()
								- reservation.getCheckin_date().getTime());
						long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

						reservation.setNoOfDays(diff);

						return reservation;
					}
				}).returningResultSet("userDetails", new RowMapper<UserDetails>() {
					@Override
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserDetails userDetails = new UserDetails();
						userDetails.setFirstname(rs.getString("first_name"));
						userDetails.setLastname(rs.getString("last_name"));
						return userDetails;
					}
				}).returningResultSet("room", new RowMapper<Room>() {
					@Override
					public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
						Room room = new Room();
						room.setRoomId(rs.getInt("room_id"));
						room.setRoomtype(rs.getString("room_type"));
						room.setRoomrate(rs.getDouble("room_rate"));
						return room;
					}
				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();

		inParamMap.put("viewId", viewId);
		inParamMap.put("paramType", paramType);

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

		List<Reservation> reservationroom = (List<Reservation>) simpleJdbcCallResult.get("reservation");
		List<UserDetails> registeruser = (List<UserDetails>) simpleJdbcCallResult.get("userDetails");
		List<Room> rooms = (List<Room>) simpleJdbcCallResult.get("room");

		Map<String, Object> returnMap = new HashMap<String, Object>();

		if (reservationroom.isEmpty()) {
			returnMap.put("reservationDetails", null);
			returnMap.put("userDetails", null);
			returnMap.put("roomDetails", null);
		} else if (paramType.equalsIgnoreCase("view")) {
			returnMap.put("reservationDetails", reservationroom.get(0));
			returnMap.put("userDetails", registeruser.get(0));
			returnMap.put("roomDetails", rooms.get(0));
		} else if (paramType.equalsIgnoreCase("profile")) {
			double totalcost;
			for (int i = 0; i < reservationroom.size(); i++) {
				for (int k = 0; k < rooms.size(); k++) {
					if (reservationroom.get(i).getRoomId() == rooms.get(k).getRoomId()) {
						reservationroom.get(i).setRoomRate(rooms.get(k).getRoomrate());
						totalcost = rooms.get(k).getRoomrate() * reservationroom.get(i).getNo_of_room_reserved() * reservationroom.get(i).getNoOfDays();
						totalcost = totalcost + (totalcost * 0.05);
						reservationroom.get(i).setTotalCost(totalcost);
					}
				}
			}

			returnMap.put("reservationDetails", reservationroom);
			returnMap.put("userDetails", registeruser);
			returnMap.put("roomDetails", rooms);
		}
		return returnMap;
	}

	public Map<String, Object> cancelReservation(int reservationId) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cancel_reservation")
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

						return reservationroom;
					}

				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("reservationId", reservationId);

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

}
