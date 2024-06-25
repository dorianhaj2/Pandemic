package hr.game.pandemic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private String name;
    private List<Card> hand;
    private String previousCity;
    private String currentCity;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.currentCity = "atlanta";
        this.previousCity = "";
    }

    public Player(String name, List<Card> hand, String currentCity) {
        this.name = name;
        this.hand = hand;
        this.currentCity = currentCity;
    }

    public Player(String name, List<Card> hand) {
        this.name = name;
        this.hand = hand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        this.hand.remove(card);
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getPreviousCity() {
        return previousCity;
    }

    public void setPreviousCity(String previousCity) {
        this.previousCity = previousCity;
    }

    public String getRoleName() {
        return "Player";
    }
}
