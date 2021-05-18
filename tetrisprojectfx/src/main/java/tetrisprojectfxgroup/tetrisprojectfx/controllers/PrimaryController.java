package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tetrisprojectfxgroup.tetrisprojectfx.App;


public class PrimaryController {
	


    @FXML
    private void switchToSecondary(ActionEvent evt) throws IOException {
    	App.setRoot("Login");

        
       
    }
}
