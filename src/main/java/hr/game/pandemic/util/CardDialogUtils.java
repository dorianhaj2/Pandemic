package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.EventCard;
import hr.game.pandemic.model.Player;
import javafx.event.Event;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDialogUtils {
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
            DialogUtils.pickGrid = (GridPane) FXMLUtils.getNodeById("pickACardGridPane", pickACardDialogPane);

            for (int i = 0; i < cards.size(); i++) {
                Card tmpCard = cards.get(i);
                Button tmpButton = new Button(tmpCard.getName().substring(0, 1).toUpperCase() + tmpCard.getName().substring(1));
                GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
                tmpButton.setUserData(tmpCard);
                tmpButton.setId(player.getName().toLowerCase().substring(0, player.getName().length()-1) + "Card" + player.getName().charAt(player.getName().length() - 1) + tmpCard.getName());
                tmpButton.setCursor(Cursor.HAND);
                tmpButton.setOnAction(CardDialogUtils::pickACardButtonClick);
                GridPane.setHalignment(tmpButton, HPos.CENTER);
                if (tmpCard instanceof CityCard cc) {
                    tmpButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
                } else if (tmpCard instanceof EventCard) {
                    tmpButton.getStyleClass().add("cardEvent");
                    tmpButton.setUserData(tmpCard);
                    tmpButton.setOnAction(EventsUtils::onEventCardClick);
                }
                DialogUtils.pickGrid.add(tmpButton, i%5, i/5);
            }

            dialog.setDialogPane(pickACardDialogPane);

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if(clickedButton.isPresent())
                if(clickedButton.get() == ButtonType.OK) {
                    Button pickedCard = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 2, 1);
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

    public static void pickACardButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            int bRow = GridPane.getRowIndex(b);
            int bColumn = GridPane.getColumnIndex(b);
            Button prevPickedButton = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 2, 1);
            if (prevPickedButton != null){
                GridPane.setColumnIndex(prevPickedButton, bColumn);
                GridPane.setRowIndex(prevPickedButton, bRow);
            }
            GridPane.setColumnIndex(b, 1);
            GridPane.setRowIndex(b, 2);
        }
    }

    public static void pickAnInfectionCardButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            int bRow = GridPane.getRowIndex(b);
            int bColumn = GridPane.getColumnIndex(b);
            Button prevPickedButton = (Button) DialogUtils.getNodeByCoordinate(DialogUtils.pickGrid, 6, 1);
            if (prevPickedButton != null){
                GridPane.setColumnIndex(prevPickedButton, bColumn);
                GridPane.setRowIndex(prevPickedButton, bRow);
            }
            GridPane.setColumnIndex(b, 1);
            GridPane.setRowIndex(b, 6);
        }
    }
}
