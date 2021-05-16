module tetrisproyectfxgroup.tetrisproyectfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;

    opens tetrisproyectfxgroup.tetrisproyectfx to javafx.fxml;
    exports tetrisproyectfxgroup.tetrisproyectfx;
}
