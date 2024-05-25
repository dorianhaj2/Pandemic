package hr.game.pandemic.model.roles;

import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.Player;

import java.util.List;

public class QuarantineSpecialist extends Player {
    public QuarantineSpecialist() {
    }

    public QuarantineSpecialist(String name) {
        super(name);
    }

    public QuarantineSpecialist(String name, List<Card> hand, String currentCity) {
        super(name, hand, currentCity);
    }

    public QuarantineSpecialist(String name, List<Card> hand) {
        super(name, hand);
    }

    public String getRoleName() {
        return "Quarantine Specialist";
    }}
