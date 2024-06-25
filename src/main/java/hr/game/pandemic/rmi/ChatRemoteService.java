package hr.game.pandemic.rmi;

import hr.game.pandemic.model.ChatMessage;

import java.rmi.RemoteException;
import java.util.List;

public class ChatRemoteService implements RemoteService {

    List<ChatMessage> chatMessageList;

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        chatMessageList.add(message);
    }

    @Override
    public List<ChatMessage> getAllChatMessages() throws RemoteException {
        return chatMessageList;
    }
}
