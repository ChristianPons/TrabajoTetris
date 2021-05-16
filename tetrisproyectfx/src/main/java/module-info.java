module tetrisproyectfxgroup.tetrisproyectfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires lombok;

    opens tetrisproyectfxgroup.tetrisproyectfx to javafx.fxml;
    exports tetrisproyectfxgroup.tetrisproyectfx;
}
