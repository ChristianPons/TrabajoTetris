package tetrisprojectfxgroup.tetrisprojectfx.controllers;



import java.net.URL;
import java.util.ResourceBundle;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import gamecore.logic.Board;
import gamecore.logic.GameLogic;
import gamecore.logic.Piece;
import gamecore.logic.Tile;
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


public class BoardController extends GameLogic implements Initializable{
	
	boolean isSoftDropping = false;

	@FXML Label level;
	@FXML Label score;
	@FXML GridPane piece1;
	@FXML GridPane piece2;
	@FXML GridPane piece3;
	@FXML GridPane pieceHolder;
	@FXML GridPane board;
	Pane[][] panelBoard = new Pane[Board.BOARD_HEIGHT][Board.BOARD_WIDTH];
	Color[] colors = {Color.TRANSPARENT, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE};
	VariableTimer timer = new VariableTimer(SPEED_CURVE[getLevel()]) {
		@Override
		public void task() {
			moveDown(isSoftDropping);
		}
		
	};
	
	public BoardController() {
		super(1, 3);
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGridPane();
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void levelUp() {
		super.levelUp();
		timer.setDelayedInterval(SPEED_CURVE[getLevel()]);
	}
	
	@Override
	public void gameOver() {
		
	}
	
	public void setControls() {
		
	}
	
	@Override
	public void refresh() {
		int[][]board = getBoard().getBoard();
		Piece piece = getCurrentPiece();
		for(Tile tile : piece.getTiles()) {
			board[piece.getTileAbsoluteY(tile)][piece.getTileAbsoluteX(tile)] = tile.getValue();
		}
		if(isSoftDropping) board[0][0] = 1;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				panelBoard[Board.BOARD_HEIGHT - i - 3][j].setBackground(new Background(new BackgroundFill(colors[board[i][j]], CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}
		
		System.out.println(getBoard().getJSON());
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
	
	
	
	
}
