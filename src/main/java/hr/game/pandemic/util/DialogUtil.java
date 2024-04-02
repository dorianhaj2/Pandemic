package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.dialogs.NewGameDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class DialogUtil {

    public static boolean showNewGameDialog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("new-game-dialog.fxml"));
            DialogPane newGameDialogPane = fxmlLoader.load();

            NewGameDialog controller = fxmlLoader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(newGameDialogPane);
            dialog.setTitle("New game");

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    controller.setDifficulty();
                    controller.setNumOfPlayers();
                    return true;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return false;
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void showDriveFerryInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Drive/Ferry action");
        alert.setHeaderText(null);
        alert.setContentText("Click on a city connected by a white line to the one you are in or cancel in the bottom right to cancel.");
        alert.showAndWait();
    }
}
