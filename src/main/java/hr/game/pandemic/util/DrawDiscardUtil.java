package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.Card;
import hr.game.pandemic.model.CityCard;
import hr.game.pandemic.model.EventCard;
import hr.game.pandemic.model.Player;

public class DrawDiscardUtil {
    public static boolean playerDrawCard(Player player) {
        if (GameController.playerCardPile.isEmpty())
            GameController.endGame(false, "No player cards left to draw!", false);
        else {
            if (GameController.playerCardPile.getLast().getName().equals("Epidemic")) {
                GameController.playerCardPile.removeLast();
                return true;
            }
            player.addCardToHand(GameController.playerCardPile.getLast());
            GameController.playerCardPile.removeLast();

            RefreshFXMLUtils.refreshPlayerHand(player);
            return false;
        }
        return false;
    }

    public static void playerDiscardCard(Player player, Card cardToDiscard) {
        player.removeCardFromHand(cardToDiscard);
        GameController.playerDiscardPile.add(cardToDiscard);
        RefreshFXMLUtils.refreshPlayerHand(player);
        RefreshFXMLUtils.refreshPlayerDiscardPile();
    }

    public static void checkIfPlayerHasTooManyCards(Player player) {
        while (player.getHand().size() > 7) {
            Card pickedCard = CardDialogUtils.showPickACardDialog(player, "city event", "Pick a city card to discard or event card to play.");
            if (pickedCard != null) {
                if (Character.isUpperCase(pickedCard.getName().charAt(0))) {
                    //play event card
                    EventsUtils.onEventCardPlay((EventCard) pickedCard, player);
                } else {
                    playerDiscardCard(player, pickedCard);
                }
            }
        }
    }

    public static void drawInfectionCard() {
        CityCard drawnCard = GameController.infectionCardPile.getLast();
        GameController.infectionCardPile.removeLast();
        GameController.infectionDiscardPile.add(drawnCard);
        RefreshFXMLUtils.refreshInfectionDiscardPile();
    }
}
