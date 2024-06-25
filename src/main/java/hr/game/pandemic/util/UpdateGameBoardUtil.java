package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class UpdateGameBoardUtil {

    public static void updateBoard() {
        updateCities();
        updateInfectionCardPiles();
        updatePlayerCardPiles();
        updatePlayerHands();
        updateDiseaseCubeCountsAndCures();
        updatePlayerPositions();
    }

    private static void updateCities() {
        for (City city : GameController.cities) {
//            Button cityButton = (Button) FXMLUtils.getNodeById(city.getName(), GameController._gamePane);
            FXMLUtils.refreshCityDiseases(city);
        }
    }

    private static void updateInfectionCardPiles() {
        GameController.refreshInfectionDiscardPile();
    }

    private static void updatePlayerCardPiles() {
        GameController.refreshPlayerDiscardPile();
    }

    private static void updatePlayerHands() {
        GridPane playersGrid = (GridPane) FXMLUtils.getNodeById("playersGridPane", GameController._gamePane);

        if (playersGrid.getChildren().size() < GameState.NUMBER_OF_PLAYERS + 1) {
            for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                FXMLUtils.addPlayerToPlayersGrid(playersGrid, i + 1);
            }
        }
        for (Player player : GameController.players) {
            GameController.refreshPlayerHand(player);
        }

    }

    private static void updateDiseaseCubeCountsAndCures() {
        FXMLUtils.refreshDiseaseCubeCount();
        if (GameState.RED_CURE) {
            ImageView cureImage = (ImageView) FXMLUtils.getNodeById("redCure", GameController._gamePane);
            cureImage.setVisible(true);
            if (GameState.RED_ERADICATED) {
                cureImage.setImage(new Image("images\\red_eradicated.png"));
            } else {
                cureImage.setImage(new Image("images\\red_cure.png"));
            }
        }
        if (GameState.YELLOW_CURE) {
            ImageView cureImage = (ImageView) FXMLUtils.getNodeById("yellowCure", GameController._gamePane);
            cureImage.setVisible(true);
            if (GameState.YELLOW_ERADICATED) {
                cureImage.setImage(new Image("images\\yellow_eradicated.png"));
            } else {
                cureImage.setImage(new Image("images\\yellow_cure.png"));
            }
        }
        if (GameState.BLUE_CURE) {
            ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blueCure", GameController._gamePane);
            cureImage.setVisible(true);
            if (GameState.BLUE_ERADICATED) {
                cureImage.setImage(new Image("images\\blue_eradicated.png"));
            } else {
                cureImage.setImage(new Image("images\\blue_cure.png"));
            }
        }
        if (GameState.BLACK_CURE) {
            ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blackCure", GameController._gamePane);
            cureImage.setVisible(true);
            if (GameState.BLACK_ERADICATED) {
                cureImage.setImage(new Image("images\\black_eradicated.png"));
            } else {
                cureImage.setImage(new Image("images\\black_cure.png"));
            }
        }
    }

    private static void updatePlayerPositions() {
        for (Player player : GameController.players) {
            System.out.println(player.getName() + " current city: " + player.getCurrentCity());
            System.out.println(player.getName() + " previous city: " + player.getPreviousCity());
            FXMLUtils.updatePlayerLocation(player);
        }
    }

    public static void hideWaitToStartLabel() {
        Label waitLabel = (Label) FXMLUtils.getNodeById("waitToStartLabel", GameController._gamePane);
        waitLabel.setVisible(false);
    }
}
