package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class CreateTestSet extends Pane {

    private static final double WIDTH = 400; //начальная ширина экрана
    private static final double HEIGHT = 430; //начальная высота экрана
    private static final double BOTTOM_HEIGHT = 50; //высота нижней панели с кнопками
    private static final double LEFT_WIDTH = 50; //ширина левой панели с буквами

    private Pane root = new Pane(); //самая основная панель
    private Pane mainPanel_root = new Pane();//панель, на которой располагаются эскизы букв (Cell)
    private Pane topPanel_root = new Pane(); //панель, на которой располагается горизонтальный спсиок шрифтов
    private Pane leftPanel_root = new Pane(); //панель, на которой располагаются буквы
    private Pane bottomPanel_root = new Pane(); //нижняя панель, на которой располагаются кнопки

    private static Scene scene;
    private static Stage stage = new Stage();

    private ListProperty<FontForTest> fonts = new SimpleListProperty<>(); //список со шрифтами
    private ListProperty<Letter> letters = new SimpleListProperty<>(); //список с буквами

    private void init() {
        createSetFonts();
        createSetLetters();

        createSetCells(); //после создания наборов букв и шрфитов, создаём соответствующие ячейки

        initStyle();
        initSizeAndBeans();
    }

    private void createSetLetters() {
        ArrayList letters_list = new ArrayList();
        letters.set(FXCollections.observableArrayList(letters_list));
        for (int i = 'А'; i <= 'Я'; i++) {
            Letter letter = new Letter((char) i);
            letters.add(letter);
        }
    }//создаём набор букв

    private void createSetFonts() {
        ArrayList<String> fonts_list = FileManager.getPathFont();
        fonts.set(FXCollections.observableArrayList());
        for (int i = 0; i < fonts_list.size(); i++) {
            FontForTest font = new FontForTest(fonts_list.get(i));
            fonts.add(font);
        }
    }//создаёт набор шрифтов

    private void createSetCells() {
        for (int i = 0; i < fonts.size(); i++) {
            ArrayList<Cell> cells = new ArrayList<>();
            for (int j = 0; j < letters.size(); j++) {
                Cell cell = new Cell(letters.get(j).getChar(), fonts.get(i).getPathFont());
                cells.add(cell);
            }
            fonts.get(i).setCells(cells);
        }
    }//создаём наборы ячеек

    private void initStyle() {
        bottomPanel_root.setStyle("-fx-background-color: lightgreen;");
        topPanel_root.setStyle("-fx-background-color: white;");
        leftPanel_root.setStyle("-fx-background-color: white;");
    }// устанавливает все нужжные стили

    private void initSizeAndBeans() {
        stage.maxWidthProperty().bind(topPanel_root.prefWidthProperty()); //максимальная ширина окна равна ширине для отображения всех шрифтов
        stage.maxHeightProperty().bind(leftPanel_root.prefHeightProperty().add(FontForTest.HEIGHT).add(BOTTOM_HEIGHT));

        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty()); //привязали размеры главной панели к размеру окна

        leftPanel_root.setPrefWidth(LEFT_WIDTH);
        leftPanel_root.prefHeightProperty().bind(letters.sizeProperty().multiply(Cell.CELL_SIDE));
        leftPanel_root.setTranslateY(FontForTest.HEIGHT);

        topPanel_root.setPrefHeight(FontForTest.HEIGHT);
        topPanel_root.prefWidthProperty().bind(fonts.sizeProperty().multiply(Cell.CELL_SIDE).add(LEFT_WIDTH));

        bottomPanel_root.setPrefHeight(BOTTOM_HEIGHT);


        mainPanel_root.setTranslateY(FontForTest.HEIGHT);
        mainPanel_root.setTranslateX(LEFT_WIDTH);

        mainPanel_root.translateXProperty().bind(topPanel_root.translateXProperty().add(LEFT_WIDTH));
        mainPanel_root.translateYProperty().bind(leftPanel_root.translateYProperty()); //привязали основную область к верхней и левой панели соответсвенно

        mainPanel_root.prefWidthProperty().bind(topPanel_root.prefWidthProperty());
        mainPanel_root.prefHeightProperty().bind(leftPanel_root.heightProperty());

        bottomPanel_root.translateYProperty().bind(stage.heightProperty().add(-BOTTOM_HEIGHT)); //нижняя панель всегда внизу
        bottomPanel_root.prefWidthProperty().bind(stage.widthProperty()); //нижняя панель по всей ширине экрана

    }//определяет размеры основных панелей и настраивает бинды

    public CreateTestSet() {
        init();

        for (int i = 0; i < fonts.size(); i++) {
            Pane labelFonts = fonts.get(i).getView();
            labelFonts.setTranslateX(LEFT_WIDTH + Cell.CELL_SIDE * i);
            topPanel_root.getChildren().add(labelFonts);
        } //заполняем верхнюю строчку подписями Шрифтов

        for (int i = 0; i < letters.size(); i++) {
            Label letter_lab = letters.get(i).getView();
            letter_lab.setTranslateY(i * Cell.CELL_SIDE + 40); //+40 - для красоты отображения
            letter_lab.setTranslateX(5);
            leftPanel_root.getChildren().add(letter_lab);
        }//заполнили левую панель с буквами

        for (int i = 0; i < fonts.size(); i++) {
            FontForTest font  = fonts.get(i);
            for (int j = 0; j < letters.size(); j++) {
                Cell cell = font.getCell(j);
                Pane cell_view = cell.getView();
                cell_view.setTranslateX(i * Cell.CELL_SIDE);
                cell_view.setTranslateY(j * Cell.CELL_SIDE);
                mainPanel_root.getChildren().add(cell_view);
            }
        } //Заполнили центральную область буквами соответсвтующего шрифта

        TextField nameTestSet_tf = new TextField();
        Button create_but = new Button("Сохранить");
        create_but.setTranslateX(250);
        bottomPanel_root.getChildren().addAll(nameTestSet_tf, create_but);


        root.getChildren().addAll(topPanel_root, leftPanel_root, mainPanel_root, bottomPanel_root);

        getChildren().addAll(root);

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


        create_but.setOnMouseClicked(event -> {
            /*JSONObject json = new JSONObject();
            JSONArray array =
            for (int i = 0; i < fonts.size(); i++) {
                ArrayList<Cell> cells = fonts.get(i).getCells();
                for (int j = 0; j < cells.size(); j++) {
                    Cell cell = cells.get(j);
                    int rgb[][] = cell.getRGB();


                }
            }*/
            FileManager.writeCells(nameTestSet_tf.getText(), fonts);
        });


        leftPanel_root.toFront();
        topPanel_root.toFront();
        bottomPanel_root.toFront();



    }



    private void scrollTopAndMain(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            if (topPanel_root.getWidth() > (stage.getWidth() + (-topPanel_root.getTranslateX()))) {
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
            if (leftPanel_root.getTranslateY() < FontForTest.HEIGHT) {
                leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() + 15);
            }
        } else {
            if (mainPanel_root.getTranslateY() + mainPanel_root.getPrefHeight() + BOTTOM_HEIGHT >= stage.getHeight()) {
                leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() - 15);
            }
        }
    }//прокручивание основной и левой области по вертикали

    public Panel getPosition(double x, double y) {
        if (y < FontForTest.HEIGHT) {
            return Panel.Top;
        } else if (y > stage.getHeight() - BOTTOM_HEIGHT) {
            return Panel.Bottom;
        } else if (x < LEFT_WIDTH) {
            return Panel.Left;
        } else {
            return Panel.Main;
        }
    } //возвращает панель, над которой сейчас курсор

    public void deleteLetter() {
        System.out.println("изначальная высота - " + leftPanel_root.getPrefHeight());
        letters.remove(0);
        System.out.println("после удаления высота - " + leftPanel_root.getPrefHeight());
    }

    private void deleteFont (FontForTest font) {
        int index = fonts.indexOf(font);
        fonts.remove(font);
        mainPanel_root.getChildren().removeAll(font.getCells());
        for (int i = index; i < fonts.size(); i++) {
            fonts.get(i).changeTranslateX(-Cell.CELL_SIDE);
        }
    }

    public static void createPane() {
        CreateTestSet createTestSet = new CreateTestSet();
        scene = new Scene(createTestSet, WIDTH, HEIGHT);
        stage.setScene(scene);
        //stage.setResizable(false);
        //stage.setFullScreen(true);
        stage.show();
    } //создаёт новое окно для создания тестового набора
}

enum Panel {
    Top, Left, Main, Bottom
}

