package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.BasicData;
import services.conector.Conector;
import services.dao.GameLobby;
import services.manager.LobbyManager;

public class HostLobbyController implements Initializable{
	@FXML
	private Label lobbyId;
	@FXML
	private Label hostName;
	@FXML
	private Label guestName;
	private LobbyManager lobbyManager = new LobbyManager(BasicData.getPlayer().getPlayerId(),
			new Conector().getMySQLConnection());
	private GameLobby lobby = lobbyManager.findLobby();
	private Runnable runnable = new Runnable() {
		
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
	private Thread thread = new Thread(runnable);

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		lobbyManager.joinLobby();
		this.thread.start();
	}
	
	@FXML
	public void startGame() {
		if(lobbyManager.startGame()) thread.stop();
	}

}
