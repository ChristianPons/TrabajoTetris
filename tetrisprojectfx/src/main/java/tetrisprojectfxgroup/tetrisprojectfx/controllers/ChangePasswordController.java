package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import model.BasicData;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;
import services.conector.Conector;

public class ChangePasswordController {

	@FXML private PasswordField newPassword;
	@FXML private PasswordField confirmedPassword;
	
	@FXML
	public void changePassword() {
		if(newPassword.getText().equals(confirmedPassword.getText())) {
			try(Connection con =  new Conector().getMySQLConnection()) {
				new PlayerManager().changePassword(con,BasicData.getPlayer().getUserName() , newPassword.getText());
				Alert a = new Alert(AlertType.INFORMATION);
				a.setContentText("Se ha cambiado la contraseña");
				a.show();
				goBack();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("La contraseña y la confirmación no son iguales");
			a.show();
		}
	}
	
	@FXML
	public void goBack() throws IOException {
		App.setRoot("Options");
	}
}
