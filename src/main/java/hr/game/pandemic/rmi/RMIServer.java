package hr.game.pandemic.rmi;

import hr.game.pandemic.jndi.ConfigurationKey;
import hr.game.pandemic.jndi.ConfigurationReader;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer {

    private static final int RANDOM_PORT_HINT = 0;

    public static void startRMIServer() {
        try {
            Integer rmiPort = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.RMI_PORT));
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            ChatRemoteService remoteService = new ChatRemoteService();
            RemoteService skeleton = (RemoteService) UnicastRemoteObject.exportObject(remoteService, RANDOM_PORT_HINT);
            registry.rebind(RemoteService.REMOTE_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
