package hr.game.pandemic.model.roles;

import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.Player;

import java.io.Serializable;
import java.util.List;

public class Medic extends Player implements Serializable {

    public Medic() {
    }

    public Medic(String name) {
        super(name);
    }

    public Medic(String name, List<Card> hand, String currentCity) {
        super(name, hand, currentCity);
    }

    public Medic(String name, List<Card> hand) {
        super(name, hand);
    }

    public String getRoleName() {
        return "Medic";
    }

}
