package com.nikitahotel.nikitahotel.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.nikitahotel.nikitahotel.POJO.LoginUser;
import com.nikitahotel.nikitahotel.POJO.UserDetails;

@Controller
public class LoginDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public UserDetails verifyUserLogin(LoginUser login) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("verifyLogin")
				.returningResultSet("userDetails", new RowMapper<UserDetails>() {
					@Override
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserDetails userDetails = new UserDetails();
						userDetails.setUserId(rs.getInt("user_id"));
						userDetails.setFirstname(rs.getString("first_name"));
						userDetails.setLastname(rs.getString("last_name"));
						userDetails.setGender(rs.getString("gender"));
						userDetails.setStreet(rs.getString("street"));
						userDetails.setCity(rs.getString("city"));
						userDetails.setState(rs.getString("state"));
						userDetails.setCountry(rs.getString("country"));
						userDetails.setEmail(rs.getString("email"));
						userDetails.setPhone(rs.getString("phone"));
						return userDetails;
					}
				});

		Map<String, Object> inParamMap = new HashMap<String, Object>();

		inParamMap.put("username", login.getUsername());
		inParamMap.put("passwrd", login.getPassword());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
		List<UserDetails> userDetailsList = (List<UserDetails>) simpleJdbcCallResult.get("userDetails");

		UserDetails userDetails = null;
		if (userDetailsList.size() > 0) {
			userDetails = userDetailsList.get(0);
		}
		return userDetails;
	}

}
