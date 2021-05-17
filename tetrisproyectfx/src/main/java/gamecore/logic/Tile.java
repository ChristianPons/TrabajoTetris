package gamecore.logic;

import lombok.Getter;
import lombok.Setter;

/**
 * Creates every one of the tiles that will be used to form the pieces.
 * 
 * @author Miguel Ruiz
 *
 */
@Getter
public class Tile {
	private int relativeX;
	private int relativeY;
	@Setter private int value = 0;
	
	/**
	 * Creates the tile and sets the initial relative position to the tile's center
	 * 
	 * @param xCoordinate - int
	 * @param yCoordinate - int
	 */
	public Tile(int xCoordinate, int yCoordinate) {
		this.relativeX = xCoordinate;
		this.relativeY = yCoordinate;
	}
	
	/**
	 * Sets the relative coordinates of the tile to the ones specified
	 * 
	 * @param xCoordinate - int
	 * @param yCoordinate - int
	 */
	private void setCoordinates(int xCoordinate, int yCoordinate) {
		this.relativeX = xCoordinate;
		this.relativeY = yCoordinate;
	}
	
	/**
	 * Checks wether a tile can be moved to a specified position within a designated board or not
	 * 
	 * @param board - Board
	 * @param xMovement - int
	 * @param yMovement - int
	 * @return
	 * <ul>
	 *     <li>true - if the tile can be moved</li>
	 *     <li>false - if the tile cannot be moved</li>
	 * </ul>
	 */
	public boolean canMoveTile(Board board, int xMovement, int yMovement){
		return board.isInBounds(xMovement + relativeX, yMovement + relativeY) && board.isPosEmpty(xMovement + relativeX, yMovement + relativeY);
	}
	
	
	/**
	 * Rotates the tile either clockwise or counterclockwise
	 * 
	 * @param isClockwise - boolean 
	 */
	public void rotateTile(boolean isClockwise) {
		int[][] rotationMatrix = isClockwise ? new int[][] {{0, -1}, {1, 0}} : new int[][] {{0, 1}, {-1, 0}};
		setCoordinates(rotationMatrix[0][0] * relativeX + rotationMatrix[1][0] * relativeY, rotationMatrix[0][1] * relativeX + rotationMatrix[1][1] * relativeY);
	}
}