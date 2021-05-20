package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class OptionController {

	@FXML
	public void goToChangePassword() throws IOException {
		App.setRoot("ChangePassword");
	}
	
	@FXML
	public void goToChangeGamertag() throws IOException {
		App.setRoot("ChangeGamertag");
	}
	
	@FXML
	public void logout() throws IOException {
		BasicData.noPlayer(null);
		App.setRoot("Start");
	}
	
	@FXML
	public void goBack() throws IOException {
		App.setRoot("MainMenu");
	}
}
