package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import services.dao.GameLobby;
import services.dao.Match;

@Getter
public class LobbyManager {

	private int lobbyId = 0;
	private boolean isHost;
	private Connection con;
	private int userId;
	private boolean isInLobby = false;

	/**
	 * Constructor for GUEST - enters the lobby Id to find
	 * 
	 * @param lobbyId - int
	 * @param userId  - int
	 * @param con     - Connection
	 */
	public LobbyManager(int lobbyId, int userId, Connection con) {
		this.lobbyId = lobbyId;
		this.userId = userId;
		this.isHost = false;
		this.con = con;
		leaveLobby();
	}

	/**
	 * Constructor for HOST - it generates a room.
	 * 
	 * @param userId - int
	 * @param con    - Connection
	 */
	public LobbyManager(int userId, Connection con) {
		this.userId = userId;
		this.isHost = true;
		this.con = con;
		leaveLobby();
	}

	public GameLobby findLobby() throws SQLException {
		try (PreparedStatement prepstmt = con.prepareStatement("select * from game_lobby where id=?")) {
			prepstmt.setInt(1, lobbyId);
			ResultSet result = prepstmt.executeQuery();
			if (result.next()) {
				GameLobby lobby = new GameLobby(result);
				int guestId = result.getInt("guest_id");
				lobby.setHost(new PlayerManager().getPlayerById(con, result.getInt("host_id")));
				if (guestId != 0)
					lobby.setGuest(new PlayerManager().getPlayerById(con, guestId));
				else
					lobby.setGuest(null);
				if (guestId == userId)
					isInLobby = true;
				else
					isInLobby = false;
				return lobby;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return null;
	}

	public GameLobby findLobby(int lobbyId) {
		try (PreparedStatement prepstmt = con.prepareStatement("select * from game_lobby where id=?")) {
			prepstmt.setInt(1, lobbyId);
			ResultSet result = prepstmt.executeQuery();
			if (result.next()) {
				GameLobby lobby = new GameLobby(result);
				lobby.setHost(new PlayerManager().getPlayerById(con, result.getInt("host_id")));
				if (result.getInt("guest_id") != 0)
					lobby.setGuest(new PlayerManager().getPlayerById(con, result.getInt("guest_id")));
				else
					lobby.setGuest(null);
				if(lobby != null) isInLobby = true;
				else isInLobby = false;
				
				return lobby;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * For hosts it creates a new lobby. For guests, it makes them join.
	 * 
	 * @return
	 */
	public boolean joinLobby() {
		String sql;
		leaveLobby();
		GameLobby lobby;
		try {
			lobby = findLobby();

			if (isHost) {
				Set<Integer> matchIds = new TreeSet<>();
				List<Match> matches = new MatchManager().findAllMatches(con);
				sql = "insert into game_lobby(host_id, id) values(?, ?)";
				matches.forEach(match -> matchIds.add(match.getMatchId()));
				do {
					lobbyId = (int) (Math.random() * Math.pow(10, 6));
				} while (matchIds.contains(lobbyId) && lobbyId < Math.pow(10, 6));
			} else
				sql = "update game_lobby set guest_id=? where id=?";
			if ((isHost && lobby == null) || (!isHost && lobby != null)) {
				try (PreparedStatement prepstmt = con.prepareStatement(sql)) {
					prepstmt.setInt(1, userId);
					prepstmt.setInt(2, lobbyId);
					return prepstmt.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public boolean leaveLobby() {
		String sql;
		if (isHost)
			sql = "delete from game_lobby where host_id = ?";
		else
			sql = "update game_lobby set guest_id = 1 where guest_id = ?";
		if (isInLobby || isHost) {
			try (PreparedStatement prepstmt = con.prepareStatement(sql)) {
				prepstmt.setInt(1, userId);
				if (prepstmt.execute()) {
					isInLobby = false;
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean checkIfStarted() {
		try (PreparedStatement prepstmt = con.prepareStatement("select has_begun from game_lobby where id = ?")) {
			prepstmt.setInt(1, lobbyId);
			boolean funciona = prepstmt.executeQuery().getBoolean("has_begun");
			System.out.println(funciona);
			return funciona;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean startGame() {
		try {
			if (isHost && findLobby().getGuest().getPlayerId() != 1) {
				try (PreparedStatement prepstmt = con
						.prepareStatement("update game_lobby set has_begun=true where id = ?")) {
					prepstmt.setInt(1, lobbyId);
					return prepstmt.executeUpdate() > 0;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
