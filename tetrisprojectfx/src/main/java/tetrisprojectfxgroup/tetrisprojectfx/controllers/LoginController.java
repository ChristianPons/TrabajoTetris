package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Getter;
import model.BasicData;
import services.conector.Conector;
import services.dao.Player;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class LoginController {
	
	@FXML private TextField userName;
	@FXML private PasswordField password;

	
	@Getter BasicData data;


	@FXML
	public void tryLogin(ActionEvent evt) throws IOException {
		try (Connection con = new Conector().getMySQLConnection()) {
			Player player = PlayerManager.login(con, userName.getText(), password.getText());
			
			if(player != null) {
				throw new SQLException();
			}
			
			data = new BasicData();
			data.setPlayer(player);
			App.setRoot("MainMenu");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("EL nombre o la contraseña está mal, por favor inténtelo de nuevo");
			a.show();
		}
			
		}
	
	@FXML
	public void signIn() throws IOException {
		App.setRoot("SignIn");
		
	}
	
}
