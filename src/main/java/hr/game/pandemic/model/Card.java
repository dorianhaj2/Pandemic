package hr.game.pandemic.model;

import java.io.Serializable;

public class Card implements Serializable {

    private String name;

    public Card() {
    }

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
