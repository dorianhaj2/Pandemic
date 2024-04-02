package hr.game.pandemic.model;

public class EpidemicCard extends Card{
    private final String description;

    public EpidemicCard() {
        super("Epidemic");
        this.description = "1. Increase infection rate\n2. Draw a card off the bottom and infect\n3. Shuffle the Infection Discard Pile and playce it on top";
    }
}
