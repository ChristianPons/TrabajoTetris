package tetrisprojectfxgroup.tetrisprojectfx.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gamecore.logic.Board;
import gamecore.logic.GameLogic;
import gamecore.logic.Piece;
import gamecore.logic.Tile;
import gamecore.resources.VariableTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import tetrisprojectfxgroup.tetrisprojectfx.App;


public class BoardController extends GameLogic implements Initializable{

	public BoardController() {
		super(1, 3);
	}
	
	private int sidewaysInitialDelay = 110;
	private int sidewaysActiveDelay = 50;
	private int softDropDelay = 80;

	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isSoftDropping = false;

	@FXML private Label level;
	@FXML private Label score;
	@FXML private GridPane piece3;
	@FXML private GridPane pieceHolder;
	@FXML private GridPane board;
	private Pane[][] panelBoard = new Pane[Board.BOARD_HEIGHT - 2][Board.BOARD_WIDTH];
	private Color[] colors = {Color.TRANSPARENT, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED, Color.ORANGE, Color.BLUE};
	private VariableTimer fallTimer = new VariableTimer() {
		private boolean firstTime = false;
		@Override
		public void task() {
			if(!moveDown(isSoftDropping)) {
				if(cyclesOnGround > (getLevel()<6 ? 2 : getLevel()<12 ? 3 : getLevel()<16 ? 4 : 5)) {
					leavePiece();
				}
				cyclesOnGround++;
			} else {
				cyclesOnGround = 0;
			}
			if(!firstTime) setControls();
		}
	};
	
	private void setControls() {
		board.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keyPressed(event);
			}
		});
		board.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keyReleased(event);
			}
		});
	}
	
	private VariableTimer leftTimer = new VariableTimer() {
		@Override
		public void task() {
			if(!isMovingRight) {
				moveLeft();
				isMovingLeft = true;
			}
		}
	};
	
	private VariableTimer rightTimer = new VariableTimer() {
		@Override
		public void task() {
			if(!isMovingLeft) {
				moveRight();
				isMovingRight = true;
			}
		}
	};

	@Override
	public void levelUp() {
		super.levelUp();
		if(!isSoftDropping) {
			fallTimer.setDelayedInterval(SPEED_CURVE[getLevel()]);
			updateLevel();
		}
	}
	
	public void updateLevel() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				level.setText("" + getLevel());
			}
		});
	}
	
	@Override
	public void gameOver() {
		goBack();
	}
	
	public void goBack() {
		try {
			App.setRoot("MainMenu");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void keyPressed(KeyEvent e) {
		int keyCode = e.getCode().getCode();
		if(keyCode == 37 && !isMovingLeft) leftPress();
		else if(keyCode == 39 && !isMovingRight) rightPress();
		else if(keyCode == 40 && !isSoftDropping) softDropPress();
		else if(keyCode == 38) rotate(true);
		else if(keyCode == 32) dropDown();
		else if(keyCode == 67) savePieces();
		else if(keyCode == 88) rotate(false);
		else if(keyCode == 27) goBack();
	}
	
	
	private void keyReleased(KeyEvent e) {
		int keyCode = e.getCode().getCode();
		if(keyCode == 37) leftRelease();
		else if(keyCode == 39) rightRelease();
		else if(keyCode == 40) softDropRelease();
	}
	
	public void leftPress() {
		leftTimer.task();
		leftTimer.setInterval(sidewaysActiveDelay, sidewaysInitialDelay);
		isMovingLeft = true;
	}
	
	public void leftRelease() {
		leftTimer.stop();
		isMovingLeft = false;
	}
	
	public void rightPress() {
		rightTimer.task();
		rightTimer.setInterval(sidewaysActiveDelay, sidewaysInitialDelay);
		isMovingRight = true;
	}
	
	public void rightRelease() {
		rightTimer.stop();
		isMovingRight = false;
	}
	
	public void softDropPress() {
		fallTimer.task();
		fallTimer.setInterval(getLevel() < 12 ? softDropDelay : (int)(SPEED_CURVE[getLevel()] - SPEED_CURVE[getLevel()] * 0.4));
		isSoftDropping = true;
	}
	
	public void softDropRelease() {
		fallTimer.setDelayedInterval(SPEED_CURVE[getLevel()]);
		isSoftDropping = false;
	}

	@Override
	public void refresh() {
		paintBoard(getBoard().getBoard(), true);
		paintBoard(boardFromPiece(getCurrentPiece()), false);
	}
	
	@Override
	public void increaseScore(int n) {
		super.increaseScore(n);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				score.setText("" + getScore());				
			}
		});
	}
	
	public void paintBoard(int[][] exBoard, boolean overWriteEmpty) {
		int[][]board = exBoard.clone();
		for(int i=0; i<panelBoard.length; i++) {
			for(int j=0; j<panelBoard[i].length; j++) {
				if(board[i][j] == 0 && overWriteEmpty || board[i][j] != 0) {
					panelBoard[panelBoard.length - i - 1][j].setBackground(new Background(new BackgroundFill(colors[board[i][j]], CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		}
	}
	
	public int[][] boardFromPiece(Piece piece){
		int[][] board = new Board().getBoard().clone();
		for(Tile tile : piece.getTiles()) {
			board[piece.getTileAbsoluteY(tile)][piece.getTileAbsoluteX(tile)] = tile.getValue();
		}
		return board;
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
		fallTimer.setInterval(SPEED_CURVE[getLevel()]);
		updateLevel();
		increaseScore(0);
	}


}

