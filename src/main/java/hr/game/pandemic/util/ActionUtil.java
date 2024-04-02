package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.List;

public class ActionUtil {

    public static void showActions() {

    }
    public static void onDriveFerryButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            if (b.getText().equals("Drive/Ferry")) {
                ControlUtil.enablePlayerAdjacentCityButtons(ControlUtil.currentPlayer);
                DialogUtil.showDriveFerryInfo();
                b.setText("Cancel");
                ControlUtil.disableAllOtherControls(b.getId());
            } else {
                ControlUtil.disableAllCityButtons();
                b.setText("Drive/Ferry");
                ControlUtil.enableAllControls();
            }
        }
    }

    public static void driveFerryCityClicked(Player player, Button cityButton) {

    }

    public static void moveCurrentPlayerToCity(String city) {
        ControlUtil.currentPlayer.setPreviousCity(ControlUtil.currentPlayer.getCurrentCity());
        ControlUtil.currentPlayer.setCurrentCity(city);
        FXMLUtil.updatePlayerLocation(ControlUtil.currentPlayer);
    }

    public static void actionDone() {
        List<Node> allActions = FXMLUtil.getNodesByIdStartsWith("action", GameController._gamePane);

        for (Node n : allActions) {
            Button actionButton = (Button) n;
            if (actionButton.getText().equals("Cancel")) {
                actionButton.setText((String) actionButton.getUserData());
            }
        }
        GameState.NUMBER_OF_ACTIONS--;
        ControlUtil.disableAllCityButtons();
        ControlUtil.enableAllControls();

        if (GameState.NUMBER_OF_ACTIONS == 0)
            startPhaseTwo();
    }

    public static void startPhaseTwo() {

    }

    public static void onEpidemicCardDraw() {

    }
}
