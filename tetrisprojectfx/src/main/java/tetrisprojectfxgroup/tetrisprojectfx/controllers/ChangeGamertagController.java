package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.BasicData;
import services.conector.Conector;
import services.manager.PlayerManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class ChangeGamertagController {

	@FXML public TextField newGamertag;
	
	@FXML
	public void changeGamertag() {
		if(newGamertag.getText().length() <= 20) {
			try(Connection con =  new Conector().getMySQLConnection()) {
				new PlayerManager().changeUserName(con, BasicData.getPlayer().getUserName(), newGamertag.getText());
				goBack();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void goBack() throws IOException {
		App.setRoot("Options");
	}
}
