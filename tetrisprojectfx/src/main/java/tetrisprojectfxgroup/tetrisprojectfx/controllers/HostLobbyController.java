package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import gamecore.resources.VariableTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.BasicData;
import services.conector.Conector;
import services.dao.GameLobby;
import services.manager.LobbyManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class HostLobbyController implements Initializable {

	private static final int INTERVAL = 500;

	@FXML
	private Label lobbyId;
	@FXML
	private Label hostName;
	@FXML
	private Label guestName;
	private LobbyManager lobbyManager = new LobbyManager(BasicData.getPlayer().getPlayerId(),
			new Conector().getMySQLConnection());
	private GameLobby lobby;

	private VariableTimer timer = new VariableTimer() {
		@Override
		public void task() {
			try {
				lobby = lobbyManager.findLobby();
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						try {
							lobbyId.setText("ID de la sala: " + lobby.getRoomId());
							hostName.setText("Dueño: " + lobby.getHost().getName());
							guestName.setText("Invitado: " + lobby.getGuest().getName());
						} catch(NullPointerException e) {
							goBack();
						}
					}
				});
			} catch (SQLException e) {
				e.printStackTrace();
				goBack();
			}
		}
	};

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		lobbyManager.joinLobby();
		timer.setInterval(INTERVAL);
	}

	@FXML
	public void startGame() {
		System.out.println("llega");
		lobbyManager.startGame();
			timer.stop();
			BasicData.setFirstPlayer(true);
			BasicData.setOtherPlayerId(lobby.getHost().getPlayerId());
			BasicData.setJoinedLobbyId(lobby.getRoomId());
			System.out.println("sigue");
			try {
				App.setRoot("BoardOnline");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("termina");
	}
	
	@FXML
	public void goBack() {
		timer.stop();
		lobbyManager.leaveLobby();
		try {
			App.setRoot("MultiplayerOptions");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
