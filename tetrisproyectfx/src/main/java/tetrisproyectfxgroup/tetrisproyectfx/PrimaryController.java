package tetrisproyectfxgroup.tetrisproyectfx;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class PrimaryController {
	
	@FXML private Button primaryButton;

    @FXML
    private void switchToSecondary(ActionEvent evt) throws IOException {
        primaryButton.setText("Prueba");
       
    }
}
