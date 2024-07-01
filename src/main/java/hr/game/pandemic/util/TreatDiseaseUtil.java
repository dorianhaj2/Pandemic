package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.roles.Medic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class TreatDiseaseUtil {
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
            FXMLUtils.refreshCityDiseases(city);
            FXMLUtils.refreshDiseaseCubeCount();
            MovementActionUtils.actionDone();
        }
    }

    public static void setDiseaseEradicated(String color) {
        ImageView cureImageView = (ImageView) FXMLUtils.getNodeById(color + "Cure", GameController._gamePane);
        cureImageView.setImage(new Image("images\\" + color + "_eradicated.png"));
    }
}
