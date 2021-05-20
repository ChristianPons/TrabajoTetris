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
	
	/**
	 * This method tries to add a new player using the method singIn from PlayerManager using the values of the text fields of the window.
	 * <ul>
	 * 		<li>if all the fields are filled and password and confirmPassword are equal a new player is added to the table with that values.</li>
	 * 		<li>if some field is empty or the password and confirmPaswword does not match, Shows an alert.</li>
	 * </ul>
	 * @param evt
	 */
	
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
			Alert a = new Alert(AlertType.ERROR);
			String tpassword = "La contraseña introducida y su confirmación no son iguales";
			String empty = "Alguno de los campos está vacío, por favor rellene todos los campos";
			a.setContentText(password.getText().equals(confirmPassword.getText()) ? tpassword:empty);
			a.show();
		}
		
		
	}
	
	/**
	 * This method returns to the first window.
	 * @throws IOException
	 */
	
	@FXML
	public void goBack() throws IOException {
		App.setRoot("Login");
	}
}
