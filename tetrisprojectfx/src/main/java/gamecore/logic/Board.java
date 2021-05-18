package gamecore.logic;

import java.util.Arrays;

import org.json.JSONObject;

import lombok.Setter;

import org.json.JSONArray;

/**
 * Creates the board in which the game is to be played.
 * 
 * @author Miguel Ruiz
 *
 */
public class Board {
	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 22;
	@Setter private int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];

	/**
	 * Checks wether the specified position is already occupied or not
	 * 
	 * @param xCoordinate - int
	 * @param yCoordinate - int
	 * @return
	 * <ul>
	 *     <li>true - if the position is empty</li>
	 *     <li>false - if it's not empty</li>
	 * </ul>
	 */
	public boolean isPosEmpty(int xCoordinate, int yCoordinate) {
		try {
			return board[yCoordinate][xCoordinate] == 0;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Checks wether the coordinates are in within the bounds of the board or not
	 * 
	 * @param xCoordinate - int
	 * @param yCoordinate - int
	 * @return
	 * <ul>
	 *     <li>true - if the coordinates are within the bounds of the board</li>
	 *     <li>false - if they're not in bounds</li>
	 * </ul>
	 */
	public boolean isInBounds(int xCoordinate, int yCoordinate) {
		return (xCoordinate >= 0 && xCoordinate < BOARD_WIDTH) && (yCoordinate >= 0 && yCoordinate < BOARD_HEIGHT);
	}

	/**
	 * Paints or fives the specified position the value specified, having made sure
	 * beforehand that the position is valid
	 * 
	 * @param xCoordinate - int
	 * @param yCoordinate - int
	 * @param value       - int
	 */
	public void paintPos(int xCoordinate, int yCoordinate, int value) {
		if (isInBounds(xCoordinate, yCoordinate) && isPosEmpty(xCoordinate, yCoordinate)) {
			board[yCoordinate][xCoordinate] = value;
		}
	}

	/**
	 * Checks if the row is filled
	 * 
	 * @param height - int
	 * @return
	 * <ul>
	 *     <li>true - if the row is filled</li>
	 *     <li>false - if the row is not filled</li>
	 * </ul>
	 */
	private boolean isRowFilled(int height) {
		for (int i = 0; i < board.length; i++) {
			if (isPosEmpty(i, height)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Cleans the row specified, and brings down every row that's above it by 1 tile
	 * 
	 * @param height - int
	 */
	private void cleanRow(int height) {
		for (int i = height; i < board.length - 1; i++) {
			board[i] = board[i + 1].clone();
		}
	}

	/**
	 * Checks every line and clears it if they are full. After that, the number of
	 * lines cleared is returned
	 * 
	 * @return The number of lines that are cleared
	 */
	public int checkRows() {
		int lineCount = 0;
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			if (isRowFilled(i)) {
				cleanRow(i);
				lineCount++;
			}
		}
		return lineCount;
	}

	/**
	 * Repeatedly checks for lines that are filled, to then clear them, and returns
	 * the number of lines that are cleared
	 * 
	 * @return
	 */
	public int cleanLines() {
		int lineCount;
		int totalLineCount = 0;
		do {
			lineCount = checkRows();
			totalLineCount += lineCount;
		} while (lineCount > 0);
		return totalLineCount;
	}

	/**
	 * Returns an exact copy of the board
	 * 
	 */
	public int[][] getBoard() {
		return this.board.clone();
	}

	/**
	 * Fills the board with the specified value
	 * 
	 * @param value - int
	 */
	public void fillBoard(int value) {
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			Arrays.fill(board[i], value);
		}
	}

	/**
	 * Fills all the non-empty tiles of the board with the specified value
	 * @param value - int
	 */
	public void paintAllPieces(int value) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!isPosEmpty(j, i))
					board[i][j] = value;
			}
		}
	}
	
	/**
	 * Gets the JSON document from the current board
	 * 
	 * @return A JSON string - String
	 */
	public String getJSON() {
		JSONArray jsonBoard = new JSONArray(board);
		return "{\"board\": " + jsonBoard.toString() + "}";
	}
	
	/**
	 * Generates a board from a JSON String
	 * 
	 * @param jsonString - String
	 * @return the board coming from the JSON
	 */
	public static Board getFromJSONString(String jsonString) {
		Board board = new Board();
		int[][] boardArray = board.getBoard();
		JSONObject obj = new JSONObject(jsonString);
		JSONArray array = (JSONArray)obj.get("board");
		for(int i=0; i<board.getBoard().length && i<array.length(); i++) {
			for(int j=0; j<board.getBoard()[i].length && j<array.getJSONArray(i).length(); j++) {
				boardArray[i][j] = Integer.parseInt(array.getJSONArray(i).get(j).toString());
			}
		}
		return board;
	}
}
