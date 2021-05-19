package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class MultiplayerOptionsController {

	@FXML TextField idLobby;
	
	@FXML
	public void createLobby() throws IOException {
		App.setRoot("HostLobby");
	}
	
	@FXML
	public void joinLobby() {
		try{
			BasicData.setJoinedLobbyId(Integer.parseInt(idLobby.getText()));
			App.setRoot("GuestLobby");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
