package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import hr.game.pandemic.model.roles.Researcher;
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

    public static void showPhaseOneControls(Player player) {
        currentPlayer = player;
        disableAllCityButtons();

        controlGrid = (GridPane) FXMLUtils.getNodeById("controlGrid", GameController._gamePane);
        GameState.NUMBER_OF_ACTIONS = 4;
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

        showCharterFlightAndBuildResearchStationButtonsIfPlayerHasCurrentCityCard();
        showShuttleFlightButtonIfPlayerIsOnResearchStation();
        showTreatDiseaseButton();
        showShareKnowledgeButton();
        showDiscoverCureButton();

        Button endTurnButton = new Button("End Turn");
        GridPane.setMargin(endTurnButton, new Insets(5, 5, 5, 5));
        endTurnButton.setId("action9");
        endTurnButton.setOnAction(ControlUtils::endTurn);
        controlGrid.add(endTurnButton, 4, 1);
    }

    public static void showCharterFlightAndBuildResearchStationButtonsIfPlayerHasCurrentCityCard() {
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action3"));
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action5"));
        for (Card c : currentPlayer.getHand()) {
            if (c.getName().equals(currentPlayer.getCurrentCity())) {
                Button charterFlightButton = new Button("Charter flight");
                charterFlightButton.setUserData("Charter flight");
                GridPane.setMargin(charterFlightButton, new Insets(5, 5, 5, 5));
                charterFlightButton.setId("action3");
                Tooltip charterFlightTooltip = new Tooltip("Discard the City card that matches the city you are in to move to any city.");
                charterFlightButton.setTooltip(charterFlightTooltip);
                charterFlightButton.setOnAction(MovementActionUtils::onCharterFlightButtonClick);
                controlGrid.add(charterFlightButton, 3, 0);

                City city = GameController.cities.stream()
                        .filter(city1 -> city1.getName().equals(currentPlayer.getCurrentCity()))
                        .findAny()
                        .orElse(null);
                if (!city.isResearchStation()) {
                    Button buildResearchStationButton = new Button("Build research station");
                    buildResearchStationButton.setUserData("Build research station");
                    buildResearchStationButton.setWrapText(true);
                    GridPane.setMargin(buildResearchStationButton, new Insets(5, 5, 5, 5));
                    buildResearchStationButton.setId("action5");
                    Tooltip buildResearchStationTooltip = new Tooltip("Discard the City card that matches the city you are in to place a research station there.");
                    buildResearchStationButton.setTooltip(buildResearchStationTooltip);
                    buildResearchStationButton.setOnAction(OtherActionUtils::onBuildResearchStationButtonClick);
                    controlGrid.add(buildResearchStationButton, 0, 1);
                }
            }
        }
    }

    public static void showShuttleFlightButtonIfPlayerIsOnResearchStation() {
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action4"));
        boolean playerOnResearchStation = false;
        for (City c : citiesWithResearchStation) {
            if (currentPlayer.getCurrentCity().equals(c.getName())) {
                playerOnResearchStation = true;
                break;
            }
        }
        if (playerOnResearchStation) {
            Button shuttleFlightButton = new Button("Shuttle flight");
            shuttleFlightButton.setUserData("Shuttle flight");
            GridPane.setMargin(shuttleFlightButton, new Insets(5, 5, 5, 5));
            shuttleFlightButton.setId("action4");
            Tooltip shuttleFlightTooltip = new Tooltip("Move from a city with a research station to any other city that has a research station.");
            shuttleFlightButton.setTooltip(shuttleFlightTooltip);
            shuttleFlightButton.setOnAction(MovementActionUtils::onShuttleFlightButtonClick);
            controlGrid.add(shuttleFlightButton, 4, 0);
        }
    }

    public static void showTreatDiseaseButton() {
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action6"));
        City currentCity = GameController.cities.stream()
                .filter(c -> c.getName().equals(currentPlayer.getCurrentCity()))
                .findAny()
                .orElse(null);

        if (!currentCity.getDiseases().isEmpty()) {
            Button treatDiseaseButton = new Button("Treat disease");
            treatDiseaseButton.setUserData("Treat disease");
            GridPane.setMargin(treatDiseaseButton, new Insets(5, 5, 5, 5));
            treatDiseaseButton.setId("action6");
            Tooltip treatDiseaseTooltip = new Tooltip("Remove 1 disease cube from the city you are in, placing it in the cube supply " +
                    "next to the board. If this disease color has been cured " +
                    ", remove all cubes of that color from the city you are in.");
            treatDiseaseButton.setTooltip(treatDiseaseTooltip);
            treatDiseaseButton.setOnAction(OtherActionUtils::onTreatDiseaseButtonClick);
            controlGrid.add(treatDiseaseButton, 1, 1);
        }
    }

    public static void showShareKnowledgeButton() {
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action7"));
        boolean twoPlayersOnSameCity = false;
        playersOnCurrentCity = new ArrayList<>();
        List<Player> playersWithCurrentCityCardOrAreResearchers = new ArrayList<>();
        for (Player p : GameController.players) {
            if (!p.equals(currentPlayer))
                if (p.getCurrentCity().equals(currentPlayer.getCurrentCity())) {
                    twoPlayersOnSameCity = true;
                    playersOnCurrentCity.add(p);
                    playersWithCurrentCityCardOrAreResearchers.add(p);
                }
        }
        playersWithCurrentCityCardOrAreResearchers.add(currentPlayer);

        if (twoPlayersOnSameCity) {
            playersWithCurrentCityCardOrAreResearchers.removeIf(p -> p.getHand().stream().noneMatch(card -> card.getName().equals(currentPlayer.getCurrentCity())) && !(p instanceof Researcher));
        }

        if (!playersWithCurrentCityCardOrAreResearchers.isEmpty()) {
            Button shareKnowledgeButton = new Button("Share Knowledge");
            shareKnowledgeButton.setUserData(playersWithCurrentCityCardOrAreResearchers);
            shareKnowledgeButton.setWrapText(true);
            GridPane.setMargin(shareKnowledgeButton, new Insets(5, 5, 5, 5));
            shareKnowledgeButton.setId("action7");
            Tooltip shareKnowledgeTooltip = new Tooltip("You can do this action in two ways:\n" +
                    "give the City card that matches the city you are in to another player, or\n" +
                    "take the City card that matches the city you are in from another player.");
            shareKnowledgeButton.setTooltip(shareKnowledgeTooltip);
            shareKnowledgeButton.setOnAction(OtherActionUtils::onShareKnowledgeButtonClick);
            controlGrid.add(shareKnowledgeButton, 2, 1);
        }
    }

    public static void showDiscoverCureButton() {
        controlGrid.getChildren().removeIf(node -> node.getId().equals("action8"));
        yellowCount = 0;
        blueCount = 0;
        redCount = 0;
        blackCount = 0;

        for (Card c : currentPlayer.getHand()) {
            if (c instanceof CityCard card) {
                if (card.getColor().equals("yellow") && !   GameState.YELLOW_CURE)
                    yellowCount++;
                if (card.getColor().equals("blue") && !GameState.BLUE_CURE)
                    blueCount++;
                if (card.getColor().equals("red") && !GameState.RED_CURE)
                    redCount++;
                if (card.getColor().equals("black") && !GameState.BLUE_CURE)
                    blackCount++;
            }
        }

        City currentCity = GameController.cities.stream()
                .filter(c -> c.getName().equals(currentPlayer.getCurrentCity()))
                .findAny()
                .orElse(null);

        if ((yellowCount > 4 || blueCount > 4 || redCount > 4 || blackCount > 4) && currentCity.isResearchStation()) {
            Button discoverACureButton = new Button("Discover a Cure");
            discoverACureButton.setWrapText(true);
            GridPane.setMargin(discoverACureButton, new Insets(5, 5, 5, 5));
            discoverACureButton.setId("action8");
            Tooltip discoverACureTooltip = new Tooltip("At any research station, discard 5 City cards of the same color from your hand to cure the disease of that color.");
            discoverACureButton.setTooltip(discoverACureTooltip);
            discoverACureButton.setOnAction(OtherActionUtils::onDiscoverACureButtonClick);
            controlGrid.add(discoverACureButton, 3, 1);
        }
    }

    public static void passTurn() {
        if (!GameState.END_GAME){
            GameState.NUMBER_OF_TURNS++;
            showPhaseOneControls(GameController.players.get(GameState.getCurrentPlayerNumber() - 1));
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

    public static void disableAllPlayerHandCards() {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("playerCard", GameController._gamePane);
        for (Node node : allActions) {
            if (node instanceof Button b)
                b.setDisable(true);
        }
    }

    public static void enableAllPlayerHandCards() {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("playerCard", GameController._gamePane);
        for (Node node : allActions) {
            if (node instanceof Button b)
                b.setDisable(false);
        }
    }

    public static void enableAllControls() {
        List<Node> allActions = FXMLUtils.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node node : allActions) {
            Button b = (Button) node;
            b.setDisable(false);
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
            if (!currentPlayer.getCurrentCity().equals(b.getId()))
                b.setDisable(false);
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
}
