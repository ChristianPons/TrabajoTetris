package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Getter
public class GameLobby {
	private int roomId;
	@Setter private Player host;
	@Setter private Player guest;
	private boolean hasBegun;
	
	public GameLobby(ResultSet result) {
		try {
			roomId = result.getInt("id");
			hasBegun = result.getBoolean("has_begun");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
