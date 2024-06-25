package hr.game.pandemic.model;

public class GameState {
    public static Integer NUMBER_OF_TURNS;
    public static Integer NUMBER_OF_ACTIONS;
    public static Integer DIFFICULTY;
    public static Integer NUMBER_OF_PLAYERS;
    public static Integer OUTBREAK_COUNTER;
    public static Integer INFECTION_RATE;
    public static Integer YELLOW_CUBES;
    public static Integer RED_CUBES;
    public static Integer BLUE_CUBES;
    public static Integer BLACK_CUBES;
    public static Integer RESEARCH_STATIONS;
    public static boolean START_GAME;
    public static boolean YELLOW_CURE;
    public static boolean RED_CURE;
    public static boolean BLUE_CURE;
    public static boolean BLACK_CURE;
    public static boolean YELLOW_ERADICATED;
    public static boolean RED_ERADICATED;
    public static boolean BLUE_ERADICATED;
    public static boolean BLACK_ERADICATED;
    public static boolean END_GAME;
    public static boolean EASY_MODE;

    public static int getCurrentPlayerNumber() {
        return (NUMBER_OF_TURNS % NUMBER_OF_PLAYERS) + 1;
    }



}
