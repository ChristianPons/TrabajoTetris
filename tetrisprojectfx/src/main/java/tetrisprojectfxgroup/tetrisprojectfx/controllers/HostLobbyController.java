package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.BasicData;
import services.conector.Conector;
import services.dao.GameLobby;
import services.manager.LobbyManager;

public class HostLobbyController {
	@FXML
	private Label lobbyId;
	@FXML
	private Label hostName;
	@FXML
	private Label guestName;
	@FXML private LobbyManager lobbyManager = new LobbyManager(BasicData.getPlayer().getPlayerId(),
			new Conector().getMySQLConnection());
	@FXML GameLobby lobby = lobbyManager.findLobby();
	@FXML private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			
			try {
				while (true) {
					lobby = lobbyManager.findLobby();
					lobbyId.setText("ID de la sala: " + lobby.getRoomId());
					hostName.setText("Dueño: " + lobby.getHost().getName());
					guestName.setText("Invitado: " + lobby.getGuest().getName());
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted");
			}
			
		}
	};
	@FXML private Thread thread = new Thread(runnable);

	@FXML
	public void initialize() {
		lobbyManager.joinLobby();
		this.thread.start();
	}
	
	@FXML
	public void startGame() {
		if(lobbyManager.startGame()) thread.stop();
	}

}
