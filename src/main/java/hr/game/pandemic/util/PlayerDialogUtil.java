package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PlayerDialogUtil {

    public static Player showPickYourselfOrAnotherPlayerDialog(Player eventPlayer) {
        ButtonType you = new ButtonType("Yourself");
        ButtonType another = new ButtonType("Another player");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Yourself or another");
        alert.setHeaderText("Choose to move yourself or another player.");
        alert.getButtonTypes().setAll(you, another, cancel);

        Player playerToMove = null;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == you) {
            playerToMove = eventPlayer;
        } else if (result.get() == another) {
            playerToMove = showPickAnotherPlayerDialog("Pick a player to move.", false);
        } else if (result.get() == cancel) {
            return null;
        }
        return playerToMove;
    }

    public static Player showPickAnotherPlayerDialog(String title, boolean currentCityOnly, List<Player> players) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(title);

            DialogUtils.pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            List<Player> playersToGet;
            if (players != null) {
                playersToGet = players;
            } else if (currentCityOnly) {
                playersToGet = ControlUtils.playersOnCurrentCity;
            } else {
                playersToGet = GameController.players;
            }
            for (int i = 0; i < playersToGet.size(); i++) {
                Player player = playersToGet.get(i);
                if (!player.equals(ControlUtils.currentPlayer)) {
                    Button tmpButton = new Button(player.getName().substring(0, 1).toUpperCase() + player.getName().substring(1));
                    GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
                    tmpButton.setUserData(player);
                    tmpButton.setCursor(Cursor.HAND);
                    tmpButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                    GridPane.setHalignment(tmpButton, HPos.CENTER);
                    DialogUtils.pickGrid.add(tmpButton, i%4, i/4);
                }
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedPlayer = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 2, 1);
                    if (pickedPlayer != null) {
                        return (Player) pickedPlayer.getUserData();
                    } else
                        return null;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return null;
                }

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Player showPickAnotherPlayerDialog(String title, boolean currentCityOnly) {
        return showPickAnotherPlayerDialog(title, currentCityOnly, null);
    }

    public static boolean showTakeCardConfirmationDialog(Player player) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Take card from " + player.getName() + "?");


        Optional<ButtonType> option = alert.showAndWait();
        return ButtonType.OK.equals(option.get());
    }

    public static boolean showTakeCardOrGiveCardDialog() {
        ButtonType take = new ButtonType("Take", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType give = new ButtonType("Give", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to give a card to a player or take a card from a player?", take, give);
        //alert.setTitle("Do you want to give a card to a player or take a card from a player?");

        Optional<ButtonType> option = alert.showAndWait();
        return take.equals(option.get());
    }
}
