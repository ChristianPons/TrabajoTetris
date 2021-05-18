package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tetrisprojectfxgroup.tetrisprojectfx.App;
import javafx.scene.control.Button;


public class PrimaryController {
	


    @FXML
    private void switchToSecondary(ActionEvent evt) throws IOException {
    	App.setRoot("Login");
        
       
    }
}
