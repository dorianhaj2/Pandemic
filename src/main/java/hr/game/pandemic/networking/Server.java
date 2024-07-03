package hr.game.pandemic.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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

                    ClientHandlerThread clientHandler = new ClientHandlerThread(socket);
                    Thread thread = new Thread(clientHandler);

                    thread.start();
                }
            } catch (SocketException se) {
                System.out.println("Server closed");
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
