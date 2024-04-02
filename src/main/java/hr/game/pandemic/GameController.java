package hr.game.pandemic;

import hr.game.pandemic.model.*;
import hr.game.pandemic.util.ActionUtil;
import hr.game.pandemic.util.ControlUtil;
import hr.game.pandemic.util.DialogUtil;
import hr.game.pandemic.util.FXMLUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController {
    @FXML
    private Pane gamePane;
    public static Pane _gamePane;
    private List<CityCard> infectionCardPile;
    private List<CityCard> infectionDiscardPile;
    private static List<Card> playerCardPile;
    private static List<Card> playerDiscardPile;
    private static List<EventCard> eventCards = new ArrayList<>();
    private static List<CityCard> cityCards = new ArrayList<>();
    private static List<Player> players;
    private static List<City> cities = new ArrayList<>();
    public static List<List<String>> citiesColors = new ArrayList<>();
    private List<String> outbreaksInCitiesInCurrentChain = new ArrayList<>();
    public void initialize() {
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

        ControlUtil.disableAllCityButtons();

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
            FXMLUtil.addGridViewForPlayersToCity(c);
        }


        /*for(Button b : allButtons){
            Tooltip newTooltip = new Tooltip();
            newTooltip.setText(b.getText());
            b.setTooltip(newTooltip);

            for(CityCard cc : cityCards) {
                if(cc.getName().equals(b.getId())){
                    if (cc.getName().equals("atlanta"))
                        b.setUserData(new City(cc.getName(), cc.getColor(), true));
                    else
                        b.setUserData(new City(cc.getName(), cc.getColor()));
                }
            }
        }

*/

        while(GameState.DIFFICULTY == null)
            newGame();
    }

    public void cityButtonPressed(Event event) {
        if (event.getSource() instanceof Button b) {
//            System.out.println( b.getId());
//            ActionUtil.driveFerryCityClicked(players.get(GameState.getCurrentPlayerNumber()-1), b);
            ActionUtil.moveCurrentPlayerToCity(b.getId());
            ActionUtil.actionDone();
        }

    }

    public void cardButtonPressed(Event event) {
        if (event.getSource() instanceof Button b) {

        }
    }
    public void saveGame() {

    }
    public void loadGame() {

    }
    public void newGame() {
        boolean startGame = DialogUtil.showNewGameDialog();
        if (startGame) {
            GameState.NUMBER_OF_TURNS = 1;
            GameState.OUTBREAK_COUNTER = 0;
            GameState.INFECTION_RATE = 1;
            GameState.YELLOW_CUBES = 24;
            GameState.RED_CUBES = 24;
            GameState.BLUE_CUBES = 24;
            GameState.BLACK_CUBES = 24;
            GameState.RESEARCH_STATIONS = 6;

            GridPane playersGrid = (GridPane) FXMLUtil.getNodeById("playersGridPane", _gamePane);
            for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                FXMLUtil.addPlayerToPlayersGrid(playersGrid, i+1);
            }

            List<Role> roles = new ArrayList<>(List.of(Role.values()));
            Collections.shuffle(roles);

            //Add players to list
            players = new ArrayList<>();
            for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                players.add(new Player("Player" + (i+1), roles.getLast()));
                roles.removeLast();
                FXMLUtil.updatePlayerLocation(players.get(i));
                FXMLUtil.updatePlayerRole(players.get(i));
            }

            //Add cards to infection pile and shuffle
            infectionCardPile = new ArrayList<>(cityCards);
            Collections.shuffle(infectionCardPile);
            infectionDiscardPile = new ArrayList<>();

            //Add initial cards to player card pile and shuffle
            playerCardPile = new ArrayList<>(cityCards);
            playerCardPile.addAll(eventCards);
            Collections.shuffle(playerCardPile);

            //Players draw cards
            for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                for (int j = 0; j < 6 - GameState.NUMBER_OF_PLAYERS; j++){
                    playerDrawCard(i);
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
                    for (int k = 0; k < 3 - i; k++)
                        drawInfectionCard(true);
                }
            }

            ControlUtil.showPhaseOneControls(players.getFirst());
        }
    }

    public static void playerDrawCard(Integer playerIndex) {
        players.get(playerIndex).addCardToHand(playerCardPile.getLast());
        playerCardPile.removeLast();
        refreshPlayerHand(playerIndex);
    }

    public static void refreshPlayerHand(Integer playerIndex) {
        GridPane playerHandGrid = (GridPane) FXMLUtil.getNodeById("player" + (playerIndex+1) + "Hand", GameController._gamePane);
        playerHandGrid.getChildren().clear();
        List<Card> playerHandCards = players.get(playerIndex).getHand();
        for (int k = 0; k < playerHandCards.size(); k++) {
            Card tmpCard = players.get(playerIndex).getHand().get(k);
            Button tmpButton = new Button(tmpCard.getName().substring(0, 1).toUpperCase() + tmpCard.getName().substring(1));
            GridPane.setMargin(tmpButton, new Insets(10, 10, 10, 10));
            tmpButton.setUserData(tmpCard);
            tmpButton.setId("player" + (playerIndex+1) + tmpCard.getName());
            tmpButton.setCursor(Cursor.HAND);
            GridPane.setHalignment(tmpButton, HPos.CENTER);
            if (tmpCard instanceof CityCard cc) {
                tmpButton.getStyleClass().add("card" + cc.getColor().substring(0, 1).toUpperCase() + cc.getColor().substring(1));
            } else if (tmpCard instanceof EventCard) {
                tmpButton.getStyleClass().add("cardEvent");
            }
            playerHandGrid.add(tmpButton, k%4, k/4);
        }
    }

    public void drawInfectionCard(boolean startOfgame) {
        CityCard drawnCard = infectionCardPile.getLast();
        infectionCardPile.removeLast();
        infectionDiscardPile.add(drawnCard);

        if (!startOfgame) {
            outbreaksInCitiesInCurrentChain = new ArrayList<>();
            infectCity(drawnCard.getName(), drawnCard.getColor());
        }
    }

    public void infectAdjacentCities(String sourceCityName, String diseaseColor) {
        List<String> citiesToInfect = CityGraph.cityGraph.get(sourceCityName);

        for (String s : citiesToInfect) {
            if (!outbreaksInCitiesInCurrentChain.contains(s)) {
                infectCity(s, diseaseColor);
            }
        }

    }

    public void infectCity(String cityName, String diseaseColor) {
        for(City cityToInfect : cities) {
            if (cityToInfect.getName().equals(cityName)) {

                boolean toOutbreak = cityToInfect.infect(diseaseColor);

                if (toOutbreak) {
                    outbreaksInCitiesInCurrentChain.add(cityName);
                    GameState.OUTBREAK_COUNTER++;
                    FXMLUtil.moveOutbreakToken(_gamePane);
                    infectAdjacentCities(cityName, diseaseColor);
                }
            }
        }
    }


}