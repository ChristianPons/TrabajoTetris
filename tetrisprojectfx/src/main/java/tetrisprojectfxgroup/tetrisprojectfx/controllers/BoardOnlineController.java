package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import model.BasicData;
import services.conector.Conector;
import services.dao.MatchState;
import services.manager.MatchStateManager;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class BoardOnlineController extends GameLogic implements Initializable {
	
	
	private int sidewaysInitialDelay = 110;
	private int sidewaysActiveDelay = 50;
	private int softDropDelay = 80;

	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isSoftDropping = false;

	@FXML private Label level;
	@FXML private Label scorep1;
	@FXML private Label scorep2;
	@FXML private Label player1;
	@FXML private Label player2;
	@FXML private GridPane piece3;
	@FXML private GridPane pieceHolder;
	@FXML private GridPane board;
	@FXML private GridPane board2;
	private MatchStateManager msm;
	private List<EventHandler<KeyEvent>> controls = new ArrayList<>();
	private Pane[][] panelBoard = new Pane[Board.BOARD_HEIGHT - 2][Board.BOARD_WIDTH];
	private Pane[][] panelBoard2 = new Pane[Board.BOARD_HEIGHT - 2][Board.BOARD_WIDTH];
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
		controls.add(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keyPressed(event);
			}
		});
		controls.add(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keyReleased(event);
			}
		});
		board.getScene().setOnKeyPressed(controls.get(0));
		board.getScene().setOnKeyReleased(controls.get(1));
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
	
	private VariableTimer onlineUpdate = new VariableTimer() {
		@Override
		public void task() {
			updateOnline();
		}
	};

	public BoardOnlineController() {
		super(5, 3);
		if(BasicData.isFirstPlayer()) {
			msm = new MatchStateManager(BasicData.getJoinedLobbyId(), BasicData.getPlayer().getPlayerId(),
					BasicData.getOtherPlayerId(), true, new Conector().getMySQLConnection());
			msm.createMatch();
		}
		else msm = new MatchStateManager(BasicData.getJoinedLobbyId(), BasicData.getOtherPlayerId(),
				BasicData.getPlayer().getPlayerId(), false, new Conector().getMySQLConnection());
	}

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
	public int leavePiece() {
		int nLines = super.leavePiece();
		msm.updateState(getBoard().getJSON(), getScore());
		updateOnline();
		return nLines;
	}
	
	public void updateOnline() {
		MatchState state = msm.getLastStateFromMatch();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				paintBoard2(state.getEnemyBoard(msm.isPlayer1()).getBoard(), true);
				scorep2.setText("" + (msm.isPlayer1() ? state.getUser2Score() : state.getUser1Score()));
			}
		});
	}
	
	@Override
	public void gameOver() {
		msm.gameOver(getBoard().getJSON(), getScore());
		goBack();
	}
	
	public void goBack() {
		fallTimer.stop();
		leftTimer.stop();
		rightTimer.stop();
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
		fallTimer.setInterval(getLevel() < 12 ? softDropDelay : (int)(SPEED_CURVE[getLevel()] - SPEED_CURVE[getLevel()] * 0.3));
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
				scorep1.setText("" + getScore());				
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
	
	public void paintBoard2(int[][] exBoard, boolean overWriteEmpty) {
		int[][]board = exBoard.clone();
		for(int i=0; i<panelBoard2.length; i++) {
			for(int j=0; j<panelBoard2[i].length; j++) {
				if(board[i][j] == 0 && overWriteEmpty || board[i][j] != 0) {
					panelBoard2[panelBoard.length - i - 1][j].setBackground(new Background(new BackgroundFill(colors[board[i][j]], CornerRadii.EMPTY, Insets.EMPTY)));
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
	
	public void setGridPane2() {
		for(int i = 0; i < panelBoard2.length; i++) {
			for(int j = 0; j < panelBoard2[i].length; j++) {
				Pane pane = new Pane();
				panelBoard2[i][j] = pane;
				board2.add(pane, j, i);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setGridPane();
		setGridPane2();
		fallTimer.setInterval(SPEED_CURVE[getLevel()]);
		updateLevel();
		increaseScore(0);
		App.reSize();
		onlineUpdate.setInterval(300);
	}
}
