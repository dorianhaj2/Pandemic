package hr.game.pandemic.rmi;

import hr.game.pandemic.model.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteService extends Remote {

    String REMOTE_OBJECT_NAME = "hr.game.pandemic.rmi.service";

    void sendMessage(ChatMessage message) throws RemoteException;

    List<ChatMessage> getAllChatMessages() throws RemoteException;

}
