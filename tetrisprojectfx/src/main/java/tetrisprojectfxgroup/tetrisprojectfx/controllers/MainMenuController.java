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
	public void searchMatch() {
		
	}
	
	@FXML
	public void soloPlay() {
		
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
