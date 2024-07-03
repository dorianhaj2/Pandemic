package hr.game.pandemic.model;

import hr.game.pandemic.GameController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {
    private String name;
    private String color;
    private List<String> diseases;
    private boolean researchStation;

    public City(String name, String color, boolean researchStation) {
        this.name = name;
        this.color = color;
        this.diseases = new ArrayList<>();
        this.researchStation = researchStation;
    }

    public City(String name, String color){
        this.name = name;
        this.color = color;
        this.diseases = new ArrayList<>();
        this.researchStation = false;
    }

    public boolean infect(String disease, int amount) {
        int n = 0;

        if (this.diseases.contains(disease)) {
            for (String d : this.diseases) {
                if (d.equals(disease))
                {
                    n++;
                }

            }
        }

        for (int i = 0; i < amount; i++) {
            if (n < 3){
                if (!((GameState.BLACK_CURE && disease.equals("black")
                        || GameState.BLUE_CURE && disease.equals("blue")
                        || GameState.YELLOW_CURE && disease.equals("yellow")
                        || GameState.RED_CURE && disease.equals("red")))){
                    this.diseases.add(disease);
                    if (disease.equals("yellow"))
                        GameState.YELLOW_CUBES--;
                    if (disease.equals("red"))
                        GameState.RED_CUBES--;
                    if (disease.equals("blue"))
                        GameState.BLUE_CUBES--;
                    if (disease.equals("black"))
                        GameState.BLACK_CUBES--;
                    n++;
                }
            } else {
                if (GameState.YELLOW_CUBES < 0 || GameState.BLACK_CUBES < 0 || GameState.BLUE_CUBES < 0 || GameState.RED_CUBES < 0) {
                    GameController.endGame(false, "No " + disease + " disease cubes left!", false);
                    return false;
                }

                return true;
            }
        }
        if (GameState.YELLOW_CUBES < 0 || GameState.BLACK_CUBES < 0 || GameState.BLUE_CUBES < 0 || GameState.RED_CUBES < 0)
            GameController.endGame(false, "No " + disease + " disease cubes left!", false);
        return false;
    }

    public int getBlueDiseases() {
        int n = 0;
        for (String d : this.diseases) {
            if (d.equals("blue"))
                n++;
        }
        return n;
    }

    public int getBlackDiseases() {
        int n = 0;
        for (String d : this.diseases) {
            if (d.equals("black"))
                n++;
        }
        return n;
    }

    public int getRedDiseases() {
        int n = 0;
        for (String d : this.diseases) {
            if (d.equals("red"))
                n++;
        }
        return n;
    }

    public int getYellowDiseases() {
        int n = 0;
        for (String d : this.diseases) {
            if (d.equals("yellow"))
                n++;
        }
        return n;
    }

    public void cureOneOfDisease(String color) {
        if (color.equals("yellow"))
            GameState.YELLOW_CUBES++;
        if (color.equals("red"))
            GameState.RED_CUBES++;
        if (color.equals("blue"))
            GameState.BLUE_CUBES++;
        if (color.equals("black"))
            GameState.BLACK_CUBES++;
        this.diseases.remove(color);
    }

    public void cureAllOfDisease(String color) {
        if (color.equals("yellow"))
            GameState.YELLOW_CUBES += getYellowDiseases();
        if (color.equals("red"))
            GameState.RED_CUBES += getRedDiseases();
        if (color.equals("blue"))
            GameState.BLUE_CUBES += getBlueDiseases();
        if (color.equals("black"))
            GameState.BLACK_CUBES += getBlackDiseases();
        this.diseases.removeIf(color::equals);
    }
}