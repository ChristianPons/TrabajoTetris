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

	public static Player getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}

	public void setPlayer(Player player2) {
		player = player2;
		
	}

}
