package hr.game.pandemic.model;

public class CityCard extends Card{
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
