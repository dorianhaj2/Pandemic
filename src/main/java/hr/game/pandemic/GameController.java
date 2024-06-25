package hr.game.pandemic;

import hr.game.pandemic.model.*;
import hr.game.pandemic.model.roles.Medic;
import hr.game.pandemic.model.roles.QuarantineSpecialist;
import hr.game.pandemic.model.roles.Researcher;
import hr.game.pandemic.model.roles.Scientist;
import hr.game.pandemic.util.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GameController {
    private static final String SAVE_GAME_FILE_NAME = "files/save.bin";
    @FXML
    private Pane gamePane;
    private static boolean SETUP;
    public static Pane _gamePane;
    public static List<CityCard> infectionCardPile;
    public static List<CityCard> infectionDiscardPile;
    public static List<Card> playerCardPile;
    public static List<Card> playerDiscardPile;
    private static List<EventCard> eventCards = new ArrayList<>();
    private static List<CityCard> cityCards = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();
    public static List<City> cities = new ArrayList<>();
    public static List<List<String>> citiesColors = new ArrayList<>();
    private static List<String> outbreaksInCitiesInCurrentChain = new ArrayList<>();
    public void initialize() throws InterruptedException {
        _gamePane = gamePane;
        //Read cities and their corresponding colors from file
        try(BufferedReader br = new BufferedReader(new FileReader("files/cityColors.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                citiesColors.add(Arrays.asList(values));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Make city cards and city object lists from read data
        for(List<String> s : citiesColors) {
            cityCards.add(new CityCard(s.get(0), s.get(1)));
            if (s.get(0).equals("atlanta"))
                cities.add(new City(s.get(0), s.get(1), true));
            else
                cities.add(new City(s.get(0), s.get(1)));
        }

        ControlUtils.disableAllCityButtons();

        //Read all event cards from file
        JSONParser parser = new JSONParser();
        try {
            Object arr = parser.parse(new FileReader("files/eventCards.json"));
            JSONArray jsonArray = (JSONArray) arr;

            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String name = (String) jsonObject.get("name");
                String desc = (String) jsonObject.get("description");
                eventCards.add(new EventCard(name, desc));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (City c : cities) {
            FXMLUtils.addGridViewForPlayersAndImagesAndLabelsForDiseasesToCity(c);
        }

        while(GameState.DIFFICULTY == null)
            newGame();
    }

    public void cityButtonPressed(Event event) {
        if (event.getSource() instanceof Button b) {
            if (EventsUtils.governmentGrandPlayed){
                City newResearchStationCity = GameController.cities.stream()
                        .filter(c -> c.getName().equals(b.getId()))
                        .findAny()
                        .orElse(null);
                newResearchStationCity.setResearchStation(true);
                ControlUtils.citiesWithResearchStation.add(newResearchStationCity);
                Button cityButton = (Button) FXMLUtils.getNodeById(newResearchStationCity.getName(), GameController._gamePane);
                cityButton.getStyleClass().add("research_station");

                ControlUtils.disableAllCityButtons();
                //ControlUtils.enableAllControls();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            } else if (EventsUtils.airliftPlayed) {
                MovementActionUtils.moveCurrentPlayerToCity(b.getId());
                setCurrentPlayerBasedOnNumberOfTurns();
                EventsUtils.airliftPlayed = false;
                //ControlUtils.enableAllControls();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            } else {
                MovementActionUtils.moveCurrentPlayerToCity(b.getId());
                MovementActionUtils.actionDone();
            }

        }

    }

    public static void setCurrentPlayerBasedOnNumberOfTurns() {
        ControlUtils.currentPlayer = players.get(GameState.getCurrentPlayerNumber() - 1);
    }

    public void saveGame() {
        GameStateDTO gameStateDTO = new GameStateDTO();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SAVE_GAME_FILE_NAME)
            );

            oos.writeObject(gameStateDTO);

            showAlert("Save game", "Game saved!");

        } catch (IOException e) {
            showAlert("Error", "Error while trying to save game: " +
                    e.getMessage());
        }

    }

    public void loadGame() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_GAME_FILE_NAME));
            if (ois.readObject() instanceof GameStateDTO gameStateDTO) {
                gameStateDTO.setGameState();
                GameApplication.client.sendGameState();
            }

            showAlert("Load game", "Game loaded!");

        } catch (IOException | ClassNotFoundException e) {
            showAlert("Error", "Error while trying to load game: " +
                    e.getMessage());
        }
    }

    private void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public void newGame() throws InterruptedException {
        if (GameApplication.player.equals(PlayerEnum.PLAYER1)) {
            GameState.START_GAME = DialogUtils.showNewGameDialog();
            if (GameState.START_GAME) {
                GameState.NUMBER_OF_TURNS = 0;
                GameState.OUTBREAK_COUNTER = 0;
                GameState.INFECTION_RATE = 1;
                GameState.YELLOW_CUBES = 24;
                GameState.RED_CUBES = 24;
                GameState.BLUE_CUBES = 24;
                GameState.BLACK_CUBES = 24;
                GameState.RESEARCH_STATIONS = 5;
                GameState.YELLOW_CURE = false;
                GameState.RED_CURE = false;
                GameState.BLUE_CURE = false;
                GameState.BLACK_CURE = false;
                GameState.YELLOW_ERADICATED = false;
                GameState.RED_ERADICATED = false;
                GameState.BLUE_ERADICATED = false;
                GameState.BLACK_ERADICATED = false;
                GameState.END_GAME = false;
                SETUP = true;

                for (Node node : FXMLUtils.getNodesByIdStartsWith("outbreak", _gamePane)) {
                    ImageView iv = (ImageView) node;
                    iv.setVisible(false);
                }

                for (Node node : FXMLUtils.getNodesByIdStartsWith("infectionRate", _gamePane)) {
                    ImageView iv = (ImageView) node;
                    iv.setVisible(false);
                }
                ImageView ivInfectionRate1 = (ImageView) FXMLUtils.getNodeById("infectionRate1", gamePane);
                ivInfectionRate1.setVisible(true);

                GridPane playersGrid = (GridPane) FXMLUtils.getNodeById("playersGridPane", _gamePane);
                List<Node> nodesToRemove = new ArrayList<>();
                for (Node node : playersGrid.getChildren()) {
                    if (node.getId().startsWith("player"))
                        nodesToRemove.add(node);
                }
                playersGrid.getChildren().removeAll(nodesToRemove);
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    FXMLUtils.addPlayerToPlayersGrid(playersGrid, i + 1);
                }

                for (City city : cities) {
                    city.setDiseases(new ArrayList<>());
                }

                List<Role> roles = new ArrayList<>(List.of(Role.values()));
                Collections.shuffle(roles);

                //Remove players from board on new game
                if (!players.isEmpty()) {
                    for (Player p : players) {
                        p.setPreviousCity(p.getCurrentCity());
                        p.setCurrentCity("");
                        FXMLUtils.updatePlayerLocation(p);
                    }
                }
                //Add new players to list
                players = new ArrayList<>();
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    if (roles.getLast().equals(Role.MEDIC)) {
                        players.add(new Medic("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.QUARANTINE_SPECIALIST)) {
                        players.add(new QuarantineSpecialist("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.RESEARCHER)) {
                        players.add(new Researcher("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.SCIENTIST)) {
                        players.add(new Scientist("Player" + (i + 1)));
                    }
                    roles.removeLast();
                    FXMLUtils.updatePlayerLocation(players.get(i));
                    FXMLUtils.updatePlayerRole(players.get(i));
                }

                //Add cards to infection pile and shuffle
                infectionCardPile = new ArrayList<>(cityCards);
                Collections.shuffle(infectionCardPile);
                infectionDiscardPile = new ArrayList<>();

                //Add initial cards to player card pile and shuffle, empty player discard pile
                playerCardPile = new ArrayList<>(cityCards);
                playerCardPile.addAll(eventCards);
                Collections.shuffle(playerCardPile);
                playerDiscardPile = new ArrayList<>();

                //Empty research station list and add Atlanta
                ControlUtils.citiesWithResearchStation.clear();
                for (City c : cities) {
                    if (c.getName().equals("atlanta"))
                        ControlUtils.citiesWithResearchStation.add(c);
                }

                //Players draw cards
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    for (int j = 0; j < 6 - GameState.NUMBER_OF_PLAYERS; j++) {
                        playerDrawCard(players.get(i));
                    }
                }

                //Add epidemic cards to player card pile and shuffle
                for (int i = 0; i < GameState.DIFFICULTY + 3; i++) {
                    playerCardPile.add(new EpidemicCard());
                }
                Collections.shuffle(playerCardPile);

                //Start of game infections
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        drawInfectionCard();
                        infectCity(infectionDiscardPile.getLast().getName(), infectionDiscardPile.getLast().getColor(), 3 - i);
                    }
                }
                ControlUtils.currentPlayer = players.getFirst();
                SETUP = false;
                GameApplication.client.sendGameState();
                ControlUtils.startTurn();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            }
        } else {
                GameState.DIFFICULTY = 1;
                Label waitToStartLabel = new Label("Waiting for host to start the game...");
                waitToStartLabel.setId("waitToStartLabel");
                waitToStartLabel.setLayoutX(1560);
                waitToStartLabel.setLayoutY(500);

                gamePane.getChildren().add(waitToStartLabel);
        }

    }

    public static boolean playerDrawCard(Player player) {
        if (playerCardPile.isEmpty())
            endGame(false, "No player cards left to draw!");
        else {
            if (playerCardPile.getLast().getName().equals("Epidemic")) {
                playerCardPile.removeLast();
                return true;
            }
            player.addCardToHand(playerCardPile.getLast());
            playerCardPile.removeLast();

            refreshPlayerHand(player);
            return false;
        }
        return false;
    }

    public static void playerDiscardCard(Player player, Card cardToDiscard) {
        player.removeCardFromHand(cardToDiscard);
        playerDiscardPile.add(cardToDiscard);
        refreshPlayerHand(player);
        refreshPlayerDiscardPile();
    }

    public static void checkIfPlayerHasTooManyCards(Player player) {
        while (player.getHand().size() > 7) {
            Card pickedCard = DialogUtils.showPickACardDialog(player, "city event", "Pick a city card to discard or event card to play.");
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

    public static void drawInfectionCard() {
        CityCard drawnCard = infectionCardPile.getLast();
        infectionCardPile.removeLast();
        infectionDiscardPile.add(drawnCard);
        refreshInfectionDiscardPile();
    }

    public static void infectAdjacentCities(String sourceCityName, String diseaseColor) {
        List<String> citiesToInfect = CityGraph.cityGraph.get(sourceCityName);

        for (String s : citiesToInfect) {
            if (!outbreaksInCitiesInCurrentChain.contains(s) && !GameState.END_GAME) {
                infectCity(s, diseaseColor, 1);
            }
        }
    }

    public static void infectCity(String cityName, String diseaseColor, int amount) {
        for(City cityToInfect : cities) {
            if (cityToInfect.getName().equals(cityName)) {
                QuarantineSpecialist potentialQuarantineSpecialistPlayer = null;
                for (Player player : players) {
                    if (player instanceof QuarantineSpecialist) {
                        potentialQuarantineSpecialistPlayer = (QuarantineSpecialist) player;
                    }
                }
                boolean toInfect = true;
                if (!SETUP) {
                    if (potentialQuarantineSpecialistPlayer != null) {
                        if (potentialQuarantineSpecialistPlayer.getCurrentCity().equals(cityName)
                                || CityGraph.cityGraph.get(potentialQuarantineSpecialistPlayer.getCurrentCity()).contains(cityName)) {
                            toInfect = false;
                        }
                    }
                }
                if (toInfect) {
                    boolean toOutbreak = cityToInfect.infect(diseaseColor, amount);
                    FXMLUtils.refreshCityDiseases(cityToInfect);
                    FXMLUtils.refreshDiseaseCubeCount();
                    if (toOutbreak) {
                        outbreaksInCitiesInCurrentChain.add(cityName);
                        GameState.OUTBREAK_COUNTER++;
                        FXMLUtils.moveOutbreakToken();
                        if (GameState.OUTBREAK_COUNTER == 8)
                            endGame(false, "Outbreak marker reached last space of the Outbreaks Track!");
                        else {
                            infectAdjacentCities(cityName, diseaseColor);
                        }
                    }
                }
            }
        }
    }

    public static void refreshPlayerDiscardPile() {
        Button discardPileButton = (Button) FXMLUtils.getNodeById("playerDiscard", _gamePane);
        if (playerDiscardPile.isEmpty()) {
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("invisible");
            discardPileButton.setText("");
        } else {
            Card topCard = playerDiscardPile.getLast();
            discardPileButton.getStyleClass().removeLast();
            if (topCard instanceof CityCard cc)
                discardPileButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
            else
                discardPileButton.getStyleClass().add("cardEvent");
            discardPileButton.setText(topCard.getName().substring(0, 1).toUpperCase() + topCard.getName().substring(1));
        }
    }

    public static void refreshInfectionDiscardPile() {
        Button discardPileButton = (Button) FXMLUtils.getNodeById("infectionDiscard", _gamePane);
        if (infectionDiscardPile.isEmpty()) {
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("invisible");
            discardPileButton.setText("");
        } else {
            CityCard topCard = infectionDiscardPile.getLast();
            discardPileButton.getStyleClass().removeLast();
            discardPileButton.getStyleClass().add("card" + topCard.getColor().substring(0, 1).toUpperCase() + topCard.getColor().substring(1));
            discardPileButton.setText(topCard.getName().substring(0, 1).toUpperCase() + topCard.getName().substring(1));
        }
    }

    public static void endGame(boolean gameWin, String loseReason) {
        GameState.END_GAME = true;
        ControlUtils.showOrHideControlsDependingOnCurrentPlayer(true);
        ControlUtils.disableAllOtherControls("");
        ControlUtils.disableAllCityButtons();
        Alert endGameAlert = new Alert(Alert.AlertType.INFORMATION);
        if (gameWin) {
            endGameAlert.setTitle("Congratulations!");
            endGameAlert.setHeaderText("You won!");
        } else {
            endGameAlert.setTitle("Game over!");
            endGameAlert.setHeaderText(loseReason);
        }
        endGameAlert.showAndWait();
    }

    public static void setGamePane(Pane pane) {

    }

}