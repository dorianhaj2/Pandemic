package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.model.GameStateDTO;
import javafx.scene.control.Alert;

import java.io.*;

public class SaveLoadUtil {

    private static final String SAVE_GAME_FILE_NAME = "files/save.bin";

    public static void saveGame() {
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

    public static void loadGame() {
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

    private static void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
