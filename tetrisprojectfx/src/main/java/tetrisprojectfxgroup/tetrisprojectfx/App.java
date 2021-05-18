package tetrisprojectfxgroup.tetrisprojectfx;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	
        scene = new Scene(loadFXML("primary"), 640, 480);
     // create a input stream


        // create a image
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("miscellaneous/Background.png"));

        // create a background image
        BackgroundImage backgroundimage = new BackgroundImage(image, 
                                         BackgroundRepeat.NO_REPEAT, 
                                         BackgroundRepeat.NO_REPEAT,
                                         BackgroundPosition.DEFAULT, 
                                         new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        // create Background
        Background background = new Background(backgroundimage);

        // set background
        ((VBox) scene.lookup("#vbox")).setBackground(background);
        stage.setScene(scene);
        System.out.println(stage.getRenderScaleX());
        System.out.println(stage.getRenderScaleY());
        stage.show();
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