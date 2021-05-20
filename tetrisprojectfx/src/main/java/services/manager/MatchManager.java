package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	public boolean addMatch(Connection con, int matchId, int player1Id, int player1Points, int player2Id, int player2Points) {
		try (PreparedStatement stmt = con.prepareStatement("INSERT INTO matches VALUES(?, ?, ?, ?, ?, ?, sysdate())")){
			stmt.setInt(1, matchId);
			stmt.setInt(2, player1Id);
			stmt.setInt(3, player1Points);
			stmt.setInt(4, player2Id);
			stmt.setInt(5, player2Points);
			stmt.setInt(6, player1Points > player2Points ? player1Id : player2Id);
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Match findMatchById(Connection con, int matchId) {
		try(PreparedStatement prepstmt = con.prepareStatement("select * from matches where id = ?")){
			prepstmt.setInt(1, matchId);
			ResultSet result = prepstmt.executeQuery();
			if(result.next()) {
				Player player1 = new PlayerManager().getPlayerById(con, result.getInt("user1_id"));
				Player player2 = new PlayerManager().getPlayerById(con, result.getInt("user2_id"));
				Match match = new Match(result);
				match.setPlayer1(player1);
				match.setPlayer2(player2);
				return match;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  This method is used to get a list with all the matches in the table.
	 * @param con Connection to database.
	 * @return a list with the matches.
	 */
	public List<Match> findAllMatches(Connection con){
		List<Match> matches = new ArrayList<>();
		try(PreparedStatement prepstmt = con.prepareStatement("select * from matches")){
			ResultSet result = prepstmt.executeQuery();
			result.beforeFirst();
			while(result.next()) {
				Player player1 = new PlayerManager().getPlayerById(con, result.getInt("user1_id"));
				Player player2 = new PlayerManager().getPlayerById(con, result.getInt("user2_id"));
				Match match = new Match(result);
				match.setPlayer1(player1);
				match.setPlayer2(player2);
				matches.add(match);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matches;
	}

	/**
	 *  This method is used to get the wanted player from a playerList using the player's id.
	 * @param playerId The id of the wanted player.
	 * @param playerList A list with several players.
	 * @return Player class object with the player's data.
	 */
	private Player findPlayer(int playerId, List<Player> playerList) {
		Player playerFound = playerList.stream().filter(player -> player.getPlayerId() == playerId).findFirst().orElse(null);
		return playerFound;
	}
}
