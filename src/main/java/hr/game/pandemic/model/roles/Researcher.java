package hr.game.pandemic.model.roles;

import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.Player;

import java.io.Serializable;
import java.util.List;

public class Researcher extends Player implements Serializable {
    public Researcher() {
    }

    public Researcher(String name) {
        super(name);
    }

    public Researcher(String name, List<Card> hand, String currentCity) {
        super(name, hand, currentCity);
    }

    public Researcher(String name, List<Card> hand) {
        super(name, hand);
    }

    public String getRoleName() {
        return "Researcher";
    }
}
