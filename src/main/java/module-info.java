module hr.game.pandemic {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.game.pandemic to javafx.fxml;
    exports hr.game.pandemic;
}