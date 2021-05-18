package tetrisprojectfxgroup.tetrisprojectfx.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import tetrisprojectfxgroup.tetrisprojectfx.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}