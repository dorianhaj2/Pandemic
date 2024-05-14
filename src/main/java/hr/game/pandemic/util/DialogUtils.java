package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import hr.game.pandemic.GameApplication;
import hr.game.pandemic.dialogs.NewGameDialog;
import hr.game.pandemic.model.Player;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DialogUtils {

    private static GridPane pickGrid;

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
                    return true;
                } else if (clickedButton.get() == ButtonType.CANCEL) {
                    return false;
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Card showPickACardDialog(Player player, String cardType, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(title);

            List<Card> cards = new ArrayList<>();

            for (Card c : player.getHand()) {
                if (cardType.contains("city") || cardType.equals("all")) {
                    if (c instanceof CityCard cc) {
                        if (cardType.contains("yellow")){
                            if (cc.getColor().equals("yellow"))
                                cards.add(c);
                        } else if (cardType.contains("blue")) {
                            if (cc.getColor().equals("blue"))
                                cards.add(c);
                        } else if (cardType.contains("red")) {
                            if (cc.getColor().equals("red"))
                                cards.add(c);
                        } else if (cardType.contains("black")) {
                            if (cc.getColor().equals("black"))
                                cards.add(c);
                        } else
                            cards.add(c);
                    }
                }
                if (cardType.contains("event") || cardType.equals("all")) {
                    if (c instanceof EventCard) {
                        cards.add(c);
                    }
                }
                if (c.getName().equals(cardType)) {
                    cards.add(c);
                }
            }
            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            for (int i = 0; i < cards.size(); i++) {
                Card tmpCard = cards.get(i);
                Button tmpButton = new Button(tmpCard.getName().substring(0, 1).toUpperCase() + tmpCard.getName().substring(1));
                GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
                tmpButton.setUserData(tmpCard);
                tmpButton.setId(player.getName().toLowerCase() + tmpCard.getName());
                tmpButton.setCursor(Cursor.HAND);
                tmpButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(tmpButton, HPos.CENTER);
                if (tmpCard instanceof CityCard cc) {
                    tmpButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
                } else if (tmpCard instanceof EventCard) {
                    tmpButton.getStyleClass().add("cardEvent");
                    tmpButton.setUserData(tmpCard);
                    tmpButton.setOnAction(EventsUtils::onEventCardClick);
                }
                pickGrid.add(tmpButton, i%4, i/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedCard = (Button) getNodeByCoordinate(pickGrid, 2, 1);
                    if (pickedCard != null) {
                        return (Card) pickedCard.getUserData();
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
    private static void pickACardButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            int bRow = GridPane.getRowIndex(b);
            int bColumn = GridPane.getColumnIndex(b);
            Button prevPickedButton = (Button) getNodeByCoordinate(pickGrid, 2, 1);
            if (prevPickedButton != null){
                GridPane.setColumnIndex(prevPickedButton, bColumn);
                GridPane.setRowIndex(prevPickedButton, bRow);
            }
            GridPane.setColumnIndex(b, 1);
            GridPane.setRowIndex(b, 2);
        }
    }

    private static Node getNodeByCoordinate(GridPane gridLayout, int row, int column) {
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
                tmpButton.setOnAction(DialogUtils::pickACardButtonClick);
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

    public static String showPickADiseaseToTreat() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Pick a disease color you want to treat.");

            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

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
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("blue")) {
                Button yellowButton = new Button("Blue");
                yellowButton.getStyleClass().add("cardBlue");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("red")) {
                Button yellowButton = new Button("Red");
                yellowButton.getStyleClass().add("cardRed");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (currentCity.getDiseases().contains("black")) {
                Button yellowButton = new Button("Black");
                yellowButton.getStyleClass().add("cardBlack");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedColor = (Button) getNodeByCoordinate(pickGrid, 2, 1);
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

    public static Player showPickYourselfOrAnotherPlayerDialog(Player eventPlayer) {
        Player tmpPlayer = ControlUtils.currentPlayer;
        ControlUtils.currentPlayer = eventPlayer;
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
            playerToMove = ControlUtils.currentPlayer;
        } else if (result.get() == another) {
            playerToMove = showPickAnotherPlayerDialog("Pick a player to move.", false);
        } else if (result.get() == cancel) {
            ControlUtils.currentPlayer = tmpPlayer;
            return null;
        }
        ControlUtils.currentPlayer = tmpPlayer;
        return playerToMove;
    }

    public static Player showPickAnotherPlayerDialog(String title, boolean currentCityOnly) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(title);

            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            List<Player> playersToGet;
            if (currentCityOnly) {
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
                    tmpButton.setOnAction(DialogUtils::pickACardButtonClick);
                    GridPane.setHalignment(tmpButton, HPos.CENTER);
                    pickGrid.add(tmpButton, i%4, i/4);
                }
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedPlayer = (Button) getNodeByCoordinate(pickGrid, 2, 1);
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

    public static boolean showTakeCardConfirmationDialog(Player player) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Take card from " + player.getName() + "?");

        Optional<ButtonType> option = alert.showAndWait();
        return ButtonType.OK.equals(option.get());
    }

    public static String showPickAColorDialog(String colorsToShow) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("dialog_fxmls/pick-a-card-dialog.fxml"));
            DialogPane pickACardDialogPane = fxmlLoader.load();
            pickACardDialogPane.getStylesheets().add(DialogUtils.class.getResource("/hr/game/pandemic/stylesheets/cardButtons.css").toExternalForm());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Pick a color.");

            pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            int k = 0;
            if (colorsToShow.contains("yellow")) {
                Button yellowButton = new Button("Yellow");
                yellowButton.getStyleClass().add("cardYellow");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("blue")) {
                Button yellowButton = new Button("Blue");
                yellowButton.getStyleClass().add("cardBlue");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("red")) {
                Button yellowButton = new Button("Red");
                yellowButton.getStyleClass().add("cardRed");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
                k++;
            }
            if (colorsToShow.contains("black")) {
                Button yellowButton = new Button("Black");
                yellowButton.getStyleClass().add("cardBlack");
                GridPane.setMargin(yellowButton, new Insets(10, 10, 10, 10));
                yellowButton.setCursor(Cursor.HAND);
                yellowButton.setOnAction(DialogUtils::pickACardButtonClick);
                GridPane.setHalignment(yellowButton, HPos.CENTER);

                pickGrid.add(yellowButton, k%4, k/4);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedColor = (Button) getNodeByCoordinate(pickGrid, 2, 1);
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

    public static void showEpidemicCardDrawn() {

    }
}

