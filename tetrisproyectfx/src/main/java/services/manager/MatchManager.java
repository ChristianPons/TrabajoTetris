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
			
			List<Player>playerList = PlayerManager.findAllById(con, playerIds);
			
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
	
	public List<Match> getMatchesSortedByWinning(Connection con, int playerId){
		
		return getPlayerMatches(con, playerId).stream().filter(match -> match.getWinnerId() == playerId).collect(Collectors.toList());
	}
	
public List<Match> getMatchesSortedByLossing(Connection con, int playerId){
		
		return getPlayerMatches(con, playerId).stream().filter(match -> match.getWinnerId() != playerId).collect(Collectors.toList());
	}

	private Player findPlayer(int playerId, List<Player> playerList) {
		Player playerFound = playerList.stream().filter(player -> player.getPlayerId() == playerId).findFirst().orElse(null);
		return playerFound;
	}
}
