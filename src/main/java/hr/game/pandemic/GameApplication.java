package hr.game.pandemic;

import hr.game.pandemic.exeption.WrongPlayerNameException;
import hr.game.pandemic.model.PlayerEnum;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumSet;

public class GameApplication extends Application {
    public static PlayerEnum player;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("pandemic-main.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle(player.name());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        String firstArg = "";
        if(args.length > 0)
            firstArg = args[0];

        boolean z = false;

        for (PlayerEnum value : EnumSet.allOf(PlayerEnum.class)) {
            if (firstArg.equals(value.name())) {
                z = true;
                break;
            }
        }

        if (z) {
            player = PlayerEnum.valueOf(firstArg);
        } else {
            throw new WrongPlayerNameException("The game was started with an invalid player name");
        }

        launch();

    }
}