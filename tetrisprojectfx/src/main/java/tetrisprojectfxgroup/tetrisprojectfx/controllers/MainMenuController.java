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
	
	/**
	 * This method is used to go to board window.
	 * @throws IOException
	 */
	
	@FXML
	public void soloPlay() throws IOException {
		App.setRoot("Board");
	}
	
	/**
	 * This method is used to go to options window.
	 */
	@FXML
	public void goToOptions() {
		
	}
	
	/**
	 * This method sets the content of the label userName with the name in-game of the player.
	 */
	
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		this.userName.setText(BasicData.getPlayer().getUserName());
		
	}

}
