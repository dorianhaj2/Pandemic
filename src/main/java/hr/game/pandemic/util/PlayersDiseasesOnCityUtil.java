package hr.game.pandemic.util;

import hr.game.pandemic.GameController;
import hr.game.pandemic.model.City;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PlayersDiseasesOnCityUtil {
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
            List<File> files = FXMLUtils.getAllFilesFromResource("hr/game/pandemic/images/");

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
}
