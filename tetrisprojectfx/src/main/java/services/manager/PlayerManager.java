package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import services.dao.Player;

/**
 * 
 * @author c.pons.hernandez
 *
 */

public class PlayerManager {

	/**
	 *  This method searches and returns all the players in the table players.
	 * @param con Connection to database.
	 * @return A list of all the current players.
	 */
	public List<Player> getAllPlayers(Connection con){
		try(Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery("SELECT * FROM players");
			result.beforeFirst();
			
			List<Player>players = new ArrayList<>();
			
			while(result.next()) {
				players.add(new Player(result));
			}
			
			return players;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Quizás sería mejor buscar por el nombre del usuario
	/**
	 *  This method gets all the data of a player from the table players.
	 * @param con Connection to the database.
	 * @param playerId The id of the player we want to get the data from.
	 * @return A Player class object.
	 */
	public Player getPlayerById(Connection con, int playerId) {
		try(PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE id = ?")) {
			stmt.setInt(1, playerId);
			ResultSet result = stmt.executeQuery();
			result.first();
			
			return new Player(result);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  This method is only used by the method getPlayerMatches from the MatchManager to obtain a list of players. 
	 * @param con Connection to the database.
	 * @param playerIds A list of IDs to search in the table players.
	 * @return A list of players that matches the playerIds' IDs. 
	 */
	public static List<Player> findAllById(Connection con, Set<Integer> playerIds) {
		String sql = String.format("SELECT * FROM players WHERE Code in (%s)",
				playerIds.stream().map(data -> "\"" + data + "\"").collect(Collectors.joining(", ")));
		try(Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			result.beforeFirst();
			
			List<Player>playerList = new ArrayList<>();
			
			while(result.next()) {
				playerList.add(new Player(result));
			}
			
			return playerList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 *  This method is used to log in the game. If the player is not in the players table, it won't have access to the program.
	 * @param con Connection to database.
	 * @param userName The name in-game of the player.
	 * @param Password The password of the player to log-in.
	 * @return	<ul>
	 * 				<li>If the player exists the method returns a Player class object with its data</li>
	 * 				<li>If the player does not exist the method will return null</li>
	 * 			</ul>
	 */
	public static  Player login(Connection con, String userName, String Password) {
		try(PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE user_name LIKE ? AND password LIKE ?")) {
			stmt.setString(1, userName);
			stmt.setString(2, Password);
			ResultSet result = stmt.executeQuery();
			result.first();
			
			return new Player(result);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  This method is used to add a new player to the database.
	 * @param con Connection to database.
	 * @param name The name of the new player.
	 * @param surnames The surnames of the new player.
	 * @param userName The in-game name the new player wants.
	 * @param password The password the new player wants to log-in in the game.
	 * @param email The email of the new player.
	 * @param country The country of origin of the new player.
	 */
	public static void signIn(Connection con, String name, String surnames, String userName, String password, String email, String country) {
		try (PreparedStatement stmt = con.prepareStatement("INSERT INTO players VALUES((SELECT COALESCE(MAX(a.id),0) FROM players a) + 1,?,?,?,?,?,?,?,?)")){
			stmt.setString(1, name);
			stmt.setString(2, surnames);
			stmt.setString(3, userName);
			stmt.setString(4, password);
			stmt.setString(5, email);
			stmt.setString(6, country);
			stmt.setDate(7, new Date(System.currentTimeMillis()));
			stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			stmt.executeUpdate();

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  This method is used to change the in-game name of a player.
	 * @param con Connection to database.
	 * @param userName the in-game name of the player.
	 * @param newUserName The new name the player wants.
	 */
	public void changeUserName(Connection con, String userName, String newUserName) {
		try(PreparedStatement stmt = con.prepareStatement("UPDATE players SET user_name = ? WHERE user_name = ?")) {
			stmt.setString(1, newUserName);
			stmt.setString(2, userName);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  This method is used to change the password of a player.
	 * @param con Connection to database.
	 * @param userName password of the player.
	 * @param newUserName The new password the player wants.
	 */
	public void changePassword(Connection con, String userName, String newPassword) {
		try(PreparedStatement stmt = con.prepareStatement("UPDATE players SET password = ? WHERE user_name = ?")) {
			stmt.setString(1, newPassword);
			stmt.setString(2, userName);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
