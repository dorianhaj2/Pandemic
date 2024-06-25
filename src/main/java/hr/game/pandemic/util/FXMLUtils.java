package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import hr.game.pandemic.model.GameState;
import hr.game.pandemic.model.Player;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
            ImageView iv = (ImageView) getNodeById("outbreak" + GameState.OUTBREAK_COUNTER, GameController._gamePane);
            iv.setVisible(true);
            if (GameState.OUTBREAK_COUNTER > 1){
                ImageView ivPrev = (ImageView) getNodeById("outbreak" + (GameState.OUTBREAK_COUNTER - 1), GameController._gamePane);
                ivPrev.setVisible(false);
            }
        }
    }
    public static void moveInfectionRateToken() {
        ImageView iv = (ImageView) getNodeById("infectionRate" + GameState.INFECTION_RATE, GameController._gamePane);
        iv.setVisible(true);
        if (GameState.INFECTION_RATE > 1) {
            ImageView ivPrev = (ImageView) getNodeById("infectionRate" + (GameState.INFECTION_RATE - 1), GameController._gamePane);
            ivPrev.setVisible(false);
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
        if (!player.getPreviousCity().isEmpty()) {
            ImageView previousCityPlayerImage = (ImageView) getNodeById(player.getPreviousCity() + player.getName() + "Image", GameController._gamePane);
            previousCityPlayerImage.setImage(null);
        }
        if (!player.getCurrentCity().isEmpty()) {
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
    public static void addGridViewForPlayersAndImagesAndLabelsForDiseasesToCity(City c) {
        Button cityButton = (Button) FXMLUtils.getNodeById(c.getName(), GameController._gamePane);

        GridPane newCityGridPane = new GridPane();
        newCityGridPane.setId(c.getName() + "Grid");
        newCityGridPane.setLayoutX(cityButton.getLayoutX());
        newCityGridPane.setLayoutY(cityButton.getLayoutY());
        newCityGridPane.setMinWidth(40);
        newCityGridPane.setMinHeight(40);
        newCityGridPane.setMaxWidth(40);
        newCityGridPane.setMaxHeight(40);
        newCityGridPane.setMouseTransparent(true);
        ColumnConstraints colc = new ColumnConstraints();
        colc.setPercentWidth(100d / 2);
        RowConstraints rowc = new RowConstraints();
        rowc.setPercentHeight(100d / 2);
        newCityGridPane.getRowConstraints().addAll(rowc, rowc);
        newCityGridPane.getColumnConstraints().addAll(colc, colc);

        ImageView imageView1 = new ImageView();
        imageView1.setId(c.getName() + "Player1Image");
        imageView1.setCache(true);
        ImageView imageView2 = new ImageView();
        imageView2.setId(c.getName() + "Player2Image");
        imageView2.setCache(true);
        ImageView imageView3 = new ImageView();
        imageView3.setId(c.getName() + "Player3Image");
        imageView3.setCache(true);
        ImageView imageView4 = new ImageView();
        imageView4.setId(c.getName() + "Player4Image");
        imageView4.setCache(true);

        newCityGridPane.add(imageView1, 0 , 0);
        newCityGridPane.add(imageView2, 1 , 0);
        newCityGridPane.add(imageView3, 0 , 1);
        newCityGridPane.add(imageView4, 1 , 1);


        GameController._gamePane.getChildren().add(newCityGridPane);

        GridPane newCityDiseaseGridPane = new GridPane();
        newCityDiseaseGridPane.setId(c.getName() + "DiseaseGrid");
        int gridOffset = 25;
        if (c.getName().equals("sanFrancisco")) gridOffset = 18;
        newCityDiseaseGridPane.setLayoutX(cityButton.getLayoutX() - gridOffset);
        newCityDiseaseGridPane.setLayoutY(cityButton.getLayoutY() - 25);
        newCityDiseaseGridPane.setMouseTransparent(true);
        newCityDiseaseGridPane.setMaxHeight(90);
        newCityDiseaseGridPane.setMinHeight(90);
        newCityDiseaseGridPane.setMaxWidth(90);
        newCityDiseaseGridPane.setMaxWidth(90);
        ColumnConstraints colcD = new ColumnConstraints();
        colcD.setPercentWidth(100d / 3);
        RowConstraints rowcD = new RowConstraints();
        rowcD.setPercentHeight(100d / 3);
        newCityDiseaseGridPane.getRowConstraints().addAll(rowcD, rowcD, rowcD);
        newCityDiseaseGridPane.getColumnConstraints().addAll(colcD, colcD, colcD);

        ImageView imageViewRed = new ImageView();
        imageViewRed.setId(c.getName() + "RedCube");
        ImageView imageViewYellow = new ImageView();
        imageViewYellow.setId(c.getName() + "YellowCube");
        ImageView imageViewBlue = new ImageView();
        imageViewBlue.setId(c.getName() + "BlueCube");
        ImageView imageViewBlack = new ImageView();
        imageViewBlack.setId(c.getName() + "BlackCube");

        try {
            List<File> files = getAllFilesFromResource("hr/game/pandemic/images/");

            for (File f : files) {
                switch (f.getName()) {
                    case "black_cube.png" -> imageViewBlack.setImage(new Image(new FileInputStream(f)));
                    case "blue_cube.png" -> imageViewBlue.setImage(new Image(new FileInputStream(f)));
                    case "red_cube.png" -> imageViewRed.setImage(new Image(new FileInputStream(f)));
                    case "yellow_cube.png" -> imageViewYellow.setImage(new Image(new FileInputStream(f)));
                }
            }
            imageViewRed.setFitWidth(20);
            imageViewRed.setFitHeight(20);
            imageViewYellow.setFitWidth(20);
            imageViewYellow.setFitHeight(20);
            imageViewBlue.setFitWidth(20);
            imageViewBlue.setFitHeight(20);
            imageViewBlack.setFitWidth(20);
            imageViewBlack.setFitHeight(20);

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        StackPane redStackPane = new StackPane();
        redStackPane.setMaxHeight(30);
        redStackPane.setMaxWidth(30);
        redStackPane.setMinHeight(30);
        redStackPane.setMinWidth(30);
        StackPane.setMargin(redStackPane, new Insets(10));
        redStackPane.getChildren().add(imageViewRed);
        StackPane yellowStackPane = new StackPane();
        yellowStackPane.setMaxHeight(30);
        yellowStackPane.setMaxWidth(30);
        yellowStackPane.setMinHeight(30);
        yellowStackPane.setMinWidth(30);
        StackPane.setMargin(yellowStackPane, new Insets(10));
        yellowStackPane.getChildren().add(imageViewYellow);
        StackPane blueStackPane = new StackPane();
        blueStackPane.setMaxHeight(30);
        blueStackPane.setMaxWidth(30);
        blueStackPane.setMinHeight(30);
        blueStackPane.setMinWidth(30);
        StackPane.setMargin(blueStackPane, new Insets(10));
        blueStackPane.getChildren().add(imageViewBlue);
        StackPane blackStackPane = new StackPane();
        blackStackPane.setMaxHeight(30);
        blackStackPane.setMaxWidth(30);
        blackStackPane.setMinHeight(30);
        blackStackPane.setMinWidth(30);
        StackPane.setMargin(blackStackPane, new Insets(10));
        blackStackPane.getChildren().add(imageViewBlack);

        newCityDiseaseGridPane.add(redStackPane, 1 , 0);
        newCityDiseaseGridPane.add(yellowStackPane, 0 , 1);
        newCityDiseaseGridPane.add(blueStackPane, 2 , 1);
        newCityDiseaseGridPane.add(blackStackPane, 1 , 2);

        Label redCountLabel = new Label("0");
        redCountLabel.setId(c.getName() + "RedCount");
        redCountLabel.setLayoutX(newCityDiseaseGridPane.getLayoutX() + 41);
        redCountLabel.setLayoutY(newCityDiseaseGridPane.getLayoutY() + 6);
        redCountLabel.setMinHeight(20);
        redCountLabel.setMaxHeight(20);
        redCountLabel.setTextFill(Color.color(1, 1, 1));

        Label yellowCountLabel = new Label("0");
        yellowCountLabel.setId(c.getName() + "YellowCount");
        yellowCountLabel.setLayoutX(newCityDiseaseGridPane.getLayoutX() + 12);
        yellowCountLabel.setLayoutY(newCityDiseaseGridPane.getLayoutY() + 36);
        yellowCountLabel.setMinHeight(20);
        yellowCountLabel.setMaxHeight(20);

        Label blackCountLabel = new Label("0");
        blackCountLabel.setId(c.getName() + "BlackCount");
        blackCountLabel.setLayoutX(newCityDiseaseGridPane.getLayoutX() + 42);
        blackCountLabel.setLayoutY(newCityDiseaseGridPane.getLayoutY() + 64);
        blackCountLabel.setMinHeight(20);
        blackCountLabel.setMaxHeight(20);
        blackCountLabel.setTextFill(Color.color(1, 1, 1));

        Label blueCountLabel = new Label("0");
        blueCountLabel.setId(c.getName() + "BlueCount");
        blueCountLabel.setLayoutX(newCityDiseaseGridPane.getLayoutX() + 72);
        blueCountLabel.setLayoutY(newCityDiseaseGridPane.getLayoutY() + 35);
        blueCountLabel.setMinHeight(20);
        blueCountLabel.setMaxHeight(20);
        blueCountLabel.setTextFill(Color.color(1, 1, 1));

        GameController._gamePane.getChildren().add(newCityDiseaseGridPane);
        GameController._gamePane.getChildren().add(redCountLabel);
        GameController._gamePane.getChildren().add(yellowCountLabel);
        GameController._gamePane.getChildren().add(blackCountLabel);
        GameController._gamePane.getChildren().add(blueCountLabel);
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