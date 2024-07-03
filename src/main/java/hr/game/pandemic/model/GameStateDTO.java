package hr.game.pandemic.model;

import hr.game.pandemic.GameController;
import hr.game.pandemic.util.ControlUtils;
import hr.game.pandemic.util.EventsUtils;
import hr.game.pandemic.util.FXMLUtils;
import hr.game.pandemic.util.UpdateGameBoardUtil;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameStateDTO implements Serializable {

    private Integer NUMBER_OF_TURNS;
    private Integer DIFFICULTY;
    private Integer NUMBER_OF_PLAYERS;
    private Integer OUTBREAK_COUNTER;
    private Integer INFECTION_RATE;
    private Integer YELLOW_CUBES;
    private Integer RED_CUBES;
    private Integer BLUE_CUBES;
    private Integer BLACK_CUBES;
    private Integer RESEARCH_STATIONS;
    private boolean START_GAME;
    private boolean YELLOW_CURE;
    private boolean RED_CURE;
    private boolean BLUE_CURE;
    private boolean BLACK_CURE;
    private boolean YELLOW_ERADICATED;
    private boolean RED_ERADICATED;
    private boolean BLUE_ERADICATED;
    private boolean BLACK_ERADICATED;
    private boolean END_GAME;
    private boolean WIN;
    private String reason;
    private boolean EASY_MODE;
    private boolean oneQuietNightPlayed;
    private List<Player> players;
    private List<City> cities;
    public List<CityCard> infectionCardPile;
    public List<CityCard> infectionDiscardPile;
    public List<Card> playerCardPile;
    public List<Card> playerDiscardPile;
    private List<City> citiesWithResearchStation;

    public GameStateDTO() {
        NUMBER_OF_TURNS = GameState.NUMBER_OF_TURNS;
        DIFFICULTY = GameState.DIFFICULTY;
        NUMBER_OF_PLAYERS = GameState.NUMBER_OF_PLAYERS;
        OUTBREAK_COUNTER = GameState.OUTBREAK_COUNTER;
        INFECTION_RATE = GameState.INFECTION_RATE;
        YELLOW_CUBES = GameState.YELLOW_CUBES;
        RED_CUBES = GameState.RED_CUBES;
        BLUE_CUBES = GameState.BLUE_CUBES;
        BLACK_CUBES = GameState.BLACK_CUBES;
        RESEARCH_STATIONS = GameState.RESEARCH_STATIONS;
        START_GAME = GameState.START_GAME;
        YELLOW_CURE = GameState.YELLOW_CURE;
        RED_CURE = GameState.RED_CURE;
        BLUE_CURE = GameState.BLUE_CURE;
        BLACK_CURE = GameState.BLACK_CURE;
        YELLOW_ERADICATED = GameState.YELLOW_ERADICATED;
        RED_ERADICATED = GameState.RED_ERADICATED;
        BLUE_ERADICATED = GameState.BLUE_ERADICATED;
        BLACK_ERADICATED = GameState.BLACK_ERADICATED;
        END_GAME = GameState.END_GAME;
        WIN = GameState.WIN;
        reason = GameState.reason;
        EASY_MODE = GameState.EASY_MODE;
        oneQuietNightPlayed = EventsUtils.oneQuietNightPlayed;
        players = new ArrayList<>(GameController.players);
        cities = new ArrayList<>(GameController.cities);
        infectionCardPile = new ArrayList<>(GameController.infectionCardPile);
        infectionDiscardPile = new ArrayList<>(GameController.infectionDiscardPile);
        playerCardPile = new ArrayList<>(GameController.playerCardPile);
        playerDiscardPile = new ArrayList<>(GameController.playerDiscardPile);
        citiesWithResearchStation = new ArrayList<>(ControlUtils.citiesWithResearchStation);
    }

    public void setGameState() {
        GameState.NUMBER_OF_TURNS = NUMBER_OF_TURNS;
        GameState.DIFFICULTY = DIFFICULTY;
        GameState.NUMBER_OF_PLAYERS = NUMBER_OF_PLAYERS;
        GameState.OUTBREAK_COUNTER = OUTBREAK_COUNTER;
        GameState.INFECTION_RATE = INFECTION_RATE;
        GameState.YELLOW_CUBES = YELLOW_CUBES;
        GameState.RED_CUBES = RED_CUBES;
        GameState.BLUE_CUBES = BLUE_CUBES;
        GameState.BLACK_CUBES = BLACK_CUBES;
        GameState.RESEARCH_STATIONS = RESEARCH_STATIONS;
        GameState.START_GAME = START_GAME;
        GameState.YELLOW_CURE = YELLOW_CURE;
        GameState.RED_CURE = RED_CURE;
        GameState.BLUE_CURE = BLUE_CURE;
        GameState.BLACK_CURE = BLACK_CURE;
        GameState.YELLOW_ERADICATED = YELLOW_ERADICATED;
        GameState.RED_ERADICATED = RED_ERADICATED;
        GameState.BLUE_ERADICATED = BLUE_ERADICATED;
        GameState.BLACK_ERADICATED = BLACK_ERADICATED;
        GameState.END_GAME = END_GAME;
        GameState.WIN = WIN;
        GameState.reason = reason;
        GameState.EASY_MODE = EASY_MODE;
        EventsUtils.oneQuietNightPlayed = oneQuietNightPlayed;
        GameController.players = players;
        GameController.cities = cities;
        GameController.infectionCardPile =infectionCardPile;
        GameController.infectionDiscardPile = infectionDiscardPile;
        GameController.playerCardPile = playerCardPile;
        GameController.playerDiscardPile = playerDiscardPile;
        ControlUtils.citiesWithResearchStation = citiesWithResearchStation;
        ControlUtils.setCurrentPlayerBasedOnNumberOfTurns();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                javafxChanges();
            }
        });
    }

    private void javafxChanges() {
        FXMLUtils.moveOutbreakToken();
        FXMLUtils.moveInfectionRateToken();
        UpdateGameBoardUtil.updateBoard();
        UpdateGameBoardUtil.hideWaitToStartLabel();
        ControlUtils.showPhaseOneControls();
        ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
        if (GameState.END_GAME) {
            GameController.endGame(GameState.WIN, GameState.reason, true);
        }
    }

    @Override
    public String toString() {
        return "GameStateDTO{" +
                "NUMBER_OF_TURNS=" + NUMBER_OF_TURNS +
                ", DIFFICULTY=" + DIFFICULTY +
                ", NUMBER_OF_PLAYERS=" + NUMBER_OF_PLAYERS +
                ", OUTBREAK_COUNTER=" + OUTBREAK_COUNTER +
                ", INFECTION_RATE=" + INFECTION_RATE +
                ", YELLOW_CUBES=" + YELLOW_CUBES +
                ", RED_CUBES=" + RED_CUBES +
                ", BLUE_CUBES=" + BLUE_CUBES +
                ", BLACK_CUBES=" + BLACK_CUBES +
                ", RESEARCH_STATIONS=" + RESEARCH_STATIONS +
                ", START_GAME=" + START_GAME +
                ", YELLOW_CURE=" + YELLOW_CURE +
                ", RED_CURE=" + RED_CURE +
                ", BLUE_CURE=" + BLUE_CURE +
                ", BLACK_CURE=" + BLACK_CURE +
                ", YELLOW_ERADICATED=" + YELLOW_ERADICATED +
                ", RED_ERADICATED=" + RED_ERADICATED +
                ", BLUE_ERADICATED=" + BLUE_ERADICATED +
                ", BLACK_ERADICATED=" + BLACK_ERADICATED +
                ", END_GAME=" + END_GAME +
                ", EASY_MODE=" + EASY_MODE +
                '}';
    }
}
