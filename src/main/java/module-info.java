module hr.game.pandemic {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.common;

    opens hr.game.pandemic.dialogs to javafx.fxml;
    exports hr.game.pandemic.dialogs;
    opens hr.game.pandemic.model to javafx.fxml;
    exports hr.game.pandemic.model;
    opens hr.game.pandemic to javafx.fxml;
    exports hr.game.pandemic;
}