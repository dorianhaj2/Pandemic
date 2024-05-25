package hr.game.pandemic.model.roles;

import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.Player;

import java.util.List;

public class Scientist extends Player {
    public Scientist() {
    }

    public Scientist(String name) {
        super(name);
    }

    public Scientist(String name, List<Card> hand, String currentCity) {
        super(name, hand, currentCity);
    }

    public Scientist(String name, List<Card> hand) {
        super(name, hand);
    }

    public String getRoleName() {
        return "Scientist";
    }
}
