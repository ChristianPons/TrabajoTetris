package tetrisprojectfxgroup.tetrisprojectfx.controllers;



import java.net.URL;
import java.util.ResourceBundle;

import gamecore.logic.Board;
import gamecore.logic.GameLogic;
import gamecore.resources.VariableTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.BasicData;


public class BoardController extends GameLogic implements Initializable{
	
	public BoardController() {
		super(1, 3);

	}



	BasicData data;
	@FXML Label level;
	@FXML Label score;
	@FXML GridPane piece1;
	@FXML GridPane piece2;
	@FXML GridPane piece3;
	@FXML GridPane pieceHolder;
	@FXML GridPane board;
	Pane[][] panelBoard = new Pane[Board.BOARD_HEIGHT][Board.BOARD_WIDTH];
	Color[] color = {Color.TRANSPARENT, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE};
	VariableTimer timer = new VariableTimer() {

		@Override
		public void task() {
			moveDown(false);
			
		}
		
	};
	
	@Override
	public void levelUp() {
		super.levelUp();
	}
	
	@Override
	public void gameOver() {
		
	}
	
	public void setControls() {

	}
	
	public void refresh() {
		int[][]board = getBoard().getBoard();
	}
	
	public void setGridPane() {
		for(int i = 0; i < panelBoard.length; i++) {
			for(int j = 0; j < panelBoard[i].length; j++) {
				Pane pane = new Pane();
				panelBoard[i][j] = pane;
				board.add(pane, j, i);
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGridPane();
		panelBoard[5][5].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	
}
