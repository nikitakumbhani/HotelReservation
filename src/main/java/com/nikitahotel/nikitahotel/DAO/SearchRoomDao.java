package com.nikitahotel.nikitahotel.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;

import com.nikitahotel.nikitahotel.POJO.Room;
import com.nikitahotel.nikitahotel.POJO.RoomPhoto;
import com.nikitahotel.nikitahotel.POJO.SearchRoom;

@Controller
public class SearchRoomDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public Map<String, Object> searchRoom(SearchRoom sroom) throws ParseException {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("roomsearch")
				.returningResultSet("search", new RowMapper<Room>() {
					@Override
					public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
						Room room = new Room();
						room.setRoomId(rs.getInt("room_id"));
						room.setRoomtype(rs.getString("room_type"));
						room.setRoomarea(rs.getInt("room_area"));
						room.setNoofoccupants(rs.getInt("no_of_occupants"));
						room.setNoofbeds(rs.getInt("no_of_beds"));
						room.setRoomrate(rs.getDouble("room_rate"));
						return room;
					}
				}).returningResultSet("roomphotos", new RowMapper<RoomPhoto>() {
					@Override
					public RoomPhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
						RoomPhoto roomPhotos = new RoomPhoto();
						roomPhotos.setRoomId(rs.getInt("room_id"));
						roomPhotos.setPhotopath(rs.getString("photopath"));
						return roomPhotos;
					}
				}).returningResultSet("extraroom", new RowMapper<Room>() {
					@Override
					public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
						Room room = new Room();
						room.setRoomId(rs.getInt("room_id"));
						room.setRoomtype(rs.getString("room_type"));
						room.setRoomarea(rs.getInt("room_area"));
						room.setNoofoccupants(rs.getInt("no_of_occupants"));
						room.setNoofbeds(rs.getInt("no_of_beds"));
						room.setRoomrate(rs.getDouble("room_rate"));
						return room;
					}
				}).returningResultSet("extraroomphotos", new RowMapper<RoomPhoto>() {
					@Override
					public RoomPhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
						RoomPhoto roomPhotos = new RoomPhoto();
						roomPhotos.setRoomId(rs.getInt("room_id"));
						roomPhotos.setPhotopath(rs.getString("photopath"));
						return roomPhotos;
					}
				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();

		String patternFrom = "MM-dd-yyyy";
		String patternTo = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(patternFrom);
		SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(patternTo);
		sroom.setArrivaldate(simpleDateFormatTo.format(simpleDateFormatFrom.parse(sroom.getArrivaldate())));
		
		sroom.setDeparturedate(simpleDateFormatTo.format(simpleDateFormatFrom.parse(sroom.getDeparturedate())));
		
		inParamMap.put("checkInDate", sroom.getArrivaldate());
		inParamMap.put("checkOutDate", sroom.getDeparturedate());
		inParamMap.put("rooms", sroom.getRooms());
		inParamMap.put("adults", sroom.getAdults());
		inParamMap.put("children", sroom.getChildren());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Map<String, Object> out = simpleJdbcCall.execute(in);

		List<Room> roomsearch = (List<Room>) out.get("search");
		List<RoomPhoto> roomphoto = (List<RoomPhoto>) out.get("roomphotos");
		List<Room> extraroom = (List<Room>) out.get("extraroom");
		List<RoomPhoto> extraroomphoto = (List<RoomPhoto>) out.get("extraroomphotos");

		for (int i = 0; i < roomsearch.size(); i++) {
			List<String> tempPhotoList = new ArrayList<String>();
			for (int j = 0; j < roomphoto.size(); j++) {
				if (roomsearch.get(i).getRoomId() == roomphoto.get(j).getRoomId()) {
					tempPhotoList.add(roomphoto.get(j).getPhotopath());
				}
			}
			roomsearch.get(i).setPhotopath(tempPhotoList);
		}

		for (int i = 0; i < extraroom.size(); i++) {
			List<String> tempPhotoListExtraroom = new ArrayList<String>();
			for (int j = 0; j < extraroomphoto.size(); j++) {
				if (extraroom.get(i).getRoomId() == extraroomphoto.get(j).getRoomId()) {
					tempPhotoListExtraroom.add(extraroomphoto.get(j).getPhotopath());
				}
			}
			extraroom.get(i).setPhotopath(tempPhotoListExtraroom);
		}
		Map<String, Object> returnMap = new HashMap<>();

		// returnMap.put("userDetails", r.get(0));
		returnMap.put("roomDetails", roomsearch);
		returnMap.put("extraRoomDetails", extraroom);

		return returnMap;

	}
}
