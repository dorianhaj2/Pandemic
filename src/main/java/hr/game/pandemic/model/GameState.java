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

    public static int getCurrentPlayerNumber() {
        return GameState.NUMBER_OF_TURNS % GameState.NUMBER_OF_PLAYERS;
    }

}
