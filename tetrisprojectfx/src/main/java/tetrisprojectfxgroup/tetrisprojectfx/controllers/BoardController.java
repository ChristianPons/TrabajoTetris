package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.BasicData;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class BoardController {
	
	BasicData data;
	@FXML Label level;
	@FXML Label score;
	@FXML GridPane piece1;
	@FXML GridPane piece2;
	@FXML GridPane piece3;
	@FXML GridPane pieceHolder;
	@FXML GridPane board;

	public BoardController(BasicData data) throws IOException {
		level.setText("1");
		score.setText("0");
		App.setRoot("Board");
	}
	
	public void setNextPieces() {
		
	}
	
	public void holdPiece() {
		
	}
}
