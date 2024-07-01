package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.EventCard;
import hr.game.pandemic.model.Player;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EventsUtils {

    private static boolean cardPlayed;
    public static boolean airliftPlayed = false;
    public static boolean governmentGrandPlayed = false;
    public static boolean oneQuietNightPlayed = false;

    public static void onEventCardClick(Event event) {
        if (event.getSource() instanceof Button b) {
            int playerIndex = b.getId().charAt(10) - '0' - 1;
            onEventCardPlay((EventCard) b.getUserData(), GameController.players.get(playerIndex));
        }
    }
    public static void onEventCardPlay(EventCard eventCard, Player eventPlayer) {
        cardPlayed = false;

        switch (eventCard.getName()) {
            case "Forecast" -> onForecastEventPlay();
            case "Airlift" -> onAirliftEventPlay(eventPlayer);
            case "Government Grant" -> onGovernmentGrantEventPlay();
            case "Resilient Population" -> onResilientPopulationPlay();
            case "One Quiet Night" -> onOneQuietNightEventPlay();
        }

        if (cardPlayed) {
            DrawDiscardUtil.playerDiscardCard(eventPlayer, eventCard);
            GameApplication.client.sendGameState();
        }


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
            CityCard pickedCard = (CityCard) CardDialogUtils.showPickACardDialog(tmpPlayer, "city", "Pick card number 1. (1 - bottom, 6 - top)");
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
        Player pickedPlayer = PlayerDialogUtil.showPickYourselfOrAnotherPlayerDialog(eventPlayer);
        if (pickedPlayer != null) {
            airliftPlayed = true;
            ControlUtils.currentPlayer = pickedPlayer;
            ControlUtils.enableAllCityButtonsExceptCurrentPlayer();
            ControlUtils.showOrHideControlsDependingOnCurrentPlayer(true);
            ControlUtils.disableAllOtherControls("");
            cardPlayed = true;
        }
    }

    public static void onResilientPopulationPlay() {
        CityCard cardToRemove = DialogUtils.showPickAnInfectionCardDialog();
        if (cardToRemove != null) {
            GameController.infectionDiscardPile.remove(cardToRemove);
            RefreshFXMLUtils.refreshInfectionDiscardPile();
            cardPlayed = true;
        }
    }

    public static void onOneQuietNightEventPlay() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("One Quiet Night");
        alert.setHeaderText("Are you sure you want to play this event card?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            oneQuietNightPlayed = true;
            cardPlayed = true;
        }
    }

    public static void onGovernmentGrantEventPlay() {
        governmentGrandPlayed = true;
        ControlUtils.enableAllCityButtons();
        ControlUtils.showOrHideControlsDependingOnCurrentPlayer(true);
        ControlUtils.disableAllOtherControls("");
        cardPlayed = true;
    }
}
