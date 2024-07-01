package hr.game.pandemic.networking;

import hr.game.pandemic.model.GameStateDTO;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private String clientUsername;
    private static int playerNumber = 1;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            clientUsername = "Player " + playerNumber;
            playerNumber++;

            this.outputStream = socket.getOutputStream();
            this.objectOutputStream = new ObjectOutputStream(outputStream);
            this.inputStream = socket.getInputStream();
            this.objectInputStream = new ObjectInputStream(inputStream);

            clientHandlers.add(this);

        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
        }
    }

    @Override
    public void run() {

        while (socket.isConnected()) {
            try {
                GameStateDTO gameStateDTO = (GameStateDTO) objectInputStream.readObject();
                System.out.println(clientUsername + " is broadcasting game state");
                broadcastMessage(gameStateDTO);
            } catch (IOException e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void broadcastMessage(GameStateDTO gameStateToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    System.out.println("Sending game state to " + clientHandler.clientUsername);
                    clientHandler.objectOutputStream.writeObject(gameStateToSend);
                }
            } catch (IOException e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
            }
        }
    }

    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        clientHandlers.remove(this);
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
