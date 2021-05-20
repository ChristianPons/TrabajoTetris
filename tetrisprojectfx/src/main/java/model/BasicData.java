package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import services.dao.Player;

@Getter
@Setter
@ToString
public class BasicData {

	private static Player player;
	@Getter @Setter private static int joinedLobbyId;
	@Getter @Setter private static boolean isFirstPlayer = false;
	@Getter @Setter private static int otherPlayerId;

	public static Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player2) {
		player = player2;
		
	}

}
