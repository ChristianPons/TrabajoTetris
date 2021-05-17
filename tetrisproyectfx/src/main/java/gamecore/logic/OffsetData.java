package gamecore.logic;

import lombok.Getter;

/**
 * Enum that contains all of the offset data required when rotating a piece
 * 
 * @author Miguel Ruiz
 *
 */
public enum OffsetData {
	JLSTZ_OFFSET(new int[][][] {
		{{0, 0}, {0, 0}, {0, 0}, {0, 0}},
		{{0, 0}, {1, 0}, {0, 0}, {-1, 0}},
		{{0, 0}, {1, -1}, {0, 0}, {-1, -1}},
		{{0, 0}, {0, 2}, {0, 0}, {0, 2}},
		{{0, 0}, {1, 2}, {0, 0}, {-1, 2}}
	}),
	I_OFFSET(new int[][][] {
		{{0, 0}, {-1, 0}, {-1, 1}, {0, 1}},
		{{-1, 0}, {0, 0}, {1, 1}, {0, 1}},
		{{2, 0}, {0, 0}, {-2, 1}, {0, 1}},
		{{-1, 0}, {0, 1}, {1, 0}, {0, -1}},
		{{2, 0}, {0, -2}, {-2, 0}, {0, 2}}
	});
	
	@Getter private int[][][] offsetData;
	
	/**
	 * It just assigns the offset data to the array
	 * 
	 * @param offsetData
	 */
	private OffsetData(int[][][] offsetData) {
		this.offsetData = offsetData;
	}
	
}
