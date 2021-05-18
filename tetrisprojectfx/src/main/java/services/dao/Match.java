package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Match {

	private int matchId;
	private Player player1;
	private int player1Id;
	private int player1Points;
	private Player player2;
	private int player2Id;
	private int player2Points;
	private Player winner;
	private int winnerId;
	private Timestamp matchDate;
	
	public Match(ResultSet result) {
		try {
			
			this.matchId = result.getInt("id");
			this.player1Id = result.getInt("user1_id");
			this.player1Points = result.getInt("user1_points");
			this.player2Id = result.getInt("user2_id");
			this.player2Points = result.getInt("user2_points");
			this.winnerId = result.getInt("winner");
			this.matchDate = result.getTimestamp("match_date");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
