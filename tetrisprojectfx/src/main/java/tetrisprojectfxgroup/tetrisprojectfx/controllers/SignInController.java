package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.conector.Conector;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

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
		if (name.getText() != null && surnames.getText() != null && userName.getText() != null
				&& password.getText() != null && confirmPassword.getText() != null && email.getText() != null
				&& country.getText() != null && password.getText().equals(confirmPassword.getText())) {
			
			try (Connection con = new Conector().getMySQLConnection()) {
				PlayerManager.signIn(con, name.getText(), surnames.getText(), userName.getText(), password.getText(),
						email.getText(), country.getText());
				
					
					goBack();
				
				
			}catch(SQLException e) {
				e.printStackTrace();
			}catch(IOException f) {
				f.printStackTrace();
			}
		
		}else {
			if(surnames.getText() != null) {
				System.out.println(name.getText());
			}
			Alert a = new Alert(AlertType.ERROR);
			String tpassword = "La contraseña introducida y su confirmación no son iguales";
			String empty = "Alguno de los campos está vacío, por favor rellene todos los campos";
			a.setContentText(password.getText().equals(confirmPassword.getText()) ? tpassword:empty);
			a.show();
		}
		
		
	}
	
	@FXML
	public void goBack() throws IOException {
		App.setRoot("Start");
	}
}
