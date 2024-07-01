package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import hr.game.pandemic.model.roles.Researcher;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowActionControlsUtil {
    public static void showCharterFlightAndBuildResearchStationButtonsIfPlayerHasCurrentCityCard() {
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action3"));
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action5"));
        for (Card c : ControlUtils.currentPlayer.getHand()) {
            if (c.getName().equals(ControlUtils.currentPlayer.getCurrentCity())) {
                Button charterFlightButton = new Button("Charter flight");
                charterFlightButton.setUserData("Charter flight");
                GridPane.setMargin(charterFlightButton, new Insets(5, 5, 5, 5));
                charterFlightButton.setId("action3");
                Tooltip charterFlightTooltip = new Tooltip("Discard the City card that matches the city you are in to move to any city.");
                charterFlightButton.setTooltip(charterFlightTooltip);
                charterFlightButton.setOnAction(MovementActionUtils::onCharterFlightButtonClick);
                ControlUtils.controlGrid.add(charterFlightButton, 3, 0);

                City city = GameController.cities.stream()
                        .filter(city1 -> city1.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
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
                    ControlUtils.controlGrid.add(buildResearchStationButton, 0, 1);
                }
            }
        }
    }

    public static void showShuttleFlightButtonIfPlayerIsOnResearchStation() {
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action4"));
        boolean playerOnResearchStation = false;
        for (City c : ControlUtils.citiesWithResearchStation) {
            if (ControlUtils.currentPlayer.getCurrentCity().equals(c.getName())) {
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
            ControlUtils.controlGrid.add(shuttleFlightButton, 4, 0);
        }
    }

    public static void showTreatDiseaseButton() {
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action6"));
        City currentCity = GameController.cities.stream()
                .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
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
            ControlUtils.controlGrid.add(treatDiseaseButton, 1, 1);
        }
    }

    public static void showShareKnowledgeButton() {
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action7"));
        boolean twoPlayersOnSameCity = false;
        ControlUtils.playersOnCurrentCity = new ArrayList<>();
        List<Player> playersWithCurrentCityCardOrAreResearchers = new ArrayList<>();
        for (Player p : GameController.players) {
            if (!p.equals(ControlUtils.currentPlayer))
                if (p.getCurrentCity().equals(ControlUtils.currentPlayer.getCurrentCity())) {
                    twoPlayersOnSameCity = true;
                    ControlUtils.playersOnCurrentCity.add(p);
                    playersWithCurrentCityCardOrAreResearchers.add(p);
                }
        }
        playersWithCurrentCityCardOrAreResearchers.add(ControlUtils.currentPlayer);

        if (twoPlayersOnSameCity) {
            playersWithCurrentCityCardOrAreResearchers.removeIf(p -> p.getHand().stream().noneMatch(card -> card.getName().equals(ControlUtils.currentPlayer.getCurrentCity())) && !(p instanceof Researcher));
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
            ControlUtils.controlGrid.add(shareKnowledgeButton, 2, 1);
        }
    }

    public static void showDiscoverCureButton() {
        ControlUtils.controlGrid.getChildren().removeIf(node -> node.getId().equals("action8"));
        ControlUtils.yellowCount = 0;
        ControlUtils.blueCount = 0;
        ControlUtils.redCount = 0;
        ControlUtils.blackCount = 0;

        for (Card c : ControlUtils.currentPlayer.getHand()) {
            if (c instanceof CityCard card) {
                if (card.getColor().equals("yellow") && !   GameState.YELLOW_CURE)
                    ControlUtils.yellowCount++;
                if (card.getColor().equals("blue") && !GameState.BLUE_CURE)
                    ControlUtils.blueCount++;
                if (card.getColor().equals("red") && !GameState.RED_CURE)
                    ControlUtils.redCount++;
                if (card.getColor().equals("black") && !GameState.BLUE_CURE)
                    ControlUtils.blackCount++;
            }
        }

        Optional<City> currentCityOptional = GameController.cities.stream()
                .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                .findAny();

        if (currentCityOptional.isPresent()) {
            City currentCity = currentCityOptional.get();
            if (((ControlUtils.yellowCount > 4 || ControlUtils.blueCount > 4 || ControlUtils.redCount > 4 || ControlUtils.blackCount > 4) && currentCity.isResearchStation()) || GameState.EASY_MODE) {
                Button discoverACureButton = new Button("Discover a Cure");
                discoverACureButton.setWrapText(true);
                GridPane.setMargin(discoverACureButton, new Insets(5, 5, 5, 5));
                discoverACureButton.setId("action8");
                Tooltip discoverACureTooltip = new Tooltip("At any research station, discard 5 City cards of the same color from your hand to cure the disease of that color.");
                discoverACureButton.setTooltip(discoverACureTooltip);
                discoverACureButton.setOnAction(OtherActionUtils::onDiscoverACureButtonClick);
                ControlUtils.controlGrid.add(discoverACureButton, 3, 1);
            }
        }
    }
}
