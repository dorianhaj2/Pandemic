package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.roles.Medic;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            }
        }
    }

    public static void moveCurrentPlayerToCity(String city) {
        ControlUtils.currentPlayer.setCurrentCity(city);
        Optional<City> currentCityOptional = GameController.cities.stream()
                .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                .findAny();
        if (ControlUtils.currentPlayer instanceof Medic) {
            if (currentCityOptional.isPresent()) {
                City currentCity = currentCityOptional.get();
                if (GameState.RED_CURE)
                    currentCity.cureAllOfDisease("red");
                if (GameState.YELLOW_CURE)
                    currentCity.cureAllOfDisease("yellow");
                if (GameState.BLUE_CURE)
                    currentCity.cureAllOfDisease("blue");
                if (GameState.BLACK_CURE)
                    currentCity.cureAllOfDisease("black");
                FXMLUtils.refreshCityDiseases(currentCity);
            }
        }
        FXMLUtils.updatePlayerLocation(ControlUtils.currentPlayer);
    }

    public static void actionDone() {
        if (!GameState.END_GAME) {
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

            ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            GameApplication.client.sendGameState();

            if (GameState.NUMBER_OF_ACTIONS == 0)
                startPhaseTwo();
        }
    }

    public static void startPhaseTwo() {
        if (!GameState.END_GAME) {
            boolean drawnEpidemic = DrawDiscardUtil.playerDrawCard(ControlUtils.currentPlayer);
            Alert epidemicCardDrawnAlert = new Alert(Alert.AlertType.INFORMATION);
            epidemicCardDrawnAlert.setTitle(GameApplication.player.name() + " - Epidemic card!");
            epidemicCardDrawnAlert.setHeaderText("You drew and epidemic card!");
            if (drawnEpidemic) {
                epidemicCardDrawnAlert.showAndWait();
                onEpidemicCardDraw();
            }
            boolean drawnSecondEpidemic = DrawDiscardUtil.playerDrawCard(ControlUtils.currentPlayer);

            if (drawnSecondEpidemic) {
                epidemicCardDrawnAlert.showAndWait();
                onEpidemicCardDraw();
            }
            DrawDiscardUtil.checkIfPlayerHasTooManyCards(ControlUtils.currentPlayer);
            startPhaseThree();
        }
    }

    public static void onEpidemicCardDraw() {
        if (!GameState.END_GAME) {
            //1. Increase
            if (GameState.INFECTION_RATE < 7) {
                GameState.INFECTION_RATE++;
                FXMLUtils.moveInfectionRateToken();
            }
            //2. Infect
            CityCard drawnCard = GameController.infectionCardPile.getLast();
            GameController.infectionCardPile.removeLast();
            GameController.infectionDiscardPile.add(drawnCard);
            InfectUtil.infectCity(drawnCard.getName(), drawnCard.getColor(), 3);
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
                DrawDiscardUtil.drawInfectionCard();
                InfectUtil.infectCity(GameController.infectionDiscardPile.getLast().getName(), GameController.infectionDiscardPile.getLast().getColor(), 1);
            }
        }
        ControlUtils.passTurn();
    }

    public static void onDirectFlightButtonClick(Event event) {
        CityCard discardedCity = (CityCard) CardDialogUtils.showPickACardDialog(ControlUtils.currentPlayer, "city", "Discard a city card to move to that city");
        if (discardedCity != null) {
            moveCurrentPlayerToCity(discardedCity.getName());
            DrawDiscardUtil.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);
            actionDone();
        }
    }

    public static void onCharterFlightButtonClick(Event event) {
        CityCard discardedCity = (CityCard) CardDialogUtils.showPickACardDialog(ControlUtils.currentPlayer, ControlUtils.currentPlayer.getCurrentCity(), "Discard the City card that matches the city you are in to move to any city.");
        if (discardedCity != null) {
            ControlUtils.enableAllCityButtonsExceptCurrentPlayer();
            ControlUtils.disableAllOtherControls("");
            DrawDiscardUtil.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);
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
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            }
        }
    }

    public static void clickCity(Button b) {
            if (EventsUtils.governmentGrandPlayed){
                Optional<City> newResearchStationCityOptional = GameController.cities.stream()
                        .filter(c -> c.getName().equals(b.getId()))
                        .findAny();
                if (newResearchStationCityOptional.isPresent()) {
                    City newResearchStationCity = newResearchStationCityOptional.get();
                    newResearchStationCity.setResearchStation(true);
                    ControlUtils.citiesWithResearchStation.add(newResearchStationCity);
                    Button cityButton = (Button) FXMLUtils.getNodeById(newResearchStationCity.getName(), GameController._gamePane);
                    cityButton.getStyleClass().add("research_station");
                    EventsUtils.governmentGrandPlayed = false;
                    ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
                    GameApplication.client.sendGameState();
                }
                ControlUtils.disableAllCityButtons();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            } else if (EventsUtils.airliftPlayed) {
                moveCurrentPlayerToCity(b.getId());
                ControlUtils.setCurrentPlayerBasedOnNumberOfTurns();
                EventsUtils.airliftPlayed = false;
                ControlUtils.disableAllCityButtons();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
                GameApplication.client.sendGameState();
            } else {
                moveCurrentPlayerToCity(b.getId());
                ControlUtils.disableAllCityButtons();
                actionDone();
            }
    }

}