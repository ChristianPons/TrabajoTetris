package gamecore.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum that contains all of 7 the pieces that the game works with
 * 
 * @author Miguel Ruiz
 *
 */
public enum PiecesList {
	O(new Tile(-1, 0), new Tile(-1, 1), new Tile(0, 0), new Tile(0, 1)),
	I(new Tile(-1, 0), new Tile(0, 0), new Tile(1, 0), new Tile(2, 0)),
	T(new Tile(-1, 0), new Tile(0, 0), new Tile(0, 1), new Tile(1, 0)),
	S(new Tile(-1, 0), new Tile(0, 0), new Tile(0, 1), new Tile(1, 1)),
	Z(new Tile(-1, 1), new Tile(0, 1), new Tile(0, 0), new Tile(1, 0)),
	L(new Tile(-1, 0), new Tile(0, 0), new Tile(1, 0), new Tile(1, 1)),
	J(new Tile(-1, 1), new Tile(-1, 0), new Tile(0, 0), new Tile(1, 0));

	private Piece piece = new Piece();
	private List<Tile> tiles = new ArrayList<>();

	/**
	 * Receives the 4 tiles that form the tetromino and assigns them to the tile list
	 * for later use when they are retrieved
	 * 
	 * @param tile1 - Tile
	 * @param tile2 - Tile
	 * @param tile3 - Tile
	 * @param tile4 - Tile
	 */
	private PiecesList(Tile tile1, Tile tile2, Tile tile3, Tile tile4) {
		tiles.add(tile1);
		tiles.add(tile2);
		tiles.add(tile3);
		tiles.add(tile4);
	}

	/**
	 * Initializes the piece and sets the tiles from the beginning so 
	 * that it will not appear with a different coordinate or rotation
	 * @return The desired piece - Piece
	 */
	public Piece getPiece() {
		piece.initializePiece();
		piece.setPieceType(this.ordinal());
		for (Tile t : tiles) {
			t.setValue(this.ordinal() + 1);
		}
		piece.setTiles(tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3));
		return piece;
	}
}
