package hr.game.pandemic.model;

import java.io.Serializable;

public class EpidemicCard extends Card implements Serializable {
    private final String description;

    public EpidemicCard() {
        super("Epidemic");
        this.description = "1. Increase infection rate\n2. Draw a card off the bottom and infect\n3. Shuffle the Infection Discard Pile and playce it on top";
    }
}
