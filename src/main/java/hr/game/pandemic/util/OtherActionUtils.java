package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import hr.game.pandemic.model.roles.Medic;
import hr.game.pandemic.model.roles.Researcher;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OtherActionUtils {

    public static void onBuildResearchStationButtonClick(Event event) {
        CityCard discardedCity = (CityCard) DialogUtils.showPickACardDialog(ControlUtils.currentPlayer, ControlUtils.currentPlayer.getCurrentCity(), "Discard the City card that matches the city you are in to place a research station there.");
        if (discardedCity != null) {
            if (GameState.RESEARCH_STATIONS == 0) {
                City researchToRemove = DialogUtils.showPickAResearchStationDialog();
                if (researchToRemove != null) {
                    researchToRemove.setResearchStation(false);
                    ControlUtils.citiesWithResearchStation.remove(researchToRemove);
                    Button cityButton = (Button) FXMLUtils.getNodeById(researchToRemove.getName(), GameController._gamePane);
                    cityButton.getStyleClass().remove("research_station");
                    GameState.RESEARCH_STATIONS++;
                } else {
                    return;
                }
            }
            GameController.playerDiscardCard(ControlUtils.currentPlayer, discardedCity);
            GameState.RESEARCH_STATIONS--;
            Optional<City> newResearchStationCityOptional = GameController.cities.stream()
                    .filter(c -> c.getName().equals(discardedCity.getName()))
                    .findAny();
            if (newResearchStationCityOptional.isPresent()) {
                City newResearchStationCity = newResearchStationCityOptional.get();
                newResearchStationCity.setResearchStation(true);
                ControlUtils.citiesWithResearchStation.add(newResearchStationCity);
                Button cityButton = (Button) FXMLUtils.getNodeById(newResearchStationCity.getName(), GameController._gamePane);
                cityButton.getStyleClass().add("research_station");
            }
            MovementActionUtils.actionDone();
        }

    }

    public static void onTreatDiseaseButtonClick(Event event) {
        String pickedColor = DialogUtils.showPickADiseaseToTreat();
        if (pickedColor != null)
            treatDisease(pickedColor);
    }

    public static void treatDisease(String color) {
        Optional<City> cityOptional = GameController.cities.stream()
                .filter(c -> c.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                .findAny();
        if (cityOptional.isPresent()) {
            City city = cityOptional.get();

            if (color.equals("yellow")) {
                if (GameState.YELLOW_CURE || ControlUtils.currentPlayer instanceof Medic) {
                    city.cureAllOfDisease(color);
                    if (GameState.YELLOW_CUBES == 24)
                        setDiseaseEradicated(color);
                } else {
                    city.cureOneOfDisease(color);
                }
            }
            if (color.equals("red")) {
                if (GameState.RED_CURE || ControlUtils.currentPlayer instanceof Medic) {
                    city.cureAllOfDisease(color);
                    if (GameState.RED_CUBES == 24)
                        setDiseaseEradicated(color);
                } else {
                    city.cureOneOfDisease(color);
                }
            }
            if (color.equals("blue")) {
                if (GameState.BLUE_CURE || ControlUtils.currentPlayer instanceof Medic) {
                    city.cureAllOfDisease(color);
                    if (GameState.BLACK_CUBES == 24)
                        setDiseaseEradicated(color);
                } else {
                    city.cureOneOfDisease(color);
                }
            }
            if (color.equals("black")) {
                if (GameState.BLACK_CURE || ControlUtils.currentPlayer instanceof Medic) {
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
    }

    public static void setDiseaseEradicated(String color) {
        ImageView cureImageView = (ImageView) FXMLUtils.getNodeById(color + "Cure", GameController._gamePane);
        cureImageView.setImage(new Image("images\\" + color + "_eradicated.png"));
    }

    public static void onShareKnowledgeButtonClick(Event event) {
        if (event.getSource() instanceof Button b) {
            List<Player> playersOnCurrentCity = (List<Player>) b.getUserData();
            boolean currentPlayerHasCurrentCityCardOrIsAResearcher = false;
            boolean anotherPlayerHasCurrentCityCard = false;
            boolean anotherPlayerIsAResearcher = false;
            boolean toTake = false;
            for (Player p : playersOnCurrentCity) {
                if (p == ControlUtils.currentPlayer)
                    currentPlayerHasCurrentCityCardOrIsAResearcher = true;
                else if (p instanceof Researcher)
                    anotherPlayerIsAResearcher = true;
                else
                    anotherPlayerHasCurrentCityCard = true;
            }
            if (currentPlayerHasCurrentCityCardOrIsAResearcher) {
                if ((ControlUtils.currentPlayer instanceof Researcher && anotherPlayerHasCurrentCityCard) || (!(ControlUtils.currentPlayer instanceof Researcher) && anotherPlayerIsAResearcher)) {
                    toTake = DialogUtils.showTakeCardOrGiveCardDialog();
                }
                if (!toTake) {
                    Player pickedPlayer = DialogUtils.showPickAnotherPlayerDialog("Pick a player to give a city card to.", true);
                    if (pickedPlayer != null) {
                        Card cardToGive = null;
                        if (ControlUtils.currentPlayer instanceof Researcher) {
                            cardToGive = DialogUtils.showPickACardDialog(ControlUtils.currentPlayer, "city", "Pick a city card to give.");
                        } else {
                            Optional<Card> cardToGiveOptional = ControlUtils.currentPlayer.getHand().stream()
                                    .filter(card -> card.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                                    .findAny();
                            if (cardToGiveOptional.isPresent()) {
                                cardToGive = cardToGiveOptional.get();
                            }
                        }

                        if (cardToGive != null) {
                            pickedPlayer.addCardToHand(cardToGive);
                            ControlUtils.currentPlayer.removeCardFromHand(cardToGive);
                            GameController.checkIfPlayerHasTooManyCards(pickedPlayer);
                            GameController.refreshPlayerHand(pickedPlayer);
                            GameController.refreshPlayerHand(ControlUtils.currentPlayer);
                            MovementActionUtils.actionDone();
                        }
                    }
                }
            }
            if (toTake || (!currentPlayerHasCurrentCityCardOrIsAResearcher && (anotherPlayerIsAResearcher || anotherPlayerHasCurrentCityCard))) {
                Card cardToTake = null;
                playersOnCurrentCity.remove(ControlUtils.currentPlayer);
                Player playerToTakeFrom = DialogUtils.showPickAnotherPlayerDialog("Choose a player to take a city card from.", false, playersOnCurrentCity);

                if (playerToTakeFrom instanceof Researcher) {
                    cardToTake = DialogUtils.showPickACardDialog(playerToTakeFrom, "city", "Choose a card to take from the researcher.");
                } else {
                    if (playerToTakeFrom != null) {
                        boolean toTake2 = DialogUtils.showTakeCardConfirmationDialog(playerToTakeFrom);
                        if (toTake2) {
                            Optional<Card> cardToTakeOptional = playerToTakeFrom.getHand().stream()
                                    .filter(card -> card.getName().equals(ControlUtils.currentPlayer.getCurrentCity()))
                                    .findAny();
                            if (cardToTakeOptional.isPresent()) {
                                cardToTake = cardToTakeOptional.get();
                            }
                        }
                    }
                }
                if (cardToTake != null) {
                    ControlUtils.currentPlayer.addCardToHand(cardToTake);
                    playerToTakeFrom.removeCardFromHand(cardToTake);
                    GameController.checkIfPlayerHasTooManyCards(ControlUtils.currentPlayer);
                    GameController.refreshPlayerHand(playerToTakeFrom);
                    GameController.refreshPlayerHand(ControlUtils.currentPlayer);
                    MovementActionUtils.actionDone();
                }
                playersOnCurrentCity.add(ControlUtils.currentPlayer);
            }
        }
    }

    public static void onDiscoverACureButtonClick(Event event) {

        String colorsToShow = "";
        if (ControlUtils.yellowCount > 4 || (ControlUtils.yellowCount > 3 && ControlUtils.currentPlayer instanceof Researcher))
            colorsToShow += " yellow";
        if (ControlUtils.blueCount > 4 || (ControlUtils.blueCount > 3 && ControlUtils.currentPlayer instanceof Researcher))
            colorsToShow += " blue";
        if (ControlUtils.redCount > 4 || (ControlUtils.redCount > 3 && ControlUtils.currentPlayer instanceof Researcher))
            colorsToShow += " red";
        if (ControlUtils.blackCount > 4 || (ControlUtils.blackCount > 3 && ControlUtils.currentPlayer instanceof Researcher))
            colorsToShow += " black";

        String pickedColor = DialogUtils.showPickAColorDialog(colorsToShow);

        if (pickedColor != null) {
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
                for (CityCard c : cardsToDiscard) {
                    GameController.playerDiscardCard(ControlUtils.currentPlayer, c);
                }
                switch (pickedColor) {
                    case "yellow" -> GameState.YELLOW_CURE = true;
                    case "blue" -> GameState.BLUE_CURE = true;
                    case "red" -> GameState.RED_CURE = true;
                    case "black" -> GameState.BLACK_CURE = true;
                }

                ImageView cureImage = (ImageView) FXMLUtils.getNodeById(pickedColor + "Cure", GameController._gamePane);
                cureImage.setVisible(true);

                if (GameState.YELLOW_CURE && GameState.BLUE_CURE && GameState.RED_CURE && GameState.BLACK_CURE)
                    GameController.endGame(true, "");

                MovementActionUtils.actionDone();
            }
        }


    }

}
