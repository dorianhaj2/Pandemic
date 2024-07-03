package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.Player;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FXMLUtils {
    public static void moveOutbreakToken() {
        if (GameState.OUTBREAK_COUNTER > 0) {
            for (int i = 1; i <= 8; i++) {
                ImageView iv = (ImageView) getNodeById("outbreak" + i, GameController._gamePane);
                iv.setVisible(i == GameState.OUTBREAK_COUNTER);
            }
        }
    }
    public static void moveInfectionRateToken() {
        for (int i = 1; i <= 7; i++) {
            ImageView iv = (ImageView) getNodeById("infectionRate" + i, GameController._gamePane);
            iv.setVisible(i == GameState.INFECTION_RATE);
        }
    }
    public static void addPlayerToPlayersGrid(GridPane playersGrid, Integer playerNumber){
        GridPane playerGridPane = new GridPane();
        playerGridPane.setId("player" + playerNumber + "Grid");
        RowConstraints rowc1 = new RowConstraints();
        rowc1.setVgrow(Priority.ALWAYS);
        playerGridPane.getRowConstraints().add(rowc1);

        FlowPane titleFlowPane = new FlowPane();

        Label playerName = new Label("Player" + playerNumber);
        FlowPane.setMargin(playerName, new Insets(15, 15, 15, 15));
        playerName.setFont(new Font("System Bold", 14));

        Label colon = new Label(":");
        FlowPane.setMargin(colon, new Insets(15, 15, 15, 15));
        colon.setFont(new Font("System Bold", 14));

        Label playerRole = new Label("Role");
        playerRole.setFont(new Font("System Bold", 14));
        playerRole.setId("player" + playerNumber + "Role");
        FlowPane.setMargin(playerRole, new Insets(15, 15, 15, 15));

        Label playerLocation = new Label("Location");
        playerLocation.setFont(new Font("System Bold", 14));
        playerLocation.setId("player" + playerNumber + "Location");
        FlowPane.setMargin(playerLocation, new Insets(15, 15, 15, 15));

        titleFlowPane.getChildren().add(playerName);
        titleFlowPane.getChildren().add(colon);
        titleFlowPane.getChildren().add(playerRole);
        titleFlowPane.getChildren().add(playerLocation);

        GridPane playerHandGridPane = new GridPane(4, 2);
        ColumnConstraints colc = new ColumnConstraints();
        colc.setPercentWidth(100d / 4);
        RowConstraints rowc2 = new RowConstraints();
        rowc2.setPercentHeight(100d / 2);
        playerHandGridPane.getColumnConstraints().addAll(colc, colc, colc, colc);
        playerHandGridPane.getRowConstraints().addAll(rowc2, rowc2);
        playerHandGridPane.setId("player" + playerNumber + "Hand");

        playerGridPane.add(titleFlowPane, 0,0);
        playerGridPane.add(playerHandGridPane, 0, 1);
        playersGrid.getChildren().removeIf(node -> node.getId().equals("player" + playerNumber + "Grid"));
        playersGrid.add(playerGridPane, 0,playerNumber - 1);
    }
    public static void refreshCityDiseases(City city) {
        Label redDiseaseLabel = (Label) getNodeById(city.getName() + "RedCount", GameController._gamePane);
        redDiseaseLabel.setText(Integer.toString(city.getRedDiseases()));
        Label yellowDiseaseLabel = (Label) getNodeById(city.getName() + "YellowCount", GameController._gamePane);
        yellowDiseaseLabel.setText(Integer.toString(city.getYellowDiseases()));
        Label blueDiseaseLabel = (Label) getNodeById(city.getName() + "BlueCount", GameController._gamePane);
        blueDiseaseLabel.setText(Integer.toString(city.getBlueDiseases()));
        Label blackDiseaseLabel = (Label) getNodeById(city.getName() + "BlackCount", GameController._gamePane);
        blackDiseaseLabel.setText(Integer.toString(city.getBlackDiseases()));
    }
    public static Node getNodeById(String id, Parent parent){
        Node result = null;
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (result == null) {
                if (node.getId() != null) {
                    if (node.getId().equals(id)) {
                        return node;
                    }
                }
                if (Parent.class.isAssignableFrom(node.getClass())) {
                        result = getNodeById(id, (Parent) node);
                }

            }
        }
        return result;
    }
    public static  List<Node> getNodesByIdStartsWith(String id, Parent parent){
        List<Node> result = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node.getId() != null) {
                if (node.getId().startsWith(id)) {
                    result.add(node);
                }
            }
            if (Parent.class.isAssignableFrom(node.getClass())) {
                result.addAll(getNodesByIdStartsWith(id, (Parent) node));
            }
        }
        return result;
    }
    public static void updatePlayerRole(Player player) {
        String roleName = player.getRoleName();

        Label roleLabel = (Label) getNodeById(player.getName().toLowerCase() + "Role", GameController._gamePane);
        roleLabel.setText("");
        roleLabel.setText(roleName);
    }
    public static void updatePlayerLocation(Player player) {
        if (!player.getCurrentCity().isEmpty()) {
            for (City city : GameController.cities) {
                if (!city.getName().equals(player.getCurrentCity())) {
                    ImageView cityPlayerImage = (ImageView) getNodeById(city.getName() + player.getName() + "Image", GameController._gamePane);
                    cityPlayerImage.setImage(null);
                }
            }
            String cityName = player.getCurrentCity().substring(0, 1).toUpperCase() + player.getCurrentCity().substring(1);
            String cityNameNormalCase = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(cityName), StringUtils.SPACE);
            Label cityLabel = (Label) getNodeById(player.getName().toLowerCase() + "Location", GameController._gamePane);
            cityLabel.setText(cityNameNormalCase);

            ImageView cityPlayerImage = (ImageView) getNodeById(player.getCurrentCity() + player.getName() + "Image", GameController._gamePane);

            try {
                List<File> files = getAllFilesFromResource("hr/game/pandemic/images/roles/pawns/");

                for (File f : files) {
                    if (f.getName().equals(player.getRoleName().toLowerCase().replaceAll(" ", "_") + "_pawn.png"))
                        cityPlayerImage.setImage(new Image(new FileInputStream(f)));
                }
                cityPlayerImage.setFitWidth(20);
                cityPlayerImage.setFitHeight(20);

            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static List<File> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
        ClassLoader classLoader = FXMLUtils.class.getClassLoader();
        URL resource = classLoader.getResource(folder);

        List<File> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        return collect;
    }

    public static void refreshDiseaseCubeCount() {
        Label yellowLabel = (Label) FXMLUtils.getNodeById("cubeCounterYellow", GameController._gamePane);
        Label blueLabel = (Label) FXMLUtils.getNodeById("cubeCounterBlue", GameController._gamePane);
        Label redLabel = (Label) FXMLUtils.getNodeById("cubeCounterRed", GameController._gamePane);
        Label blackLabel = (Label) FXMLUtils.getNodeById("cubeCounterBlack", GameController._gamePane);

        yellowLabel.setText(GameState.YELLOW_CUBES.toString());
        blueLabel.setText(GameState.BLUE_CUBES.toString());
        redLabel.setText(GameState.RED_CUBES.toString());
        blackLabel.setText(GameState.BLACK_CUBES.toString());
    }
}