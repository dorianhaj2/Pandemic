package hr.game.pandemic.util;

import hr.game.pandemic.GameApplication;
import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import hr.game.pandemic.model.roles.Medic;
import hr.game.pandemic.model.roles.QuarantineSpecialist;
import hr.game.pandemic.model.roles.Researcher;
import hr.game.pandemic.model.roles.Scientist;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewGameUtil {
    public static void newGame() throws InterruptedException {
        if (GameApplication.player.equals(PlayerEnum.PLAYER1)) {
            GameState.START_GAME = DialogUtils.showNewGameDialog();
            if (GameState.START_GAME) {
                System.out.println("New game started");
                GameState.NUMBER_OF_TURNS = 0;
                GameState.OUTBREAK_COUNTER = 0;
                GameState.INFECTION_RATE = 1;
                GameState.YELLOW_CUBES = 24;
                GameState.RED_CUBES = 24;
                GameState.BLUE_CUBES = 24;
                GameState.BLACK_CUBES = 24;
                GameState.RESEARCH_STATIONS = 5;
                GameState.YELLOW_CURE = false;
                GameState.RED_CURE = false;
                GameState.BLUE_CURE = false;
                GameState.BLACK_CURE = false;
                GameState.YELLOW_ERADICATED = false;
                GameState.RED_ERADICATED = false;
                GameState.BLUE_ERADICATED = false;
                GameState.BLACK_ERADICATED = false;
                GameState.END_GAME = false;
                GameController.SETUP = true;

                for (Node node : FXMLUtils.getNodesByIdStartsWith("outbreak", GameController._gamePane)) {
                    ImageView iv = (ImageView) node;
                    iv.setVisible(false);
                }

                for (Node node : FXMLUtils.getNodesByIdStartsWith("infectionRate", GameController._gamePane)) {
                    ImageView iv = (ImageView) node;
                    iv.setVisible(false);
                }
                ImageView ivInfectionRate1 = (ImageView) FXMLUtils.getNodeById("infectionRate1", GameController._gamePane);
                ivInfectionRate1.setVisible(true);

                GridPane playersGrid = (GridPane) FXMLUtils.getNodeById("playersGridPane", GameController._gamePane);
                List<Node> nodesToRemove = new ArrayList<>();
                for (Node node : playersGrid.getChildren()) {
                    if (node.getId().startsWith("player"))
                        nodesToRemove.add(node);
                }
                playersGrid.getChildren().removeAll(nodesToRemove);
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    FXMLUtils.addPlayerToPlayersGrid(playersGrid, i + 1);
                }

                for (City city : GameController.cities) {
                    city.setDiseases(new ArrayList<>());
                }

                List<Role> roles = new ArrayList<>(List.of(Role.values()));
                Collections.shuffle(roles);

                //Remove players from board on new game
                if (!GameController.players.isEmpty()) {
                    for (Player p : GameController.players) {
                        p.setCurrentCity("");
                        FXMLUtils.updatePlayerLocation(p);
                    }
                }
                //Add new players to list
                GameController.players = new ArrayList<>();
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    if (roles.getLast().equals(Role.MEDIC)) {
                        GameController.players.add(new Medic("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.QUARANTINE_SPECIALIST)) {
                        GameController.players.add(new QuarantineSpecialist("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.RESEARCHER)) {
                        GameController.players.add(new Researcher("Player" + (i + 1)));
                    } else if (roles.getLast().equals(Role.SCIENTIST)) {
                        GameController.players.add(new Scientist("Player" + (i + 1)));
                    }
                    roles.removeLast();
                    FXMLUtils.updatePlayerLocation(GameController.players.get(i));
                    FXMLUtils.updatePlayerRole(GameController.players.get(i));
                }

                //Add cards to infection pile and shuffle
                GameController.infectionCardPile = new ArrayList<>(GameController.cityCards);
                Collections.shuffle(GameController.infectionCardPile);
                GameController.infectionDiscardPile = new ArrayList<>();

                //Add initial cards to player card pile and shuffle, empty player discard pile
                GameController.playerCardPile = new ArrayList<>(GameController.cityCards);
                GameController.playerCardPile.addAll(GameController.eventCards);
                Collections.shuffle(GameController.playerCardPile);
                GameController.playerDiscardPile = new ArrayList<>();

                //Empty research station list and add Atlanta
                ControlUtils.citiesWithResearchStation.clear();
                for (City c : GameController.cities) {
                    if (c.getName().equals("atlanta"))
                        ControlUtils.citiesWithResearchStation.add(c);
                    else {
                        c.setResearchStation(false);
                        Button cButton = (Button) FXMLUtils.getNodeById(c.getName(), GameController._gamePane);
                        cButton.getStyleClass().remove("research_station");
                    }
                }

                //Players draw cards
                for (int i = 0; i < GameState.NUMBER_OF_PLAYERS; i++) {
                    for (int j = 0; j < 6 - GameState.NUMBER_OF_PLAYERS; j++) {
                        DrawDiscardUtil.playerDrawCard(GameController.players.get(i));
                    }
                }

                //Add epidemic cards to player card pile and shuffle
                for (int i = 0; i < GameState.DIFFICULTY + 3; i++) {
                    GameController.playerCardPile.add(new EpidemicCard());
                }
                Collections.shuffle(GameController.playerCardPile);

                //Start of game infections
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        DrawDiscardUtil.drawInfectionCard();
                        InfectUtil.infectCity(GameController.infectionDiscardPile.getLast().getName(), GameController.infectionDiscardPile.getLast().getColor(), 3 - i);
                    }
                }

                UpdateGameBoardUtil.updateBoard();
                ControlUtils.currentPlayer = GameController.players.getFirst();
                GameController.SETUP = false;
                GameApplication.client.sendGameState();
                ControlUtils.startTurn();
                ControlUtils.showOrHideControlsDependingOnCurrentPlayer(false);
            }
        } else {
                GameState.DIFFICULTY = 1;
                GameController.waitToStartLabel.setVisible(true);
        }

    }
}
