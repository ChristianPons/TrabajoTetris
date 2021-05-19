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

public class GuestLobbyController implements Initializable {

	private static final int INTERVAL = 500;

	@FXML
	private Label lobbyId;
	@FXML
	private Label hostName;
	@FXML
	private Label guestName;
	private GameLobby lobby;
	private LobbyManager lobbyManager = new LobbyManager(BasicData.getJoinedLobbyId(),
			BasicData.getPlayer().getPlayerId(), new Conector().getMySQLConnection());

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
				if (lobbyManager.checkIfStarted()) {

				}
			} catch (SQLException e) {
				e.printStackTrace();
				goBack();
			}
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (lobbyManager.joinLobby()) {
			goBack();
		}
		timer.setInterval(INTERVAL);
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
