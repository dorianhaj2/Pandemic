package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Collections;
import java.util.List;

public class MovementActionUtils {

    public static void onDriveFerryButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            if (b.getText().equals("Drive/Ferry")) {
                ControlUtils.enablePlayerAdjacentCityButtons(ControlUtils.currentPlayer);
                b.setText("Cancel");
                ControlUtils.disableAllOtherControls(b.getId());
            } else {
                ControlUtils.disableAllCityButtons();
                b.setText("Drive/Ferry");
                ControlUtils.enableAllControls();
            }
        }
    }

    public static void moveCurrentPlayerToCity(String city) {
        ControlUtils.currentPlayer.setPreviousCity(ControlUtils.currentPlayer.getCurrentCity());
        ControlUtils.currentPlayer.setCurrentCity(city);
        FXMLUtils.updatePlayerLocation(ControlUtils.currentPlayer);
    }

    public static void actionDone() {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node n : allActions) {
            Button actionButton = (Button) n;
            if (actionButton.getText().equals("Cancel")) {
                actionButton.setText((String) actionButton.getUserData());
            }
        }
        GameState.NUMBER_OF_ACTIONS--;
        Label actionCounterLabel = (Label) FXMLUtils.getNodeById("actCounter", GameController._gamePane);
        actionCounterLabel.setText("Actions: " + GameState.NUMBER_OF_ACTIONS);
        ControlUtils.disableAllCityButtons();
        ControlUtils.enableAllControls();

        if (GameState.NUMBER_OF_ACTIONS == 0)
            startPhaseTwo();
        ControlUtils.showCharterFlightAndBuildResearchStationButtonsIfPlayerHasCurrentCityCard();
        ControlUtils.showShuttleFlightButtonIfPlayerIsOnResearchStation();
        ControlUtils.showTreatDiseaseButton();
        ControlUtils.showShareKnowledgeButton();
        ControlUtils.showDiscoverCureButton();
    }

    public static void startPhaseTwo() {
        boolean drawnEpidemic = GameController.playerDrawCard(ControlUtils.currentPlayer);
        if (drawnEpidemic && !GameState.END_GAME)
            onEpidemicCardDraw();
        boolean drawnSecondEpidemic = GameController.playerDrawCard(ControlUtils.currentPlayer);
        if (!GameState.END_GAME) {
            if (drawnSecondEpidemic) {
                if (drawnEpidemic) {
                    //dodati play event? dialog ako itko od igraca ima event
                }
                onEpidemicCardDraw();
            }
           GameController.checkIfPlayerHasTooManyCards(ControlUtils.currentPlayer);
            startPhaseThree();
        }
    }

    public static void onEpidemicCardDraw() {
        if (!GameState.END_GAME) {
            //1. Increase
            GameState.INFECTION_RATE++;
            if (GameState.INFECTION_RATE < 8) {
                FXMLUtils.moveInfectionRateToken();
            }
            //2. Infect
            CityCard drawnCard = GameController.infectionCardPile.getLast();
            GameController.infectionCardPile.removeLast();
            GameController.infectionDiscardPile.add(drawnCard);
            GameController.infectCity(drawnCard.getName(), drawnCard.getColor(), 3);
            //3. Intensify
            Collections.shuffle(GameController.infectionDiscardPile);
            GameController.infectionCardPile.addAll(GameController.infectionDiscardPile);
            GameController.infectionDiscardPile.clear();
        }

    }

    public static void startPhaseThree() {
        int amountOfInfectionCardsToDraw = (GameState.INFECTION_RATE/2) +  1;
        if (EventsUtils.oneQuietNightPlayed)
            amountOfInfectionCardsToDraw = 0;
        if (amountOfInfectionCardsToDraw == 1)
            amountOfInfectionCardsToDraw++;
        for (int i = 0; i < amountOfInfectionCardsToDraw; i++) {
            if (!GameState.END_GAME){
                GameController.drawInfectionCard();
                GameController.infectCity(GameController.infectionDiscardPile.getLast().getName(), GameController.infectionDiscardPile.getLast().getColor(), 1);

            }
        }
        ControlUtils.passTurn();
    }

    public static void onDirectFlightButtonClick(Event event) {
        CityCard discardedCity = (CityCard) DialogUtils.showPickACardDialog(ControlUtils.currentPlayer, "city", "Discard a city card to move to that city");
        if (discardedCity != null) {
            moveCurrentPlayerToCity(discardedCity.getName());
            GameController.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);
            actionDone();
        }
    }

    public static void onCharterFlightButtonClick(Event event) {
        CityCard discardedCity = (CityCard) DialogUtils.showPickACardDialog(ControlUtils.currentPlayer, ControlUtils.currentPlayer.getCurrentCity(), "Discard the City card that matches the city you are in to move to any city.");
        if (discardedCity != null) {
            ControlUtils.enableAllCityButtonsExceptCurrentPlayer();
            ControlUtils.disableAllOtherControls("");
            GameController.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);
        }
    }

    public static void onShuttleFlightButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            if (b.getText().equals("Shuttle flight")) {
                ControlUtils.enableAllCitiesWithResearchStations();
                b.setText("Cancel");
                ControlUtils.disableAllOtherControls(b.getId());
            } else {
                ControlUtils.disableAllCityButtons();
                b.setText("Shuttle flight");
                ControlUtils.enableAllControls();
            }
        }
    }
}
