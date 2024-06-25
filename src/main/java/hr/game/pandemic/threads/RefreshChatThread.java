package hr.game.pandemic.threads;

import hr.game.pandemic.jndi.ConfigurationKey;
import hr.game.pandemic.jndi.ConfigurationReader;
import hr.game.pandemic.rmi.RemoteService;
import hr.game.pandemic.util.ChatMessagesUtil;
import javafx.scene.control.TextArea;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RefreshChatThread implements Runnable{
    private TextArea textArea;

    public RefreshChatThread(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void run() {

        RemoteService service;
        Registry registry = null;
        try {
            Integer rmiPort = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.RMI_PORT));
            String rmiHost = ConfigurationReader.getValue(ConfigurationKey.RMI_HOST);
            registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
            service = (RemoteService) registry.lookup(RemoteService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        while(true) {
            try {
                Thread.sleep(1000);
                textArea.clear();
                textArea.appendText(ChatMessagesUtil.convertChatMessagesToString(service.getAllChatMessages()));
                textArea.setScrollTop(Double.MAX_VALUE);
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
