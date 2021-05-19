package gamecore.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * Runs the game logic
 * 
 * @author Miguel Ruiz
 *
 */
public class GameLogic {

	private static final int MAX_LVL = 10;
	private static final int MAX_VSBLLINES = 3;
	private static final int[] SCORE_PER_LINES = {40, 100, 300, 1200};
	protected static final int[] SPEED_CURVE = {800, 716, 633, 640, 550, 466, 383, 300, 216, 133, 100, 83, 83, 83,
			66, 66, 66, 50, 50, 50, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 16};
	private static final int SCORE_FOR_SOFTDROP = 1;
	private static final int SCORE_FOR_HARDDROP = 2;

	protected static int visibleUpcomingPieces = 0;
	private boolean canSave = true;
	@Getter private int level = 1;
	@Getter private int score = 0;
	@Getter private int lines = 0;
	protected int cyclesOnGround = 0;

	@Getter private Board board = new Board();
	@Getter private Piece currentPiece;
	@Getter private Piece savedPiece;
	
	@Getter private List<Piece> piecesPool = new ArrayList<>();

	/**
	 * Generates the game, and sets the initial difficulty and the number of visible pieces
	 * 
	 * @param level - int
	 * @param visibleUpcomingPieces - int
	 */
	public GameLogic(int level, int visibleUpcomingPieces) {
		this.level = level < 1 ? 1 : level > MAX_LVL ? MAX_LVL : level;
		GameLogic.visibleUpcomingPieces = visibleUpcomingPieces < 0 ? 0 : visibleUpcomingPieces > MAX_VSBLLINES ? MAX_VSBLLINES : visibleUpcomingPieces;
		generateUpcomingPieces();
		nextPiece();
		savedPiece = new Piece();
		savedPiece.setPieceType(-1);
		
	}
	
	/**
	 * Moves the piece down by 1 tile, and increases the score if using soft drop
	 * 
	 * @param isSoftDrop - boolean
	 * @return
	 * <ul>
	 *     <li>true - if the piece moved down</li>
	 *     <li>false - if the piece didn't move down</li>
	 * </ul>
	 */
	public boolean moveDown(boolean isSoftDrop) {
		boolean didItMove = currentPiece.movePiece(board, 0, -1);
		if(didItMove && isSoftDrop) {
			increaseScore(SCORE_FOR_SOFTDROP);
		}
		refresh();
		return didItMove;
	}
	
	/**
	 * Moves the piece left by 1 tile, and refreshes the screen afterwards. It prevents pieces 
	 * from being placed on the ground if it's used fast enough, and returns true if it was able
	 * to move.
	 * 
	 * @return
	 * <ul>
	 *     <li>true - if the piece moved left</li>
	 *     <li>false - if the piece didn't move left</li>
	 * </ul>
	 */
	public boolean moveLeft() {
		boolean didItMove = currentPiece.movePiece(board, -1, 0);
		if(didItMove && cyclesOnGround > 0) cyclesOnGround--;
		refresh();
		return didItMove;
	}
	
	/**
	 * Moves the piece right by 1 tile, and refreshes the screen afterwards. It prevents pieces 
	 * from being placed on the ground if it's used fast enough, and returns true if it was able
	 * to move.
	 * 
	 * @return
	 * <ul>
	 *     <li>true - if the piece moved right</li>
	 *     <li>false - if the piece didn't move right</li>
	 * </ul>
	 */
	public boolean moveRight() {
		boolean didItMove = currentPiece.movePiece(board, 1, 0);
		if(didItMove && cyclesOnGround > 0) cyclesOnGround--;
		refresh();
		return didItMove;
	}
	
	/**
	 * Rotates the piece clockwise or counterclockwise, and refreshes the screen afterwards.
	 * It prevents the piece from being placed if it hits ground if used fast enough, and
	 * returns true if it was able to rotate.
	 * 
	 * @param clockwise - boolean
	 * @return
	 * @return
	 * <ul>
	 *     <li>true - if the piece rotated successfully</li>
	 *     <li>false - if the piece didn't rotate successfully</li>
	 * </ul>
	 */
	public boolean rotate(boolean clockwise) {
		boolean didItRotate = currentPiece.rotate(board, clockwise);
		if(didItRotate && cyclesOnGround > 0) cyclesOnGround--;
		refresh();
		return didItRotate;
	}
	
	/**
	 * Does all the steps required to refresh the screen
	 */
	public void refresh() {
		
	}

	/**
	 * Leaves the piece on the board as it is, and calls for the next piece to arrive.
	 * @return Number of lines cleared if there are - int
	 */
	public int leavePiece() {
		int lines;
		for (Tile tile : currentPiece.getTiles()) {
			board.paintPos(currentPiece.getTileAbsoluteX(tile), currentPiece.getTileAbsoluteY(tile), tile.getValue());
			if(currentPiece.getTileAbsoluteY(tile) >= 20) gameOver();
		}
		nextPiece();
		lines = board.cleanLines();
		refresh();
		canSave = true;
		return lines;
	}

	/**
	 * Gets the next piece of the pool, and if there aren't enough to be displayed 
	 * on the screen, more will be generated.
	 */
	private void nextPiece() {
		Piece piece = piecesPool.remove(0);
		piece.initializePiece();
		currentPiece = piece;
		if (piecesPool.size() - 1 < visibleUpcomingPieces) {
			generateUpcomingPieces();
		}
	}

	/**
	 * Generates a set of all pieces and shuffles them, to then be added into the pool
	 */
	private void generateUpcomingPieces() {
		List<Piece> pieces = new ArrayList<>();
		for (int i=0; i<7; i++) {
			Piece piece = getNewPieceOfType(i);
			piece.initializePiece();
			pieces.add(piece);
		}
		Collections.shuffle(pieces);
		piecesPool.addAll(pieces);
	}
	
	/**
	 * Gets a piece of the specified type from the PiecesList enum
	 * 
	 * @param type - int
	 * @return The desired piece - Piece
	 */
	private Piece getNewPieceOfType(int type) {
		return PiecesList.values()[type].getPiece();
	}

	public void savePieces() {
		if(canSave) {
			if (savedPiece.getPieceType() != -1) {
				Piece auxiliary = getNewPieceOfType(savedPiece.getPieceType());
				savedPiece = getNewPieceOfType(currentPiece.getPieceType());
				currentPiece = auxiliary;
			} else {
				savedPiece = getNewPieceOfType(currentPiece.getPieceType());
				nextPiece();
			}
			refresh();
			canSave = false;
		}
	}
	
	/**
	 * It will repeatedly pull the piece down until it reaches ground in an instant.
	 * It also increases the score accordingly.
	 */
	public void dropDown() {
		while(currentPiece.movePiece(board, 0, -1)) {
			increaseScore(SCORE_FOR_HARDDROP);
		}
		addLines(leavePiece());
	}
	
	/**
	 * Increases the score
	 * @param n  int
	 */
	public void increaseScore(int n) {
		score += n;
	}
	
	/**
	 * Increases the number of lines cleared
	 * @param n - int
	 */
	public void addLines(int n) {
		int lastLinesNumber = lines;
		if(n>0) increaseScore(SCORE_PER_LINES[n-1] * level);
		lines += n;
		if(lastLinesNumber / 10 < lines / 10) levelUp();
	}
	
	/**
	 * Increases the level of difficulty
	 */
	public void levelUp() {
		level++;
	}
	
	/**
	 * Triggers the game to stop - It must be overridden to be able to do anything
	 */
	public void gameOver() {
		
	}
	
}
