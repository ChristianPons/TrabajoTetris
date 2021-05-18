package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.BasicData;
import services.conector.Conector;
import services.dao.Player;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class LoginController {
	
	@FXML private TextField userName;
	@FXML private PasswordField password;


	@FXML
	public void tryLogin(ActionEvent evt) throws IOException {
		try (Connection con = new Conector().getMySQLConnection()) {
			Player player = PlayerManager.login(con, userName.getText(), password.getText());
			
			if(player != null) {
				BasicData data = new BasicData();
				data.setPlayer(player);
				new MainMenuController(data);
				
			} else {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("EL nombre o la contraseña está mal, por favor inténtelo de nuevo");
				a.show();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
			
		}
	
	@FXML
	public void signIn() throws IOException {
		new SignInController();
	}
	
}
