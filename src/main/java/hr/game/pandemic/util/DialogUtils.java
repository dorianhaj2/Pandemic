package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.dialogs.NewGameDialog;
import hr.game.pandemic.model.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;

public class DialogUtils {

    public static GridPane pickGrid;

    public static boolean showNewGameDialog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/new-game-dialog.fxml"));
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
                    controller.setCheat();
                    return true;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return false;
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Node getNodeByCoordinate(GridPane gridLayout, int row, int column) {
        for (Node node : gridLayout.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column){
                return node;
            }
        }
        return null;
    }

    public static City showPickAResearchStationDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("All research stations used! Pick a city to remove a research station from.");

            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            for (int i = 0; i < ControlUtils.citiesWithResearchStation.size(); i++) {
                City tmpCity = ControlUtils.citiesWithResearchStation.get(i);
                Button tmpButton = new Button(tmpCity.getName().substring(0, 1).toUpperCase() + tmpCity.getName().substring(1));
                GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
                tmpButton.setUserData(tmpCity);
                tmpButton.setId(tmpCity.getName() + "ResearchStation");
                tmpButton.setCursor(Cursor.HAND);
                tmpButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(tmpButton, HPos.CENTER);

                tmpButton.getStyleClass().add("card" + tmpCity.getColor().substring(0, 1).toUpperCase() + tmpCity.getColor().substring(1));

                pickGrid.add(tmpButton, i%4, i/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedCity = (Button) getNodeByCoordinate(pickGrid, 2, 1);
                    if (pickedCity != null) {
                        return (City) pickedCity.getUserData();
                    } else
                        return null;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return null;
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CityCard showPickAnInfectionCardDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-an-infection-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Pick an infection card to remove from the infection discard pile.");

            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            for (int i = 0; i < GameController.infectionDiscardPile.size(); i++) {
                CityCard tmpCard = GameController.infectionDiscardPile.get(i);
                Button tmpButton = new Button(tmpCard.getName().substring(0, 1).toUpperCase() + tmpCard.getName().substring(1));
                GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
                tmpButton.setUserData(tmpCard);
                tmpButton.setId(tmpCard.getName());
                tmpButton.setCursor(Cursor.HAND);
                tmpButton.setOnAction(CardDialogUtils::pickAnInfectionCardButtonClick);
                GridPane.setHalignment(tmpButton, HPos.CENTER);

                tmpButton.getStyleClass().add("card" + tmpCard.getColor().substring(0, 1).toUpperCase() + tmpCard.getColor().substring(1));

                pickGrid.add(tmpButton, i%8, i/8);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedCard = (Button) getNodeByCoordinate(pickGrid, 6, 1);
                    if (pickedCard != null) {
                        return (CityCard) pickedCard.getUserData();
                    } else
                        return null;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return null;
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showEpidemicCardDrawn() {

    }
}

