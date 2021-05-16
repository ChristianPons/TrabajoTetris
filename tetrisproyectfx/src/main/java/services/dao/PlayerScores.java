package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayerScores {

	private int playerId;
	private int highscore;
	private int score2;
	private int score3;
	private int score4;
	private int score5;
	
	public PlayerScores(ResultSet result) {
		try {
			
			this.playerId = result.getInt("player_id");
			this.highscore = result.getInt("highscore");
			this.score2 = result.getInt("score2");
			this.score3 = result.getInt("score3");
			this.score4 = result.getInt("score4");
			this.score5 = result.getInt("score5");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
