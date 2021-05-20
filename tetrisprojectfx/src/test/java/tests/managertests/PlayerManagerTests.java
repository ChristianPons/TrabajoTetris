package tests.managertests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import services.conector.Conector;
import services.dao.Player;
import services.manager.PlayerManager;

@TestMethodOrder(OrderAnnotation.class)
class PlayerManagerTests {
	
	private int players;
	private Player player;

	@Test
	@Order(1)
	void test1_getAllPlayers() {
		try(Connection con =  new Conector().getMySQLConnection()) {
			List<Player>playerList = new PlayerManager().getAllPlayers(con);
			this.players = (playerList.size() > 0) ? this.players = playerList.size() : fail();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Order(2)
	void test2_signIn_OK() {
		try(Connection con = new Conector().getMySQLConnection()){
			PlayerManager.signIn(con, "Ivan", "Pavilichenko", "Blyatman","Vodka", "Kalashnikovgodman@gmail.com", "Russia");
			int playerCount = (int) new PlayerManager().getAllPlayers(con).stream().count();
			assertEquals(players + 1, playerCount);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(3)
	void test3_login_Ok() {
		try(Connection con = new Conector().getMySQLConnection()) {
			this.player = PlayerManager.login(con, "Blyatman", "Vodka");
			assertEquals("Ivan", this.player.getName());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(4)
	void test4_changeUserName_Ok() {
		try(Connection con = new Conector().getMySQLConnection()) {
			new PlayerManager().changeUserName(con, "Blyatman", "BLyat-Man");
			assertEquals("Blyat-Man", this.player.getName());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(5)
	void test5_deletePlayer_Ok() {
		try(Connection con = new Conector().getMySQLConnection()) {
			new PlayerManager().deletePlayer(con, "Blyat-Man");
			int playerCount = (int) new PlayerManager().getAllPlayers(con).stream().count();
			assertEquals(players, playerCount);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(6)
	void test6_findAllById(){
		try(Connection con = new Conector().getMySQLConnection()) {
			Set<Integer> ids = new HashSet<>(Arrays.asList(13,14));
			List<Player> players = PlayerManager.findAllById(con, ids);
			
			assertEquals(2, players.size());
			assertEquals("1", players.get(1).getName());
			assertEquals("2", players.get(2).getName());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
