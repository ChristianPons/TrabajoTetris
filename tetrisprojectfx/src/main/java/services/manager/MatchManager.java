package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import services.dao.Match;
import services.dao.Player;
public class MatchManager {

	/**
	 *  This method searches all the matches where a player has participated.
	 *  It also returns all the data of the players in every match so we can use the names of the players or anything we want 
	 * @param con Connection to the database.
	 * @param playerId The id of the player we are searching.
	 * @return A list of all the matches the player has.
	 */
	public List<Match> getPlayerMatches(Connection con, int playerId){
		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM matches WHERE user1_id = ? OR user2_id = ?")){
			stmt.setInt(1, playerId);
			stmt.setInt(2, playerId);
			ResultSet result = stmt.executeQuery();
			result.beforeFirst();
			
			List<Match> matches = new ArrayList<>();
			Set<Integer>playerIds = new HashSet<>();
			
			while(result.next()) {
				Match match = new Match(result);
				playerIds.add(match.getPlayer1Id());
				playerIds.add(match.getPlayer2Id());
				playerIds.add(match.getWinnerId());
				matches.add(match);
			}
			
			List<Player>playerList = PlayerManager.findAllById(con, playerIds); // Find the players that matches the obtained IDs.
			
			matches.forEach(match ->{
				match.setPlayer1(findPlayer(match.getPlayer1Id(), playerList));
				match.setPlayer2(findPlayer(match.getPlayer2Id(), playerList));
				match.setWinner(findPlayer(match.getWinnerId(), playerList));
			});
			
			return matches;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  This method calls getPlayerMatches and filter the matches to only return those matches were the player won.
	 * @param con Connection to the database.
	 * @param playerId The Id of the player we want to get the matches.
	 * @return returns a list of all the matches the player won. 
	 */
	public List<Match> getMatchesSortedByWinning(Connection con, int playerId){
		return getPlayerMatches(con, playerId).stream().filter(match -> match.getWinnerId() == playerId).collect(Collectors.toList());
	}
	
	/**
	 *  This method calls getPlayerMatches and filter the matches to only return those matches were the player lost.
	 * @param con Connection to the database.
	 * @param playerId The Id of the player we want to get the matches.
	 * @return returns a list of all the matches the player lost. 
	 */
	public List<Match> getMatchesSortedByLossing(Connection con, int playerId){
		return getPlayerMatches(con, playerId).stream().filter(match -> match.getWinnerId() != playerId).collect(Collectors.toList());
	}
	
	public void addMatch(Connection con, int player1Id, int player1Points, int player2Id, int player2Points, int winnerId) {
		try (PreparedStatement stmt = con.prepareStatement("INSERT INTO MATCHES VALUES((SELECT COALESCE(MAX(id),0)) + 1, ?,?,?,?,?,?)")){
			stmt.setInt(1, player1Id);
			stmt.setInt(2, player1Points);
			stmt.setInt(3, player2Id);
			stmt.setInt(4, player2Points);
			stmt.setInt(5, winnerId);
			stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Player findPlayer(int playerId, List<Player> playerList) {
		Player playerFound = playerList.stream().filter(player -> player.getPlayerId() == playerId).findFirst().orElse(null);
		return playerFound;
	}
}
