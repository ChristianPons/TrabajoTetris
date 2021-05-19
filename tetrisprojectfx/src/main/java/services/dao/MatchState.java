package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import gamecore.logic.Board;
import lombok.Getter;
import lombok.ToString;

@ToString @Getter
public class MatchState {
	private int matchId;
	private int stateId;
	private String user1Board;
	private String user2Board;
	private int user1Score;
	private int user2Score;
	private boolean user1HasLost;
	private boolean user2HasLost;
	
	public MatchState(ResultSet result) {
		try {
			this.matchId = result.getInt("match_id");
			this.stateId = result.getInt("state_id");
			this.user1Board = result.getString("user1_board");
			this.user2Board = result.getString("user2_board");
			this.user1Score = result.getInt("user1_score");
			this.user2Score = result.getInt("user2_score");
			this.user1HasLost = result.getBoolean("user1_haslost");
			this.user2HasLost = result.getBoolean("user2_haslost");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Board getEnemyBoard(boolean isPlayer1) {
		if(isPlayer1) return Board.getFromJSONString(user2Board);
		else return Board.getFromJSONString(user1Board);
	}
	
}
