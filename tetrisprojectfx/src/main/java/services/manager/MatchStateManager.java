package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gamecore.logic.Board;
import lombok.Getter;
import services.dao.MatchState;

public class MatchStateManager {

	private int matchId;
	private Connection con;
	@Getter private boolean isPlayer1;
	private int player1Id;
	private int player2Id;

	public MatchStateManager(int matchId, int player1Id, int player2Id, boolean isPlayer1, Connection con) {
		this.matchId = matchId;
		this.con = con;
		this.isPlayer1 = isPlayer1;
		this.player1Id = player1Id;
		this.player2Id = player2Id;
	}

	public MatchState getLastStateFromMatch() {
		List<MatchState> match = getEntireMatch();
		return match.get(!match.isEmpty() ? match.size() - 1 : 0);
	}

	public List<MatchState> getEntireMatch() {
		List<MatchState> states = new ArrayList<>();
		try (PreparedStatement prepstmt = con
				.prepareStatement("SELECT * FROM match_state WHERE match_id = ? ORDER BY state_id")) {
			prepstmt.setInt(1, matchId);
			ResultSet result = prepstmt.executeQuery();
			result.beforeFirst();
			while (result.next()) {
				states.add(new MatchState(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return states;
	}

	public boolean updateState(String jsonBoard, int score) {
		String sql = "";
		MatchState lastState = getLastStateFromMatch();
		sql = "insert into match_state(match_id, state_id, user1_board, user2_board, user1_score, user2_score, user1_haslost, user2_haslost) values("
				+ "?, ?, ?, ?, ?, ?, ?, ?)";
		if (!((lastState.isUser1HasLost() && isPlayer1) || (lastState.isUser2HasLost() && !isPlayer1))) {
			try (PreparedStatement prepstmt = con.prepareStatement(sql)) {
				prepstmt.setInt(1, matchId);
				prepstmt.setInt(2, lastState.getStateId() + 1);
				if (isPlayer1) {
					prepstmt.setString(3, jsonBoard);
					prepstmt.setString(4, lastState.getUser2Board());
					prepstmt.setInt(5, score);
					prepstmt.setInt(6, lastState.getUser2Score());
					prepstmt.setBoolean(7, false);
					prepstmt.setBoolean(8, lastState.isUser2HasLost());
				} else {
					prepstmt.setString(3, lastState.getUser1Board());
					prepstmt.setString(4, jsonBoard);
					prepstmt.setInt(5, lastState.getUser1Score());
					prepstmt.setInt(6, score);
					prepstmt.setBoolean(7, lastState.isUser1HasLost());
					prepstmt.setBoolean(8, false);
				}
				return prepstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	public boolean gameOver(String jsonBoard, int score) {
		String sql = "";
		MatchState lastState = getLastStateFromMatch();
		sql = "insert into match_state(match_id, state_id, user1_board, user2_board, user1_score, user2_score, user1_haslost, user2_haslost) values("
				+ "?, ?, ?, ?, ?, ?, ?, ?)";
		if (!((lastState.isUser1HasLost() && isPlayer1) || (lastState.isUser2HasLost() && !isPlayer1))) {
			try (PreparedStatement prepstmt = con.prepareStatement(sql)) {
				prepstmt.setInt(1, matchId);
				prepstmt.setInt(2, lastState.getStateId() + 1);
				if (isPlayer1) {
					prepstmt.setString(3, jsonBoard);
					prepstmt.setString(4, lastState.getUser2Board());
					prepstmt.setInt(5, score);
					prepstmt.setInt(6, lastState.getUser2Score());
					prepstmt.setBoolean(7, true);
					prepstmt.setBoolean(8, lastState.isUser2HasLost());
				} else {
					prepstmt.setString(3, lastState.getUser1Board());
					prepstmt.setString(4, jsonBoard);
					prepstmt.setInt(5, lastState.getUser1Score());
					prepstmt.setInt(6, score);
					prepstmt.setBoolean(7, lastState.isUser1HasLost());
					prepstmt.setBoolean(8, true);
				}
				if ((lastState.isUser1HasLost() && !isPlayer1) || (lastState.isUser2HasLost() && isPlayer1)) {
					new MatchManager().addMatch(con, matchId, player1Id, lastState.getUser1Score(), player2Id, lastState.getUser2Score());
				}
				return prepstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean createMatch() {
		String sql = "";
		sql = "insert into match_state(match_id, state_id, user1_board, user2_board, user1_score, user2_score) values("
				+ "?, ?, ?, ?, ?, ?)";
		try (PreparedStatement prepstmt = con.prepareStatement(sql)) {
			prepstmt.setInt(1, matchId);
			prepstmt.setInt(2, 1);
			prepstmt.setString(3, new Board().getJSON());
			prepstmt.setString(4, new Board().getJSON());
			prepstmt.setInt(5, 0);
			prepstmt.setInt(6, 0);
			return prepstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
