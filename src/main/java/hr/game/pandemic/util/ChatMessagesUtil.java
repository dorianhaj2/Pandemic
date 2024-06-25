package hr.game.pandemic.util;

import hr.game.pandemic.model.ChatMessage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatMessagesUtil {
    public static String convertChatMessagesToString(List<ChatMessage> chatMessageList) {
        StringBuilder messagesBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for(ChatMessage message : chatMessageList) {
            messagesBuilder.append(message.getPlayerName());
            messagesBuilder.append(" (");
            messagesBuilder.append(formatter.format(message.getLocalDateTime()));
            messagesBuilder.append("): ");
            messagesBuilder.append(message.getMessage());
            messagesBuilder.append("\n");
        }

        return messagesBuilder.toString();
    }
}
