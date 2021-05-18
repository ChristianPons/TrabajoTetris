package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class MainMenuController {


	@FXML private Label userName;
	
	
	@FXML
	public void searchMatch() {
		
	}
	
	@FXML
	public void soloPlay() {
		
	}
	
	@FXML
	public void goToOptions() {
		
	}

	public void setPlayer() {
		
		this.userName.setText(BasicData.getPlayer().getUserName());
		
	}

}
