package hr.game.pandemic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCard extends Card implements Serializable {
    private String description;

    public EventCard(String name, String description) {
        super(name);
        this.description = description;
    }
}
