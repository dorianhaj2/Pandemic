package hr.game.pandemic;

import hr.game.pandemic.jndi.ConfigurationKey;
import hr.game.pandemic.jndi.ConfigurationReader;
import hr.game.pandemic.model.*;
import hr.game.pandemic.rmi.RemoteService;
import hr.game.pandemic.threads.RefreshChatThread;
import hr.game.pandemic.util.ControlUtils;
import hr.game.pandemic.util.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameController {
    @FXML
    private Pane gamePane;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField chatTextInput;
    @FXML
    private MenuItem newGame;
    @FXML
    private MenuItem saveGame;
    @FXML
    private MenuItem loadGame;
    public static boolean SETUP;
    public static Label waitToStartLabel;
    public static Pane _gamePane;
    public static List<CityCard> infectionCardPile;
    public static List<CityCard> infectionDiscardPile;
    public static List<Card> playerCardPile;
    public static List<Card> playerDiscardPile;
    public static List<EventCard> eventCards = new ArrayList<>();
    public static List<CityCard> cityCards = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();
    public static List<City> cities = new ArrayList<>();
    public static List<List<String>> citiesColors = new ArrayList<>();
    public static List<String> outbreaksInCitiesInCurrentChain = new ArrayList<>();
    private RemoteService service;
    public void initialize() throws InterruptedException {
        _gamePane = gamePane;

        waitToStartLabel = new Label("Waiting for host to start the game...");
        waitToStartLabel.setId("waitToStartLabel");
        waitToStartLabel.setLayoutX(1560);
        waitToStartLabel.setLayoutY(500);
        waitToStartLabel.setVisible(false);
        gamePane.getChildren().add(waitToStartLabel);

        chatTextInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        sendChatMessage();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!GameApplication.player.equals(PlayerEnum.PLAYER1)) {
            newGame.setDisable(true);
            saveGame.setDisable(true);
            loadGame.setDisable(true);
        }

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
            PlayersDiseasesOnCityUtil.addGridViewForPlayersAndImagesAndLabelsForDiseasesToCity(c);
        }

        while(GameState.DIFFICULTY == null)
            NewGameUtil.newGame();

        Registry registry = null;
        try {
            Integer rmiPort = Integer.parseInt(ConfigurationReader.getValue(ConfigurationKey.RMI_PORT));
            String rmiHost = ConfigurationReader.getValue(ConfigurationKey.RMI_HOST);
            registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
            service = (RemoteService) registry.lookup(RemoteService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        new Thread(new RefreshChatThread(chatTextArea)).start();
    }

    public void newGame() {
        try {
            NewGameUtil.newGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendChatMessage() throws RemoteException {
        String chatMessageString = chatTextInput.getText();
        ChatMessage newChatMessage = new ChatMessage(
                GameApplication.player.name(),
                LocalDateTime.now(),
                chatMessageString);
        chatTextInput.setText("");
        service.sendMessage(newChatMessage);
    }

    public void cityButtonPressed(Event event) {
        if (event.getSource() instanceof Button b) {
            MovementActionUtils.clickCity(b);
        }
    }

    public void loadGame() {
        SaveLoadUtil.loadGame();
    }

    public void saveGame() {
        SaveLoadUtil.saveGame();
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

}