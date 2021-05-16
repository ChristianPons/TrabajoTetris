package services.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Player {

	private int playerId;
	private String name;
	private String surnames;
	private String userName;
	private String email;
	private String country;
	private Date signInDate;

	public Player(ResultSet result) {
		try {
			
			this.playerId = result.getInt("id");
			this.name = result.getString("name");
			this.surnames = result.getString("surnames");
			this.userName = result.getString("user_name");
			this.email = result.getString("email");
			this.country = result.getString("country");
			this.signInDate = result.getDate("sign_in_date");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
