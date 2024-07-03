package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class UpdateGameBoardUtil {

    public static void updateBoard() {
        updateCities();
        updateInfectionCardPiles();
        updatePlayerCardPiles();
        updatePlayerHandsAndRoles();
        updateDiseaseCubeCountsAndCures();
        updatePlayerPositions();
    }

    private static void updateCities() {
        for (City city : GameController.cities) {
            FXMLUtils.refreshCityDiseases(city);
            updateCityResearchStation(city);
        }
    }

    private static void updateCityResearchStation(City city) {
        Button cityButton = (Button) FXMLUtils.getNodeById(city.getName(), GameController._gamePane);
        if(city.isResearchStation()) {
            cityButton.getStyleClass().add("research_station");
        } else {
            cityButton.getStyleClass().remove("research_station");
        }
    }

    private static void updateInfectionCardPiles() {
        RefreshFXMLUtils.refreshInfectionDiscardPile();
    }

    private static void updatePlayerCardPiles() {
        RefreshFXMLUtils.refreshPlayerDiscardPile();
    }

    private static void updatePlayerHandsAndRoles() {
        GridPane playersGrid = (GridPane) FXMLUtils.getNodeById("playersGridPane", GameController._gamePane);

        if (playersGrid.getChildren().size() < GameState.NUMBER_OF_PLAYERS + 1) {
            for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                FXMLUtils.addPlayerToPlayersGrid(playersGrid, i + 1);
            }
        }
        for (Player player : GameController.players) {
            RefreshFXMLUtils.refreshPlayerHand(player);
            FXMLUtils.updatePlayerRole(player);
        }

    }

    private static void updateDiseaseCubeCountsAndCures() {
            FXMLUtils.refreshDiseaseCubeCount();
            if (GameState.RED_CURE) {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("redCure", GameController._gamePane);
                cureImage.setVisible(true);
                if (GameState.RED_ERADICATED) {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/red_eradicated.png").toExternalForm()));
                } else {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/red_cure.png").toExternalForm()));
                }
            } else {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("redCure", GameController._gamePane);
                cureImage.setVisible(false);
            }
            if (GameState.YELLOW_CURE) {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("yellowCure", GameController._gamePane);
                cureImage.setVisible(true);
                if (GameState.YELLOW_ERADICATED) {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/yellow_eradicated.png").toExternalForm()));
                } else {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/yellow_cure.png").toExternalForm()));
                }
            } else {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("yellowCure", GameController._gamePane);
                cureImage.setVisible(false);
            }
            if (GameState.BLUE_CURE) {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blueCure", GameController._gamePane);
                cureImage.setVisible(true);
                if (GameState.BLUE_ERADICATED) {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/blue_eradicated.png").toExternalForm()));
                } else {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/blue_cure.png").toExternalForm()));
                }
            } else {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blueCure", GameController._gamePane);
                cureImage.setVisible(false);
            }
            if (GameState.BLACK_CURE) {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blackCure", GameController._gamePane);
                cureImage.setVisible(true);
                if (GameState.BLACK_ERADICATED) {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/black_eradicated.png").toExternalForm()));
                } else {
                    cureImage.setImage(new Image(GameApplication.class.getResource("/hr/game/pandemic/images/black_cure.png").toExternalForm()));
                }
            } else {
                ImageView cureImage = (ImageView) FXMLUtils.getNodeById("blackCure", GameController._gamePane);
                cureImage.setVisible(false);
            }
    }

    private static void updatePlayerPositions() {
        for (Player player : GameController.players) {
            FXMLUtils.updatePlayerLocation(player);
        }
    }

    public static void hideWaitToStartLabel() {
        Label waitLabel = (Label) FXMLUtils.getNodeById("waitToStartLabel", GameController._gamePane);
        waitLabel.setVisible(false);
    }
}
