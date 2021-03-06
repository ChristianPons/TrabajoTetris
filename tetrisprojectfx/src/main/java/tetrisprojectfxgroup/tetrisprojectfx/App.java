package tetrisprojectfxgroup.tetrisprojectfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage myStage;

    @Override
    public void start(Stage stage) throws IOException {
    	
    	myStage = stage;
        scene = new Scene(loadFXML("Start"));
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
    
    
    /**
     * This shows the window we send it and.
     * @param fxml The xml archive of the window.
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        myStage.sizeToScene();
    }
    
    /**
     * This method resizes the window to show all the window content.
     */
    public static void reSize() {
    	myStage.sizeToScene();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}