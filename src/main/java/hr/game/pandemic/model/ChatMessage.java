package hr.game.pandemic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {
    private String playerName;
    private LocalDateTime localDateTime;
    private String message;
}
