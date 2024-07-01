package hr.game.pandemic;

import hr.game.pandemic.exeption.WrongPlayerNameException;
import hr.game.pandemic.jndi.ConfigurationKey;
import hr.game.pandemic.jndi.ConfigurationReader;
import hr.game.pandemic.model.PlayerEnum;
import hr.game.pandemic.networking.Client;
import hr.game.pandemic.networking.Server;
import hr.game.pandemic.rmi.RMIServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumSet;

public class GameApplication extends Application {

    public static PlayerEnum player;
    private static int HEIGHT;
    private static int WIDTH;
    public static String HOST;
    public static int PORT;
    public static Server server;
    public static Client client;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("pandemic-main.fxml"));
        if (player.equals(PlayerEnum.SERVER)) {
            fxmlLoader = new FXMLLoader(GameApplication.class.getResource("pandemic-server.fxml"));
            WIDTH = 600;
            HEIGHT = 400;
        } else {
            WIDTH = 1920;
            HEIGHT = 1080;
        }

        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(player.name());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException{

        HOST = ConfigurationReader.getValue(ConfigurationKey.MULT_HOST);
        PORT = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.MULT_PORT));

        String firstArg = "";
        if(args.length > 0)
            firstArg = args[0];

        boolean z = false;

        for (PlayerEnum value : EnumSet.allOf(PlayerEnum.class)) {
            if (firstArg.equals(value.name())) {
                z = true;
                break;
            }
        }

        if (z) {
            player = PlayerEnum.valueOf(firstArg);
        } else {
            throw new WrongPlayerNameException("The game was started with an invalid player name");
        }



        if (player.equals(PlayerEnum.SERVER)) {
            ServerSocket serverSocket = new ServerSocket(GameApplication.PORT);
            server = new Server(serverSocket);
            server.startServer();
            RMIServer.startRMIServer();
        } else {
            Socket socket = new Socket(GameApplication.HOST, GameApplication.PORT);
            client = new Client(socket);
            client.listenForGameState();
        }

        launch();
    }

    public static void stopServer() {
        server.closeServerSocket();
    }

}