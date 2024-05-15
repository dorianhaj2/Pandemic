package hr.game.pandemic.model;

import java.util.ArrayList;
import java.util.List;

public class City {
    private String name;
    private String color;
    private List<String> diseases;
    private boolean researchStation;
    public City() {
    }

    public City(String name, String color, List<String> diseases, boolean researchStation) {
        this.name = name;
        this.color = color;
        this.diseases = diseases;
        this.researchStation = researchStation;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<String> diseases) {
        this.diseases = diseases;
    }

    public boolean isResearchStation() {
        return researchStation;
    }

    public void setResearchStation(boolean researchStation) {
        this.researchStation = researchStation;
    }

    public boolean infect(String disease, int amount) {
        int n = 0;
        System.out.println("Infecting " + name);
        if (this.diseases.contains(disease)) {
            for (String d : this.diseases) {
                if (d.equals(disease))
                    n++;
            }
        }

        for (int i = 0; i < amount; i++) {
            if (n < 3){
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
            } else {
                return true;
            }
        }
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