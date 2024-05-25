package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class OtherActionUtils {

    public static void onBuildResearchStationButtonClick(Event event) {
        CityCard discardedCity = (CityCard) DialogUtils.showPickACardDialog(ControlUtils.currentPlayer, ControlUtils.currentPlayer.getCurrentCity(), "Discard the City card that matches the city you are in to place a research station there.");
        if (discardedCity != null) {
            GameController.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);

            if (GameState.RESEARCH_STATIONS == 0) {
                City researchToRemove = DialogUtils.showPickAResearchStationDialog();
                researchToRemove.setResearchStation(false);
                ControlUtils.citiesWithResearchStation.remove(researchToRemove);
                Button cityButton = (Button) FXMLUtils.getNodeById(researchToRemove.getName(), GameController._gamePane);
                cityButton.getStyleClass().remove("research_station");
                GameState.RESEARCH_STATIONS++;
            }
            GameState.RESEARCH_STATIONS--;
            City newResearchStationCity = GameController.cities.stream()
                    .filter(c -> c.getName().equals(discardedCity.getName()))
                    .findAny()
                    .orElse(null);
            newResearchStationCity.setResearchStation(true);
            ControlUtils.citiesWithResearchStation.add(newResearchStationCity);
            Button cityButton = (Button) FXMLUtils.getNodeById(newResearchStationCity.getName(), GameController._gamePane);
            cityButton.getStyleClass().add("research_station");
        }
        MovementActionUtils.actionDone();
    }

    public static void onTreatDiseaseButtonClick(Event event) {
        String pickedColor = DialogUtils.showPickADiseaseToTreat();
        if (pickedColor != null)
            treatDisease(pickedColor);
    }

    public static void treatDisease(String color) {
        City city = GameController.cities.stream()
                .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                .findAny()
                .orElse(null);

        if (color.equals("yellow")){
            if (GameState.YELLOW_CURE){
                city.cureAllOfDisease(color);
                if (GameState.YELLOW_CUBES == 24)
                    setDiseaseEradicated(color);
            } else {
                city.cureOneOfDisease(color);
            }
        }
        if (color.equals("red")){
            if (GameState.RED_CURE){
                city.cureAllOfDisease(color);
                if (GameState.RED_CUBES == 24)
                    setDiseaseEradicated(color);
            } else {
                city.cureOneOfDisease(color);
            }
        }
        if (color.equals("blue")){
            if (GameState.BLUE_CURE){
                city.cureAllOfDisease(color);
                if (GameState.BLACK_CUBES == 24)
                    setDiseaseEradicated(color);
            } else {
                city.cureOneOfDisease(color);
            }
        }
        if (color.equals("black")){
            if (GameState.BLACK_CURE){
                city.cureAllOfDisease(color);
                if (GameState.BLACK_CUBES == 24)
                    setDiseaseEradicated(color);
            } else {
                city.cureOneOfDisease(color);
            }
        }
        FXMLUtils.refreshCityButtonText(city);
        FXMLUtils.refreshDiseaseCubeCount();
        MovementActionUtils.actionDone();
    }

    public static void setDiseaseEradicated(String color) {
        ImageView cureImageView = (ImageView) FXMLUtils.getNodeById(color + "Cure", GameController._gamePane);
        cureImageView.setImage(new Image("images\\" + color +"_eradicated.png"));
    }

    public static void onShareKnowledgeButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            Player playerWithCurrentCityCard = (Player) b.getUserData();
            if (playerWithCurrentCityCard == ControlUtils.currentPlayer){
                Player pickedPlayer = DialogUtils.showPickAnotherPlayerDialog("Pick a player to give current city card to.", true);
                Card cardToGive = ControlUtils.currentPlayer.getHand().stream()
                        .filter(card -> card.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                        .findAny()
                        .orElse(null);
                pickedPlayer.addCardToHand(cardToGive);
                ControlUtils.currentPlayer.removeCardFromHand(cardToGive);
                GameController.checkIfPlayerHasTooManyCards(pickedPlayer);
                GameController.refreshPlayerHand(pickedPlayer);
                GameController.refreshPlayerHand(ControlUtils.currentPlayer);
            } else {
                boolean toTake = DialogUtils.showTakeCardConfirmationDialog(playerWithCurrentCityCard);
                if (toTake) {
                    Card cardToGive = playerWithCurrentCityCard.getHand().stream()
                            .filter(card -> card.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                            .findAny()
                            .orElse(null);
                    ControlUtils.currentPlayer.addCardToHand(cardToGive);
                    playerWithCurrentCityCard.removeCardFromHand(cardToGive);
                    GameController.checkIfPlayerHasTooManyCards(ControlUtils.currentPlayer);
                    GameController.refreshPlayerHand(playerWithCurrentCityCard);
                    GameController.refreshPlayerHand(ControlUtils.currentPlayer);
                }
            }
        }
        MovementActionUtils.actionDone();
    }

    public static void onDiscoverACureButtonClick(Event event) {

        String colorsToShow = "";
        if (ControlUtils.yellowCount > 4)
            colorsToShow += " yellow";
        if (ControlUtils.blueCount > 4)
            colorsToShow += " blue";
        if (ControlUtils.redCount > 4)
            colorsToShow += " red";
        if (ControlUtils.blackCount > 4)
            colorsToShow += " black";

        String pickedColor = DialogUtils.showPickAColorDialog(colorsToShow);

        if (pickedColor != null){
            List<CityCard> cardsToDiscard = new ArrayList<>();
            Player tmpPlayer = new Player(ControlUtils.currentPlayer.getName(), new ArrayList<>(ControlUtils.currentPlayer.getHand()), ControlUtils.currentPlayer.getCurrentCity());

            int k = 0;
            while (k < 5) {
                CityCard pickedCard = (CityCard) DialogUtils.showPickACardDialog(tmpPlayer, "city " + pickedColor, "Pick 5 cards to discard. Cards picked: " + k);
                if (pickedCard != null) {
                    tmpPlayer.removeCardFromHand(pickedCard);
                    cardsToDiscard.add(pickedCard);
                    k++;
                } else {
                    break;
                }
            }

            if (k > 4) {
                for (CityCard c : cardsToDiscard){
                    GameController.playerDiscardCard(ControlUtils.currentPlayer, c);
                }
                if (pickedColor.equals("yellow"))
                    GameState.YELLOW_CURE = true;
                else if (pickedColor.equals("blue"))
                    GameState.BLUE_CURE = true;
                else if (pickedColor.equals("red"))
                    GameState.RED_CURE = true;
                else if (pickedColor.equals("black"))
                    GameState.BLACK_CURE = true;

                ImageView cureImage = (ImageView) FXMLUtils.getNodeById(pickedColor + "Cure", GameController._gamePane);
                cureImage.setVisible(true);

                MovementActionUtils.actionDone();
            }
        }


    }

}
