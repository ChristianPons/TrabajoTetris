package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class MainMenuController implements Initializable{


	@FXML private Label userName;
	
	
	@FXML
	public void enterMultiplayer() throws IOException {
		App.setRoot("MultiplayerOptions");
	}
	
	@FXML
	public void soloPlay() throws IOException {
		App.setRoot("Board");
	}
	
	@FXML
	public void goToOptions() {
		
	}
	

	public void setPlayer() {
		
		
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		this.userName.setText(BasicData.getPlayer().getUserName());
		
	}

}
