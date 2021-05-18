package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import javafx.fxml.FXML;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class MainMenuController {

	private BasicData data;
	
	public MainMenuController(BasicData data) {
		this.data = data;
		prepareView();
		
	}

	private void prepareView() {
		try {
			App.setRoot("MainMenu");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void searchMatch() {
		
	}
	
	@FXML
	public void soloPlay() {
		
	}
	
	@FXML
	public void goToOptions() {
		
	}
}
