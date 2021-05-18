package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tetrisprojectfxgroup.tetrisprojectfx.App;


public class StartController {
	
	public StartController() throws IOException {
	}
	
    @FXML
    private void goToLogin(ActionEvent evt) throws IOException {
    	App.setRoot("Login");

        
       
    }
}
