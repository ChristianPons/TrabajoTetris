package gamecore.logic;

import lombok.Getter;

/**
 * Creates the piece that will be controlled or stored when the game is running
 * 
 * @author Miguel Ruiz
 *
 */
public class Piece {

	@Getter private Tile[] tiles;
	@Getter private int x;
	@Getter private int y;
	private int rotation = 0;
	@Getter private int pieceType = 0;

	public static final int INITIAL_X = Board.BOARD_WIDTH / 2;
	public static final int INITIAL_Y = Board.BOARD_HEIGHT-2;

	/**
	 * Empty constructor - It generates the initial state of the piece 
	 * without having any tiles associated
	 */
	public Piece() {
		initializePiece();
		tiles = new Tile[4];
	}

	/**
	 * Rotates the piece inside the board clockwise or counterclockwise,
	 * and checks for collisions to either wall kick or cancel the move
	 * 
	 * @param board - Board
	 * @param clockwise - boolean
	 * @return
	 * <ul>
	 *     <li>true - if the piece can be rotated</li>
	 *     <li>false - if the piece cannot be rotated</li>
	 * </ul>
	 */
	public boolean rotate(Board board, boolean clockwise) {
		//Can't rotate the O tetromino
		if (this.pieceType!=0) {
			int oldRotation = rotation;
			rotation = calculateRotation(clockwise);
			for(Tile tile : tiles) {
				tile.rotateTile(clockwise);
			}
			//Calculates offset. If it can't rotate in any way it 
			//will rotate again in the opposite way
			if(!offset(board, oldRotation, rotation)) {
				for(Tile tile : tiles) {
					tile.rotateTile(!clockwise);
				}
				rotation = calculateRotation(!clockwise);
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates the offset of the piece that is rotated, and returns true
	 * if the piece can be rotated
	 * 
	 * @param board - Board
	 * @param oldRotation - int
	 * @param newRotation - int
	 * @return
	 * <ul>
	 *     <li>true - if the piece can be rotated</li>
	 *     <li>false - if the piece cannot be rotated</li>
	 * </ul>
	 */
	public boolean offset(Board board, int oldRotation, int newRotation) {
			int[] offsetVal1 = new int[2];
			int[] offsetVal2 = new int[2];
			int[] endOffset = new int[2];
			int[][][] currentOffsetData;
			
			if(this.pieceType > 1) {
				currentOffsetData = OffsetData.JLSTZ_OFFSET.getOffsetData();
			}
			else {
				currentOffsetData = OffsetData.I_OFFSET.getOffsetData();
			}
			
			for(int[][] offsetData: currentOffsetData) {
				offsetVal1 = offsetData[oldRotation];
				offsetVal2 = offsetData[newRotation];
				for(int i=0; i<endOffset.length; i++) {
					endOffset[i] = offsetVal1[i] - offsetVal2[i];
				}
				if(canMovePiece(board, endOffset[0], endOffset[1])) {
					movePiece(board, endOffset[0], endOffset[1]);
					return true;
				}
			}
			
			return false;
	}
	
	/**
	 * Calculates the rotation index
	 * 
	 * @param clockwise - boolean
	 * @return The new rotation index - int
	 */
	private int calculateRotation(boolean clockwise) {
		int rotation = this.rotation;
		if (clockwise)
			rotation++;
		else
			rotation--;
		rotation = rotation>3 ? 0 : (rotation<0 ? 3 : rotation);
		return rotation;
	}
	
	/**
	 * Gets the absolute x coordinate of the specified tile
	 * @param tile - Tile
	 * @return
	 */
	public int getTileAbsoluteX(Tile tile) {
		return tile.getRelativeX() + x;
	}

	/**
	 * Gets the absolute y coordinate of the specified tile
	 * 
	 * @param tile - Tile
	 * @return
	 */
	public int getTileAbsoluteY(Tile tile) {
		return tile.getRelativeY() + y;
	}

	/**
	 * Sets initial piece state. Must be used several times to prevent several bugs
	 */
	protected void initializePiece() {
		x = INITIAL_X;
		y = INITIAL_Y;
		while(rotation != 0) {
			rotate(new Board(), true);
		}
	}

	/**
	 * Sets the tiles of the piece
	 * 
	 * @param tile1 - Tile
	 * @param tile2 - Tile
	 * @param tile3 - Tile
	 * @param tile4 - Tile
	 */
	public void setTiles(Tile tile1, Tile tile2, Tile tile3, Tile tile4) {
		tiles[0] = tile1;
		tiles[1] = tile2;
		tiles[2] = tile3;
		tiles[3] = tile4;
	}
	
	/**
	 * Calculates whether the piece can be moved or not specifying the location
	 * 
	 * @param board - Board
	 * @param xMovement - int
	 * @param yMovement - int
	 * @return
	 * <ul>
	 *     <li>true - if the piece can be moved</li>
	 *     <li>false - if the piece cannot be moved</li>
	 * </ul>
	 */
	public boolean canMovePiece(Board board, int xMovement, int yMovement) {
		for (Tile t : tiles) {
			if (!t.canMoveTile(board, xMovement + x, yMovement + y))
				return false;
		}
		return true;
	}
	
	/**
	 * Moves the entire piece to the desired location of a board. It checks that the piece can be move beforehand
	 * 
	 * @param board - Board
	 * @param xMovement - int
	 * @param yMovement - int
	 * @return 
	 * <ul>
	 *     <li>true - if the piece has been moved</li>
	 *     <li>false - if the piece has not been moved</li>
	 * </ul>
	 */
	public boolean movePiece(Board board, int xMovement, int yMovement) {
		if(!canMovePiece(board, xMovement, yMovement)) return false;
		x+=xMovement;
		y+=yMovement;
		return true;
	}
	
	/**
	 * Sets the type of the piece.
	 * 
	 * @param value - int
	 */
	public void setPieceType(int value) {
		this.pieceType = value;
		if(value == 1) x--;
	}
	
	/**
	 * Gets the specified tile.
	 * 
	 * @param index - int
	 * @return The desired tile - Tile
	 */
	public Tile getTile(int index) {
		return tiles[index];
	}

}
