package services.manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import services.dao.Player;
public class PlayerManager {

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
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	// Quizás sería mejor buscar por el nombre del usuario
	
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
	
	public Player login(Connection con, String userName, String Password) {
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
	
	public void signIn(Connection con, String name, String surnames, String userName, String password, String email, String country) {
		try (PreparedStatement stmt = con.prepareStatement("INSERT INTO players VALUES((SELECT COALESCE(MAX(id),0)) + 1,?,?,?,?,?,?,?,?)")){
			stmt.setString(1, name);
			stmt.setString(2, surnames);
			stmt.setString(3, userName);
			stmt.setString(4, password);
			stmt.setString(5, email);
			stmt.setString(6, country);
			stmt.setDate(7, (java.sql.Date) new Date(System.currentTimeMillis()));
			stmt.setTimestamp(8, null);
			stmt.executeUpdate();

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeUserName(Connection con, String userName, String newUserName) {
		try(PreparedStatement stmt = con.prepareStatement("UPDATE players SET user_name = ? WHERE user_name = ?")) {
			stmt.setString(1, newUserName);
			stmt.setString(2, userName);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
