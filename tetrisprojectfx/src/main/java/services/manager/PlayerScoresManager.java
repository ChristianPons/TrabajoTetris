package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import services.dao.PlayerScores;

/**
 * 
 * @author c.pons.hernandez
 *
 */

public class PlayerScoresManager {
	
	
	/**
	 * 	This method searches the player in the player_score_lists and returns its scores. 
	 * @param con Connection to the database.
	 * @param playerId	the id of the player we want to get the scores from.
	 * @return A PlayerScores class object with its 5 best scores.
	 */
	public PlayerScores getPlayerScores(Connection con, int playerId) {
		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_point_lists WHERE player_id = ?")){
			stmt.setInt(1, playerId);
			ResultSet result = stmt.executeQuery();
			result.first();
			
			return new PlayerScores(result);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  This method updates the scores of a player with a new one.
	 * @param con Connection to the database.
	 * @param playerId the id of the player we are going to update its scores.
	 * @param newScore the new score to add if its greater than any of its scores.
	 */
	public void updatePlayerScores(Connection con, int playerId, int newScore) {
		try (PreparedStatement stmt = con.prepareStatement(
				"UPDATE player_point_lists SET highscore = ?, score2 = ? score3 = ?, score4 = ?, score5 = ?")) {
			
			PlayerScores scores = getPlayerScores(con, playerId);
			
			if(newScore > scores.getHighscore()) {
				stmt.setInt(1, newScore);
				stmt.setInt(2, scores.getHighscore());
				stmt.setInt(3, scores.getScore2());
				stmt.setInt(4, scores.getScore3());
				stmt.setInt(5, scores.getScore4());
				stmt.executeUpdate();
				
			}else if(newScore > scores.getScore2()) {
				stmt.setInt(1, scores.getHighscore());
				stmt.setInt(2, newScore);
				stmt.setInt(3, scores.getScore2());
				stmt.setInt(4, scores.getScore3());
				stmt.setInt(5, scores.getScore4());
				stmt.executeUpdate();
				
			}else if(newScore > scores.getScore3()) {
				stmt.setInt(1, scores.getHighscore());
				stmt.setInt(2, scores.getScore2());
				stmt.setInt(3, newScore);
				stmt.setInt(4, scores.getScore3());
				stmt.setInt(5, scores.getScore4());
				stmt.executeUpdate();
				
			}else if(newScore > scores.getScore4()) {
				stmt.setInt(1, scores.getHighscore());
				stmt.setInt(2, scores.getScore2());
				stmt.setInt(3, scores.getScore3());
				stmt.setInt(4, newScore);
				stmt.setInt(5, scores.getScore4());
				stmt.executeUpdate();
				
			}else if(newScore > scores.getScore5()) {
				stmt.setInt(1, scores.getHighscore());
				stmt.setInt(2, scores.getScore2());
				stmt.setInt(3, scores.getScore3());
				stmt.setInt(4, scores.getScore4());
				stmt.setInt(5, newScore);
				stmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
