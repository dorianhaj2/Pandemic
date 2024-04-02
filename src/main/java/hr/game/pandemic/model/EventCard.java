package hr.game.pandemic.model;

public class EventCard extends Card{
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
