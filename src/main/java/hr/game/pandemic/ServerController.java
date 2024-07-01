package hr.game.pandemic;

public class ServerController {

    public void serverShutDown() {
        GameApplication.stopServer();
        System.exit(0);
    }

}
