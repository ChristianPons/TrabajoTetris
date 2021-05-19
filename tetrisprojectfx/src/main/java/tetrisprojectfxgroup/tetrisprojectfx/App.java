package tetrisprojectfxgroup.tetrisprojectfx;

import java.io.IOException;
import java.sql.Connection;

import gamecore.logic.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.conector.Conector;
import services.manager.LobbyManager;
import services.manager.MatchStateManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	
        scene = new Scene(loadFXML("Start"));
        stage.setScene(scene);
        stage.show();
    }
    
    public void ejemploPartida() {
        Connection connection = new Conector().getMySQLConnection();
        LobbyManager host = new LobbyManager(2, connection);
        host.joinLobby();
        LobbyManager guest = new LobbyManager(host.getLobbyId(), 3, connection);
        System.out.println(host.findLobby());
        guest.joinLobby();
        System.out.println(host.findLobby());
        host.startGame();
        MatchStateManager hostMsm = new MatchStateManager(guest.getLobbyId(), guest.getUserId() ,guest.findLobby().getGuest().getPlayerId(), true, connection);
        MatchStateManager guestMsm = new MatchStateManager(guest.getLobbyId(), guest.findLobby().getHost().getPlayerId(), guest.getUserId(), false, connection);
        hostMsm.createMatch();
        guestMsm.updateState(new Board().getJSON(), 200);
        hostMsm.gameOver(new Board().getJSON(), 1000);
        guestMsm.gameOver(new Board().getJSON(), 500);
        guest.leaveLobby();
        host.leaveLobby();
        System.out.println(hostMsm.getLastStateFromMatch());
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}