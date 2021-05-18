package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.scene.control.Label;
import services.conector.Conector;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class LoginController {
	
	@FXML private Label userName;
	@FXML private Label password;

	public void tryLogin(ActionEvent evt) throws IOException {
		try (Connection con = new Conector().getMySQLConnection()) {
			BasicData data = new BasicData();
			data.setPlayer(PlayerManager.login(con, userName.getText(), password.getText()));
			App.setRoot("MainMenu");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("EL nombre o la contraseña está mal, por favor inténtelo de nuevo");
			a.show();
		}
	}
}
