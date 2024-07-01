package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class ControlUtils {

    public static Player currentPlayer;
    public static List<Player> playersOnCurrentCity;
    public static GridPane controlGrid;
    public static List<City> citiesWithResearchStation = new ArrayList<>();
    public static int yellowCount;
    public static int blueCount;
    public static int redCount;
    public static int blackCount;

    public static void showOrHideControlsDependingOnCurrentPlayer(boolean disableHandCards) {
        boolean playerInPlayerList = false;
        for (Player player : GameController.players) {
            if (player.getName().toUpperCase().equals(GameApplication.player.name())) {
                playerInPlayerList = true;
                if (currentPlayer == player && !EventsUtils.governmentGrandPlayed && !EventsUtils.airliftPlayed){
                    showPhaseOneControls();
                } else {
                    disableAllOtherControls("");
                }
                enableOrDisableAllPlayerHandCards(player.getName().charAt(6) - '0', disableHandCards);
            } else {
                enableOrDisableAllPlayerHandCards(player.getName().charAt(6) - '0', true);
            }
        }
        if (!playerInPlayerList) {
            disableAllOtherControls("");
        }
    }

    public static void startTurn() {
        GameState.NUMBER_OF_ACTIONS = 4;
    }

    public static void showPhaseOneControls() {

        controlGrid = (GridPane) FXMLUtils.getNodeById("controlGrid", GameController._gamePane);
        controlGrid.getChildren().clear();

        Label actionsLabel = new Label("Actions: " + GameState.NUMBER_OF_ACTIONS);
        actionsLabel.setId("actCounter");
        actionsLabel.setFont(new Font("System Bold", 16));
        GridPane.setHalignment(actionsLabel, HPos.CENTER);
        controlGrid.add(actionsLabel, 0, 0);

        Button driveFerryButton = new Button("Drive/Ferry");
        driveFerryButton.setUserData("Drive/Ferry");
        GridPane.setMargin(driveFerryButton, new Insets(5, 5, 5, 5));
        driveFerryButton.setId("action1");
        Tooltip driveFerryTooltip = new Tooltip("Move to a city connected by a white line to the one you are in.");
        driveFerryButton.setTooltip(driveFerryTooltip);
        driveFerryButton.setOnAction(MovementActionUtils::onDriveFerryButtonClick);
        controlGrid.add(driveFerryButton, 1, 0);

        Button directFlightButton = new Button("Direct flight");
        directFlightButton.setUserData("Direct flight");
        GridPane.setMargin(directFlightButton, new Insets(5, 5, 5, 5));
        directFlightButton.setId("action2");
        Tooltip directFlightTooltip = new Tooltip("Discard a City card to move to the city named on the card.");
        directFlightButton.setTooltip(directFlightTooltip);
        directFlightButton.setOnAction(MovementActionUtils::onDirectFlightButtonClick);
        controlGrid.add(directFlightButton, 2, 0);

        ShowActionControlsUtil.showCharterFlightAndBuildResearchStationButtonsIfPlayerHasCurrentCityCard();
        ShowActionControlsUtil.showShuttleFlightButtonIfPlayerIsOnResearchStation();
        ShowActionControlsUtil.showTreatDiseaseButton();
        ShowActionControlsUtil.showShareKnowledgeButton();
        ShowActionControlsUtil.showDiscoverCureButton();

        Button endTurnButton = new Button("End Turn");
        GridPane.setMargin(endTurnButton, new Insets(5, 5, 5, 5));
        endTurnButton.setId("action9");
        endTurnButton.setOnAction(ControlUtils::endTurn);
        controlGrid.add(endTurnButton, 4, 1);
    }

    public static void passTurn() {
        if (!GameState.END_GAME){
            GameState.NUMBER_OF_TURNS++;
            setCurrentPlayerBasedOnNumberOfTurns();
            showOrHideControlsDependingOnCurrentPlayer(false);
            startTurn();
            GameApplication.client.sendGameState();
        }
    }

    public static void endTurn(Event event) {
        MovementActionUtils.startPhaseTwo();
    }

    public static void disableAllOtherControls(String except) {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node node : allActions) {
            Button b = (Button) node;
            if (!b.getId().equals(except)) {
                b.setDisable(true);
            }
        }
    }

    public static void enableOrDisableAllPlayerHandCards(int playerNumber, boolean disable) {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("playerCard" + playerNumber, GameController._gamePane);
        for (Node node : allActions) {
            if (node instanceof Button b)
                b.setDisable(disable);
        }
    }

    public static void enablePlayerAdjacentCityButtons(Player player) {
        List<String> adjacentCities = CityGraph.cityGraph.get(player.getCurrentCity());

        for (String city : adjacentCities) {
            Button b = (Button) FXMLUtils.getNodeById(city, GameController._gamePane);
            b.setDisable(false);
        }
    }
    public static void disableAllCityButtons() {
        for (List<String> ls : GameController.citiesColors) {
            Button b = (Button) FXMLUtils.getNodeById(ls.getFirst(), GameController._gamePane);
            b.setDisable(true);
        }
    }

    public static void enableAllCityButtonsExceptCurrentPlayer() {
        for (List<String> ls : GameController.citiesColors) {
            Button b = (Button) FXMLUtils.getNodeById(ls.getFirst(), GameController._gamePane);
            b.setDisable(currentPlayer.getCurrentCity().equals(ls.getFirst()));
        }
    }

    public static void enableAllCityButtons() {
        for (List<String> ls : GameController.citiesColors) {
            Button b = (Button) FXMLUtils.getNodeById(ls.getFirst(), GameController._gamePane);
                b.setDisable(false);
        }
    }

    public static void enableAllCitiesWithResearchStations() {
        for (City ls : GameController.cities) {
            Button b = (Button) FXMLUtils.getNodeById(ls.getName(), GameController._gamePane);
            if (citiesWithResearchStation.contains(ls) && !ControlUtils.currentPlayer.getCurrentCity().equals(ls.getName())){
                b.setDisable(false);
            }
        }
    }

    public static void setCurrentPlayerBasedOnNumberOfTurns() {
        currentPlayer = GameController.players.get(GameState.getCurrentPlayerNumber() - 1);
    }
}
