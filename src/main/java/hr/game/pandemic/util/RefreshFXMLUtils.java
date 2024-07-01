package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.EventCard;
import hr.game.pandemic.model.Player;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class RefreshFXMLUtils {
    public static void refreshPlayerHand(Player player) {
        GridPane playerHandGrid = (GridPane) FXMLUtils.getNodeById(player.getName().toLowerCase() + "Hand", GameController._gamePane);
        playerHandGrid.getChildren().clear();
        for (int k = 0; k < player.getHand().size(); k++) {
            Card tmpCard = player.getHand().get(k);
            Button tmpButton = new Button(tmpCard.getName().substring(0, 1).toUpperCase() + tmpCard.getName().substring(1));
            GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
            tmpButton.setUserData(tmpCard);

            tmpButton.setId(player.getName().toLowerCase().substring(0, player.getName().length()-1) + "Card" + player.getName().charAt(player.getName().length() - 1) + tmpCard.getName());
            tmpButton.setCursor(Cursor.HAND);
            GridPane.setHalignment(tmpButton, HPos.CENTER);
            if (tmpCard instanceof CityCard cc) {
                tmpButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
            } else if (tmpCard instanceof EventCard) {
                tmpButton.getStyleClass().add("cardEvent");
                tmpButton.setUserData(tmpCard);
                tmpButton.setOnAction(EventsUtils::onEventCardClick);
            }
            playerHandGrid.add(tmpButton, k%4, k/4);
        }
    }

    public static void refreshPlayerDiscardPile() {
        Button discardPileButton = (Button) FXMLUtils.getNodeById("playerDiscard", GameController._gamePane);
        if (GameController.playerDiscardPile.isEmpty()) {
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("invisible");
            discardPileButton.setText("");
        } else {
            Card topCard = GameController.playerDiscardPile.getLast();
            discardPileButton.getStyleClass().removeLast();
            if (topCard instanceof CityCard cc)
                discardPileButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
            else
                discardPileButton.getStyleClass().add("cardEvent");
            discardPileButton.setText(topCard.getName().substring(0, 1).toUpperCase() + topCard.getName().substring(1));
        }
    }

    public static void refreshInfectionDiscardPile() {
        Button discardPileButton = (Button) FXMLUtils.getNodeById("infectionDiscard", GameController._gamePane);
        if (GameController.infectionDiscardPile.isEmpty()) {
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("invisible");
            discardPileButton.setText("");
        } else {
            CityCard topCard = GameController.infectionDiscardPile.getLast();
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("card" + topCard.getColor().substring(0, 1).toUpperCase() + topCard.getColor().substring(1));
            discardPileButton.setText(topCard.getName().substring(0, 1).toUpperCase() + topCard.getName().substring(1));
        }
    }
}
