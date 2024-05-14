package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.EventCard;
import hr.game.pandemic.model.Player;
import javafx.event.Event;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsUtils {

    private static boolean cardPlayed;
    public static boolean airliftPlayed = false;
    public static boolean governmentGrandPlayed = false;

    public static void onEventCardClick(Event event) {
        if (event.getSource() instanceof Button b) {
            int playerNumber = b.getId().charAt(6) - '0';
            playerNumber--;
            onEventCardPlay((EventCard) b.getUserData(), GameController.players.get(playerNumber));
        }
    }
    public static void onEventCardPlay(EventCard eventCard, Player eventPlayer) {
        cardPlayed = false;

        if (eventCard.getName().equals("Forecast"))
            onForecastEventPlay();
        else if (eventCard.getName().equals("Airlift"))
            onAirliftEventPlay(eventPlayer);
        else if (eventCard.getName().equals("Government Grant"))
            onGovernmentGrantEventPlay();

        if (cardPlayed)
            GameController.playerDiscardCard(eventPlayer, eventCard);
    }

    public static void onForecastEventPlay() {
        int k = 1;
        Player tmpPlayer = new Player("tmp");
        List<CityCard> rearrangedCards = new ArrayList<>();
        List<CityCard> removedCards = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            CityCard lastCard = GameController.infectionCardPile.getLast();
            tmpPlayer.addCardToHand(lastCard);
            GameController.infectionCardPile.removeLast();
            removedCards.add(lastCard);
        }
        Collections.reverse(removedCards);

        while (k <= 6) {
            CityCard pickedCard = (CityCard) DialogUtils.showPickACardDialog(tmpPlayer, "city", "Pick card number 1. (1 - bottom, 6 - top)");
            if (pickedCard != null) {
                rearrangedCards.add(pickedCard);
                tmpPlayer.removeCardFromHand(pickedCard);
                k++;
            } else {
                GameController.infectionCardPile.addAll(removedCards);
                return;
            }
        }
        cardPlayed = true;

        GameController.infectionCardPile.addAll(rearrangedCards);
    }

    public static void onAirliftEventPlay(Player eventPlayer) {
        Player pickedPlayer = DialogUtils.showPickYourselfOrAnotherPlayerDialog(eventPlayer);
        if (pickedPlayer != null) {
            airliftPlayed = true;
            ControlUtils.currentPlayer = pickedPlayer;
            ControlUtils.enableAllCityButtonsExceptCurrentPlayer();
            ControlUtils.disableAllPlayerHandCards();
            ControlUtils.disableAllOtherControls("");
            cardPlayed = true;
        }
    }

    public static void onResilientPopulationPlay() {

    }

    public static void onOneQuietNightEventPlay() {

    }

    public static void onGovernmentGrantEventPlay() {
        governmentGrandPlayed = true;
        ControlUtils.enableAllCityButtons();
        ControlUtils.disableAllPlayerHandCards();
        ControlUtils.disableAllOtherControls("");
        cardPlayed = true;
    }
}
