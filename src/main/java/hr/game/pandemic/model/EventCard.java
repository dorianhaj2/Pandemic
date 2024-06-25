package hr.game.pandemic.model;

import java.io.Serializable;

public class EventCard extends Card implements Serializable {
    private String description;
    public EventCard() {
    }

    public EventCard(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getName() + ": " + description;
    }
}
