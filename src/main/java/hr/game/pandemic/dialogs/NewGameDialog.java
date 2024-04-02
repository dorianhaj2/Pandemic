package hr.game.pandemic.dialogs;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class NewGameDialog {

    @FXML
    private RadioButton diffEasy;
    @FXML
    private RadioButton diffNormal;
    @FXML
    private RadioButton diffHard;
    @FXML
    private RadioButton numTwo;
    @FXML
    private RadioButton numThree;
    @FXML
    private RadioButton numFour;
    @FXML
    private ToggleGroup difficulty;
    @FXML
    private ToggleGroup numberOfPlayers;

    public void initialize(){
        diffEasy.setUserData(1);
        diffNormal.setUserData(2);
        diffHard.setUserData(3);
        numTwo.setUserData(2);
        numThree.setUserData(3);
        numFour.setUserData(4);

    }

    public void setDifficulty() {
        Integer diff = (Integer)difficulty.getSelectedToggle().getUserData();

        GameState.DIFFICULTY = diff;
    }
    public void setNumOfPlayers() {
        Integer numOfPlayers = (Integer)numberOfPlayers.getSelectedToggle().getUserData();

        GameState.NUMBER_OF_PLAYERS = numOfPlayers;
    }

}
