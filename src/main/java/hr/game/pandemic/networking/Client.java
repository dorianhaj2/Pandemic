package hr.game.pandemic.networking;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.GameStateDTO;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    private Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    public Client(Socket socket) {
        try {
            this.socket = socket;

            this.outputStream = socket.getOutputStream();
            this.objectOutputStream = new ObjectOutputStream(outputStream);
            this.inputStream = socket.getInputStream();
            this.objectInputStream = new ObjectInputStream(inputStream);

        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }

    public void sendGameState() {
        try {
            objectOutputStream.writeUnshared(new GameStateDTO());
            objectOutputStream.reset();
        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }

    public void listenForGameState() {
        new Thread(() -> {
            GameStateDTO gameStateDTO;

            while (socket.isConnected()) {
                try {
                    gameStateDTO = (GameStateDTO) objectInputStream.readObject();
                    System.out.println(GameApplication.player.name() + " read game state");
                    gameStateDTO.setGameState();
                } catch (IOException e) {
                    closeEverything(socket, objectInputStream, objectOutputStream);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}