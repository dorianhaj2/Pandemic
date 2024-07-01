package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;

public class DiseaseDialogUtil {
    public static String showPickADiseaseToTreat() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Pick a disease color you want to treat.");

            DialogUtils.pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            City currentCity = GameController.cities.stream()
                    .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                    .findAny()
                    .orElse(null);
            int k = 0;
            if (currentCity.getDiseases().contains("yellow")) {
                Button yellowButton = new Button("Yellow");
                yellowButton.getStyleClass().add("cardYellow");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("blue")) {
                Button yellowButton = new Button("Blue");
                yellowButton.getStyleClass().add("cardBlue");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("red")) {
                Button yellowButton = new Button("Red");
                yellowButton.getStyleClass().add("cardRed");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("black")) {
                Button yellowButton = new Button("Black");
                yellowButton.getStyleClass().add("cardBlack");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedColor = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 2, 1);
                    if (pickedColor != null) {
                        return pickedColor.getText().toLowerCase();
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

    public static String showPickAColorDialog(String colorsToShow) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Pick a color.");

            DialogUtils.pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            int k = 0;
            if (colorsToShow.contains("yellow")) {
                Button yellowButton = new Button("Yellow");
                yellowButton.getStyleClass().add("cardYellow");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("blue")) {
                Button yellowButton = new Button("Blue");
                yellowButton.getStyleClass().add("cardBlue");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("red")) {
                Button yellowButton = new Button("Red");
                yellowButton.getStyleClass().add("cardRed");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("black")) {
                Button yellowButton = new Button("Black");
                yellowButton.getStyleClass().add("cardBlack");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                DialogUtils.pickGrid.add(yellowButton, k%4, k/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedColor = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 2, 1);
                    if (pickedColor != null) {
                        return pickedColor.getText().toLowerCase();
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
}
