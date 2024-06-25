module hr.game.pandemic {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires org.apache.commons.lang3;
    requires java.rmi;
    requires jdk.jfr;
    requires static lombok;
    requires java.naming;

    opens hr.game.pandemic.dialogs to javafx.fxml;
    exports hr.game.pandemic.dialogs;
    opens hr.game.pandemic.model to javafx.fxml;
    exports hr.game.pandemic.model;
    opens hr.game.pandemic to javafx.fxml;
    exports hr.game.pandemic;
    opens hr.game.pandemic.rmi to java.rmi;
    exports hr.game.pandemic.rmi to java.rmi;
    opens hr.game.pandemic.jndi to java.naming;
    exports hr.game.pandemic.jndi to java.naming;
}