package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.List;

public class ControlUtil {

    public static Player currentPlayer;


    public static void showPhaseOneControls(Player player) {
        currentPlayer = player;
        disableAllCityButtons();

        GridPane controlGrid = (GridPane) FXMLUtil.getNodeById("controlGrid", GameController._gamePane);
        GameState.NUMBER_OF_ACTIONS = 4;

        Label actionsLabel = new Label("Actions: " + GameState.NUMBER_OF_ACTIONS);
        actionsLabel.setId("actCounter");
        actionsLabel.setFont(new Font("System Bold", 16));
        GridPane.setHalignment(actionsLabel, HPos.CENTER);
        controlGrid.add(actionsLabel, 0, 0);

        Button driveFerryButton = new Button("Drive/Ferry");
        driveFerryButton.setUserData("Drive/Ferry");
        driveFerryButton.setId("action1");
        Tooltip driveFerryTooltip = new Tooltip("Move to a city connected by a white line to the one you are in.");
        driveFerryButton.setTooltip(driveFerryTooltip);
        driveFerryButton.setOnAction(ActionUtil::onDriveFerryButtonClick);
        controlGrid.add(driveFerryButton, 1, 0);

        Button directFlightButton = new Button("Direct flight");
        directFlightButton.setUserData("Drive/Ferry");
        directFlightButton.setId("action2");

    }

    public static void disableAllOtherControls(String except) {
        List<Node> allActions = FXMLUtil.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node node : allActions) {
            Button b = (Button) node;
            if (!b.getId().equals(except)) {
                b.setDisable(true);
            }
        }
    }

    public static void enableAllControls() {
        List<Node> allActions = FXMLUtil.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node node : allActions) {
            Button b = (Button) node;
            b.setDisable(false);
        }
    }

    public static void enablePlayerAdjacentCityButtons(Player player) {
        List<String> adjacentCities = CityGraph.cityGraph.get(player.getCurrentCity());

        for (String city : adjacentCities) {
            Button b = (Button) FXMLUtil.getNodeById(city, GameController._gamePane);
            b.setDisable(false);
        }
    }
    public static void disableAllCityButtons() {
        for (List<String> ls : GameController.citiesColors) {
            Button b = (Button) FXMLUtil.getNodeById(ls.get(0), GameController._gamePane);
            b.setDisable(true);
        }
    }
}
