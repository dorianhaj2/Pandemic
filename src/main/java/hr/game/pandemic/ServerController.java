package hr.game.pandemic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class ServerController {

    public void serverShutDown() {
        GameApplication.server.closeServerSocket();
    }

}
