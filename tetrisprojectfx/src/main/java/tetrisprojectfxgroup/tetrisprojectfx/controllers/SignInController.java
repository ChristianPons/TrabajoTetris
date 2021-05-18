package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.conector.Conector;

public class SignInController {

	@FXML private TextField name;
	@FXML private TextField surnames;
	@FXML private TextField userName;
	@FXML private PasswordField password;
	@FXML private PasswordField confirmPassword;
	@FXML private TextField email;
	@FXML private TextField country;
	
	@FXML
	public void trySignIn(ActionEvent evt) {
		if(name != null)
		try (Connection con = new Conector().getMySQLConnection()) {
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
