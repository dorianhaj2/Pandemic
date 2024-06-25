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


/*
public class Client extends Thread {

    public Client(String name) {
        super(name);
    }

    @Override
    public void run() {
        // we use new Socket for each client call
        try (MulticastSocket clientSocket = new MulticastSocket(Server.CLIENT_PORT)) {

            InetAddress group = InetAddress.getByName(Server.GROUP);
            InetSocketAddress groupAddress = new InetSocketAddress(group, Server.CLIENT_PORT);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(Server.HOST));
            System.err.println(getName() + " joining group");

            clientSocket.joinGroup(groupAddress, networkInterface);

            System.err.println(getName() + " listening...");



            Za output
            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            GameState gameState = new GameState();
            oos.writeObject(gameState);
            byte[] buffer = baos.toByteArray();

            String stopString = "+";
            while (stopString.equals("+")) {
                byte[] buffer = new byte[64];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(packet);
                System.out.println(GameApplication.player.name() + " received game state");
            }
            System.err.println(getName() + " leaving group");
            clientSocket.leaveGroup(groupAddress, networkInterface);

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameState() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        GameStateDTO gameState = new GameStateDTO();
        oos.writeObject(gameState);
    }
}
*/