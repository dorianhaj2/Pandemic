package hr.game.pandemic.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {

                    Socket socket = serverSocket.accept();
                    System.out.println("A new client has connected");

                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);

                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
public class Server extends Thread {

    public static final int CLIENT_PORT = 4446;
    public static final String HOST = "localhost";
    public static final String GROUP = "230.0.0.1";

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            byte[] bufferInput = new byte[64];
            ByteArrayInputStream bais = new ByteArrayInputStream(bufferInput);


            while (true) {
                InetAddress groupAddress = InetAddress.getByName(GROUP);

                byte[] bufferReceive = new byte[64];
                DatagramPacket receivedPacket = new DatagramPacket(bufferReceive, bufferReceive.length, groupAddress, CLIENT_PORT);
                serverSocket.receive(receivedPacket);

                byte[] gameStateInBytes = new byte[64];
                bais.read(gameStateInBytes, 0, gameStateInBytes.length);
                ObjectInputStream ois = new ObjectInputStream(bais);
                GameStateDTO receivedGameStateDTO = (GameStateDTO) ois.readObject();
                receivedGameStateDTO.setGameState();
                //nez sta radim
                GameStateDTO gameStateDTO = new GameStateDTO();
                oos.writeObject(gameStateDTO);

                byte[] bufferSend = baos.toByteArray();
                DatagramPacket sentPacket = new DatagramPacket(bufferSend, bufferSend.length, groupAddress, CLIENT_PORT);
                serverSocket.send(sentPacket);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}*/
