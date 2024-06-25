package hr.game.pandemic.model;

import java.io.Serializable;

public class CityCard extends Card implements Serializable {
    private String color;

    public CityCard() {
    }

    public CityCard(String name) {
        super(name);
    }

    public CityCard(String name, String color) {
        super(name);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
