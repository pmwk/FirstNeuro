package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTestSet extends Pane {

    private static final double WIDTH = 400;
    private static final double HEIGHT = 430;
    private static final double BOTTOM_HEIGHT = 50;
    private static final double CELL_SIDE = 100;
    private static final double LETTERS_WIDTH = 50;

    Pane root = new Pane();
    private Pane mainPanel_root = new Pane();
    private Pane topPanel_root = new Pane();
    private Pane leftPanel_root = new Pane();
    private Pane bottomPanel_root = new Pane();

    private static Scene scene;
    private static Stage stage = new Stage();

    DoubleProperty widthProperty = new SimpleDoubleProperty();

    public CreateTestSet() {

        root.setPrefWidth(WIDTH);
        root.setMaxWidth(WIDTH);

        root.setPrefHeight(HEIGHT);



        ArrayList<String> fonts_list = getFonts();
        for (int i = 0; i < fonts_list.size(); i++) {
            LabelFonts labelFonts = new LabelFonts(fonts_list.get(i));
            labelFonts.setTranslateX(LETTERS_WIDTH + CELL_SIDE * i);
            topPanel_root.getChildren().add(labelFonts);
        }//добавляем названия шрифтов на верхнюю панель

        Pane letters_root = new Pane();
        ArrayList<Character> letters_list = new ArrayList<>();
        for (int i = 'А'; i <= 'Я'; i++) {
            Pane pane = new Pane();
            pane.setPrefWidth(CELL_SIDE);
            pane.setPrefHeight(CELL_SIDE);
            Label letter_lab = new Label(String.valueOf((char) i));
            letter_lab.setFont(new Font(LF_FONT_SIZE));
            pane.getChildren().add(letter_lab);
            pane.setTranslateY((i - 'А') * CELL_SIDE);
            letters_root.getChildren().add(pane);
            letters_list.add(new Character((char)i));
        }
        leftPanel_root.getChildren().add(letters_root);

        root.getChildren().addAll(topPanel_root, leftPanel_root, mainPanel_root, bottomPanel_root);

        topPanel_root.setPrefWidth(LETTERS_WIDTH + CELL_SIDE * fonts_list.size());
        leftPanel_root.setPrefWidth(LETTERS_WIDTH);
        mainPanel_root.setPrefWidth(WIDTH - LETTERS_WIDTH);
        bottomPanel_root.setPrefWidth(WIDTH);

        topPanel_root.setPrefHeight(LF_HEIGHT);
        leftPanel_root.setPrefHeight(CELL_SIDE * letters_list.size());
        mainPanel_root.setPrefHeight(250);
        bottomPanel_root.setPrefHeight(BOTTOM_HEIGHT);

        mainPanel_root.setStyle("-fx-background-color: orange;");
        bottomPanel_root.setStyle("-fx-background-color: black;");

        leftPanel_root.setTranslateY(LF_HEIGHT);
        mainPanel_root.setTranslateY(LF_HEIGHT);
        bottomPanel_root.setTranslateY(HEIGHT - BOTTOM_HEIGHT);

        mainPanel_root.setTranslateX(LETTERS_WIDTH);

        getChildren().add(root);

        root.setOnScroll(event -> {
            Panel panel = getPosition(event.getSceneX(), event.getSceneY());
            switch (panel) {
                case Top:
                    scrollTopAndMain(event);
                    break;
                case Left:
                case Main:
                    scrollLeftAndMain(event);
            }
        });

        stage.maxWidthProperty().bind(topPanel_root.prefWidthProperty());

        bottomPanel_root.translateYProperty().bind(stage.heightProperty().add(-BOTTOM_HEIGHT)); //нижняя панель всегда внизу
        bottomPanel_root.prefWidthProperty().bind(stage.widthProperty()); //нижняя панель по всей ширине экрана

        topPanel_root.setStyle("-fx-background-color: white;");
        leftPanel_root.setStyle("-fx-background-color: white;");

        leftPanel_root.toFront();
        topPanel_root.toFront();
        bottomPanel_root.toFront();


        widthProperty.bind(stage.widthProperty());
    }

    private double getCurrentWidth() {
        return widthProperty.doubleValue();
    }

    private void scrollTopAndMain(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            if (topPanel_root.getWidth() > (getCurrentWidth() + (-topPanel_root.getTranslateX()))) {
                topPanel_root.setTranslateX(topPanel_root.getTranslateX() - 10);
            }
        } else {
            if (topPanel_root.getTranslateX() < 0) {
                topPanel_root.setTranslateX(topPanel_root.getTranslateX() + 10);
            }
        }
    } //горизонтальное прокручивание topPanel И mainPanel

    private void scrollLeftAndMain (ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            if (leftPanel_root.getTranslateY() < LF_HEIGHT) {
                mainPanel_root.setTranslateY(mainPanel_root.getTranslateY() + 10);
                leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() + 10);
            }
        } else {
            /*if () {

            }*/
            mainPanel_root.setTranslateY(mainPanel_root.getTranslateY() - 10);
            leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() - 10);
        }
    }




    public Panel getPosition(double x, double y) {
        if (y < LF_HEIGHT) {
            return Panel.Top;
        } else if (y > mainPanel_root.getHeight() + LF_HEIGHT) {
            return Panel.Bottom;
        } else if (x < CELL_SIDE) {
            return Panel.Left;
        } else {
            return Panel.Main;
        }
    }

    private ArrayList<String> getFonts() {
        ArrayList<String> fonts = new ArrayList(Arrays.asList(new String[]{"Font1", "Font2", "Font3", "Font4", "Font5", "Font6"}));

        return fonts;
    } //возвращает список массивов

    private class Cell extends Pane {
        public Cell(ImageView iv) {
            setPrefWidth(CELL_SIDE);
            setPrefHeight(CELL_SIDE);
            iv.setTranslateY(5);
            iv.setTranslateX(5);

            getChildren().add(iv);
        }
    }


    private static final double LF_HEIGHT = 30;
    private static final double LF_FONT_SIZE = 24;

    private class LabelFonts extends Pane {
        public LabelFonts(String font_name) {

            setPrefWidth(CELL_SIDE);
            setPrefHeight(LF_HEIGHT);

            Label label = new Label(font_name);
            label.setPrefWidth(CELL_SIDE - 10);
            label.setTranslateX(5);
            label.setFont(new Font(LF_FONT_SIZE));

            getChildren().add(label);
        }

        public void setGrey() {
            setStyle("-fx-background-color: lightgrey;");
        }
    }//панель для отображения названия шрифта


    public static void createPane() {
        CreateTestSet createTestSet = new CreateTestSet();
        scene = new Scene(createTestSet);
        stage.setScene(scene);
        //stage.setResizable(false);
        //stage.setFullScreen(true);
        stage.show();

    } //создаёт новое окно для создания тестового набора
}

enum Panel {
    Top, Left, Main, Bottom
}


/*HBox topPanel_root = new HBox();
        topPanel_root.getChildren().add(getEmptyLabelFonts());
        ArrayList<String> fonts_list = getFonts();
        for (int i = 0; i < fonts_list.size(); i++) {
            LabelFonts labelFont = new LabelFonts(fonts_list.get(i));
            if (i % 2 == 0) {
                labelFont.setGrey();
            }
            topPanel_root.getChildren().add(labelFont);
        }

        ScrollPane main_root = new ScrollPane();
        Pane main_inner_root = new Pane();
        ArrayList<Label> letters_labels = new ArrayList<>();
        for (char c = 'А'; c <= 'Я'; c++) {
            Label letter_lab = new Label(String.valueOf(c));
            letter_lab.setTranslateX(0);
            letter_lab.setTranslateY((c - 'А') * CELL_SIDE);
            letters_labels.add(letter_lab);
        }
        Pane letters_root = new Pane();
        letters_root.getChildren().addAll(letters_labels);

        GridPane gp = new GridPane();
        for (int i = 0; i < fonts_list.size(); i++) {
            for (int j = 0; j < letters_labels.size(); j++) {
                ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/Resource/Data/К1.png")));
                Cell cell = new Cell(iv);
                gp.add(cell, i, j);
            }
        }
        gp.setTranslateX(CELL_SIDE);

        main_inner_root.getChildren().addAll(letters_root, gp);
        main_inner_root.setPrefHeight(CELL_SIDE * letters_labels.size());
        main_root.setContent(main_inner_root);

        HBox bottomPanel_root = new HBox();

        VBox root = new VBox();
        root.getChildren().addAll(topPanel_root, main_root, bottomPanel_root);
        getChildren().add(root);*/