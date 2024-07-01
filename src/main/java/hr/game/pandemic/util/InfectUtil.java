package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.CityGraph;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.Player;
import hr.game.pandemic.model.roles.QuarantineSpecialist;

import java.util.List;

public class InfectUtil {
    public static void infectAdjacentCities(String sourceCityName, String diseaseColor) {
        List<String> citiesToInfect = CityGraph.cityGraph.get(sourceCityName);

        for (String s : citiesToInfect) {
            if (!GameController.outbreaksInCitiesInCurrentChain.contains(s) && !GameState.END_GAME) {
                infectCity(s, diseaseColor, 1);
            }
        }
    }

    public static void infectCity(String cityName, String diseaseColor, int amount) {
        for(City cityToInfect : GameController.cities) {
            if (cityToInfect.getName().equals(cityName)) {
                QuarantineSpecialist potentialQuarantineSpecialistPlayer = null;
                for (Player player : GameController.players) {
                    if (player instanceof QuarantineSpecialist) {
                        potentialQuarantineSpecialistPlayer = (QuarantineSpecialist) player;
                    }
                }
                boolean toInfect = true;
                if (!GameController.SETUP) {
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
                        GameController.outbreaksInCitiesInCurrentChain.add(cityName);
                        GameState.OUTBREAK_COUNTER++;
                        FXMLUtils.moveOutbreakToken();
                        if (GameState.OUTBREAK_COUNTER == 8)
                            GameController.endGame(false, "Outbreak marker reached last space of the Outbreaks Track!");
                        else {
                            infectAdjacentCities(cityName, diseaseColor);
                        }
                    }
                }
            }
        }
    }
}
