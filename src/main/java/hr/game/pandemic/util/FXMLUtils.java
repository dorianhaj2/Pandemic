package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

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
        ImageView iv = (ImageView) getNodeById("outbreak" + GameState.OUTBREAK_COUNTER, GameController._gamePane);
        iv.setVisible(true);
        if (GameState.OUTBREAK_COUNTER > 1){
            ImageView ivPrev = (ImageView) getNodeById("outbreak" + (GameState.OUTBREAK_COUNTER - 1), GameController._gamePane);
            ivPrev.setVisible(false);
        }
    }
    public static void moveInfectionRateToken() {
        ImageView iv = (ImageView) getNodeById("infectionRate" + GameState.INFECTION_RATE, GameController._gamePane);
        iv.setVisible(true);
        ImageView ivPrev = (ImageView) getNodeById("infectionRate" + (GameState.INFECTION_RATE - 1), GameController._gamePane);
        ivPrev.setVisible(false);
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
        playersGrid.add(playerGridPane, 0,playerNumber - 1);
    }
    public static void refreshCityButtonText(City city) {
        Label cityDiseaseLabel = (Label) getNodeById(city.getName() + "Diseases", GameController._gamePane);
        cityDiseaseLabel.setText(city.getBlueDiseases() + "Blu" +
                city.getYellowDiseases() + "Y" +
                city.getBlackDiseases() + "Bla" +
                city.getRedDiseases() + "R");

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
        String cityName = player.getCurrentCity().substring(0, 1).toUpperCase() + player.getCurrentCity().substring(1);
        List<Integer> indexes = new ArrayList<>();

        for (int i = 1; i < cityName.length(); i++) {
            if (Character.isUpperCase(cityName.charAt(i))) {
                indexes.add(i);
            }
        }
        String tmp = "";
        int lastIndex = 0;
        for (Integer i : indexes) {
            tmp = tmp + cityName.substring(lastIndex, i) + " ";
            lastIndex = i;
        }
        tmp = tmp + cityName.substring(lastIndex);
        Label cityLabel = (Label) getNodeById(player.getName().toLowerCase() + "Location", GameController._gamePane);
        cityLabel.setText(tmp);

        ImageView cityPlayerImage = (ImageView) getNodeById(player.getCurrentCity() + player.getName() + "Image", GameController._gamePane);

        try {
            List<File> files = getAllFilesFromResource("hr/game/pandemic/images/roles/pawns/");

            for (File f : files) {
                //if (f.getName().equals(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, player.getRoleName().replaceAll(" ", "") + "_pawn.png")))
                if (f.getName().equals(player.getRoleName().toLowerCase().replaceAll(" ", "_") + "_pawn.png"))
                    cityPlayerImage.setImage(new Image(new FileInputStream(f)));
            }
            cityPlayerImage.setFitWidth(20);
            cityPlayerImage.setFitHeight(20);
            if (!player.getPreviousCity().isEmpty()) {
                ImageView previousCityPlayerImage = (ImageView) getNodeById(player.getPreviousCity() + player.getName() + "Image", GameController._gamePane);
                previousCityPlayerImage.setImage(null);
            }

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<File> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
        ClassLoader classLoader = FXMLUtils.class.getClassLoader();
        URL resource = classLoader.getResource(folder);

        // dun walk the root path, we will walk all the classes
        List<File> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        return collect;
    }
    public static void addGridViewForPlayersAndLabelsForDiseasesToCity(City c) {
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

        Label cityDiseaseLabel = new Label();
        cityDiseaseLabel.setId(c.getName() + "Diseases");
        cityDiseaseLabel.setLayoutX(cityButton.getLayoutX() - 30);
        cityDiseaseLabel.setLayoutY(cityButton.getLayoutY() + 12);
        cityDiseaseLabel.setMouseTransparent(true);
        cityDiseaseLabel.getStyleClass().add("disease_label");

        Button invisibleButton = new Button();
        invisibleButton.setLayoutX(cityButton.getLayoutX());
        invisibleButton.setLayoutY(cityButton.getLayoutY());
        invisibleButton.setMinWidth(40);
        invisibleButton.setMinHeight(40);
        invisibleButton.setMaxWidth(40);
        invisibleButton.setMaxHeight(40);
        invisibleButton.getStyleClass().add("invisible");
        invisibleButton.setOnMouseEntered(event -> {
            cityDiseaseLabel.getStyleClass().remove("disease_label");
            cityDiseaseLabel.getStyleClass().add("disease_label_bigger");
        });
        invisibleButton.setOnMouseExited(event -> {
            cityDiseaseLabel.getStyleClass().remove("disease_label_bigger");
            cityDiseaseLabel.getStyleClass().add("disease_label");
        });
        cityButton.setOnMouseEntered(event -> {
            cityDiseaseLabel.getStyleClass().remove("disease_label");
            cityDiseaseLabel.getStyleClass().add("disease_label_bigger");
        });
        cityButton.setOnMouseExited(event -> {
            cityDiseaseLabel.getStyleClass().remove("disease_label_bigger");
            cityDiseaseLabel.getStyleClass().add("disease_label");
        });

        GameController._gamePane.getChildren().add(cityDiseaseLabel);
        GameController._gamePane.getChildren().add(1, invisibleButton);
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