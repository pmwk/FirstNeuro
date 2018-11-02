package sample;

import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class CreateTestSet extends Pane {

    private static final double WIDTH = 400;
    private static final double HEIGHT = 430;
    private static final double BOTTOM_HEIGHT = 50;
    private static final double CELL_SIDE = 120;
    private static final double LETTERS_WIDTH = 50;

    private Pane root = new Pane();
    private Pane mainPanel_root = new Pane();
    private Pane topPanel_root = new Pane();
    private Pane leftPanel_root = new Pane();
    private Pane bottomPanel_root = new Pane();

    private static Scene scene;
    private static Stage stage = new Stage();

    DoubleProperty widthProperty = new SimpleDoubleProperty();
    private ArrayList<FontForTest> fonts = new ArrayList<>();

    public CreateTestSet() {

        root.setPrefWidth(WIDTH);
        root.setMaxWidth(WIDTH);

        root.setPrefHeight(HEIGHT);



        ArrayList<String> fonts_list = getFonts();
        for (int i = 0; i < fonts_list.size(); i++) {
            FontForTest font = new FontForTest(fonts_list.get(i));
            fonts.add(font);
            LabelFonts labelFonts = new LabelFonts(font);
            labelFonts.setTranslateX(LETTERS_WIDTH + CELL_SIDE * i);
            topPanel_root.getChildren().add(labelFonts);
        }//добавляем названия шрифтов на верхнюю панель

        Pane letters_root = new Pane();
        ArrayList<Character> letters_list = new ArrayList<>();
        for (int i = 'А'; i <= 'Я'; i++) {
            Pane pane = new Pane();
            pane.setPrefWidth(LETTERS_WIDTH);
            pane.setPrefHeight(CELL_SIDE);
            Label letter_lab = new Label(String.valueOf((char) i));
            letter_lab.setFont(new Font(LF_FONT_SIZE));
            pane.getChildren().add(letter_lab);
            pane.setTranslateY((i - 'А') * CELL_SIDE);
            letters_root.getChildren().add(pane);
            letters_list.add(new Character((char)i));
        }
        leftPanel_root.getChildren().add(letters_root);//создали панель с буквами


        for (int i = 0; i < fonts.size(); i++) {
            FontForTest font = fonts.get(i);
            ArrayList cells = new ArrayList();
            font.cells = cells;
            for (int j = 0; j < letters_list.size(); j++) {
                Cell cell = new Cell(letters_list.get(j), font.font);

                cell.setTranslateX(i * CELL_SIDE);
                cell.setTranslateY(j * CELL_SIDE);

                mainPanel_root.getChildren().add(cell);
                cells.add(cell);
            }
        } //создаём ячейки с буквами

        root.getChildren().addAll(topPanel_root, leftPanel_root, mainPanel_root, bottomPanel_root);

        topPanel_root.setPrefWidth(LETTERS_WIDTH + CELL_SIDE * fonts.size());
        leftPanel_root.setPrefWidth(LETTERS_WIDTH);
        mainPanel_root.setPrefWidth(WIDTH - LETTERS_WIDTH);
        bottomPanel_root.setPrefWidth(WIDTH);

        topPanel_root.setPrefHeight(LF_HEIGHT);
        leftPanel_root.setPrefHeight(CELL_SIDE * letters_list.size());
        bottomPanel_root.setPrefHeight(BOTTOM_HEIGHT);

        leftPanel_root.setMaxWidth(LETTERS_WIDTH);

        bottomPanel_root.setStyle("-fx-background-color: black;");

        leftPanel_root.setTranslateY(LF_HEIGHT);
        mainPanel_root.setTranslateY(LF_HEIGHT);
        bottomPanel_root.setTranslateY(HEIGHT - BOTTOM_HEIGHT);

        mainPanel_root.setTranslateX(LETTERS_WIDTH);

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

        stage.maxWidthProperty().bind(topPanel_root.prefWidthProperty()); //максимальная ширина окна равна ширине для отображения всех шрифтов

        mainPanel_root.translateXProperty().bind(topPanel_root.translateXProperty().add(LETTERS_WIDTH));
        mainPanel_root.translateYProperty().bind(leftPanel_root.translateYProperty()); //привязали основную область к верхней и левой панели соответсвенно

        mainPanel_root.prefHeightProperty().bind(stage.heightProperty().add(-LF_HEIGHT -BOTTOM_HEIGHT)); //высота основной области свзяана с высотой окна


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
                leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() + 15);
            }
        } else {
            /*if () {

            }*/
            leftPanel_root.setTranslateY(leftPanel_root.getTranslateY() - 15);
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
    } //возвращает панель, над которой сейчас курсор

    private ArrayList<String> getFonts() {
        //ArrayList<String> fonts = new ArrayList(Arrays.asList(new String[]{"Font1", "Font2", "Font3", "Font4", "Font5", "Font6"}));
        ArrayList<String> fonts = new ArrayList();
        File directory_files = new File("src/Resource/Fonts/");
        if (directory_files.exists() && directory_files.isDirectory()) {
            for (File font : directory_files.listFiles()) {
                fonts.add(font.getName());
            }
        } else {
            System.out.println("Ошибка с доступом к шрифтам");
        }

        return fonts;
    } //возвращает список массивов

    private static final int CELL_FONT_SIZE = 32;
    private class Cell extends Pane {

        IntegerProperty sizeFont = new SimpleIntegerProperty(CELL_FONT_SIZE);

        private Label sizeFont_lab = new Label();

        public Cell(char c, String font) {
            setPrefHeight(CELL_SIDE);
            setPrefWidth(CELL_SIDE);
            setStyle("-fx-background-color: white");

            Pane workspace_root = new Pane();
            Label label = new Label(String.valueOf(c));
            label.setFont(Font.loadFont(this.getClass().getResourceAsStream("/Resource/Fonts/" + font), CELL_FONT_SIZE));

            Rectangle mask = new Rectangle(30, 30);
            workspace_root.setTranslateX((CELL_SIDE - 30) / 2);
            workspace_root.setTranslateY(25);
            workspace_root.setClip(mask);
            workspace_root.getChildren().add(label);


            HBox fontChange_root =  new HBox();
            fontChange_root.setSpacing(5);
            fontChange_root.setTranslateX(10);
            fontChange_root.setTranslateY(CELL_SIDE - 30);
            fontChange_root.setStyle("-fx-font-size: 24px;");

            Label increaseFont_lab = new Label("+");
            sizeFont_lab.setText(String.valueOf(CELL_FONT_SIZE));
            Label decreaseFont_lab = new Label("-");
            fontChange_root.getChildren().addAll(increaseFont_lab, sizeFont_lab, decreaseFont_lab);


            getChildren().addAll(workspace_root, fontChange_root);

            setOnMouseEntered(event -> {
                setStyle("-fx-background-color: lightgrey");
            });

            setOnMouseExited(event -> {
                setStyle("-fx-background-color: white");
            });

            setOnMouseClicked(event -> {
                System.out.println("click");
            });

            increaseFont_lab.setOnMouseClicked(event -> {
                setSizeFont(sizeFont.get() + 1);
            });

            decreaseFont_lab.setOnMouseClicked(event -> {
                setSizeFont(sizeFont.get() - 1);
            });


            sizeFont_lab.textProperty().bind(sizeFont.asString());
            sizeFont.addListener((ov, old_value, new_value) -> {
                label.setFont(Font.loadFont(this.getClass().getResourceAsStream("/Resource/Fonts/" + font), new_value.doubleValue()));
            });
        }

        private void setSizeFont(int size) {
            sizeFont.set(size);

        }


    }

    private static final double LF_HEIGHT = 30;
    private static final double LF_FONT_SIZE = 20;

    private class LabelFonts extends Pane {
        FontForTest fontForTest;

        public LabelFonts(FontForTest fontForTest) {
            this.fontForTest = fontForTest;

            setPrefWidth(CELL_SIDE);
            setPrefHeight(LF_HEIGHT);

            Label label = new Label();
            label.textProperty().bind(fontForTest.font_name);
            label.setPrefWidth(CELL_SIDE - 10);
            label.setTranslateX(5);
            label.setFont(new Font(LF_FONT_SIZE));
            Tooltip tooltip = new Tooltip();
            tooltip.textProperty().bind(fontForTest.font_name);
            label.setTooltip(tooltip);

            getChildren().add(label);


            setOnMouseClicked(event -> {
                changeFont(fontForTest);
            });
        }

        public void setGrey() {
            setStyle("-fx-background-color: lightgrey;");
        }
    }//панель для отображения названия шрифта

    private class FontForTest {
        String font;
        SimpleStringProperty font_name = new SimpleStringProperty();
        ArrayList<Cell> cells = new ArrayList<>();

        public FontForTest(String font) {
            this.font = font;
            font_name = new SimpleStringProperty(font);
        }

        public int getSize() {
            int size = cells.get(0).sizeFont.get();
            for (int i = 0; i < cells.size(); i++) {
                if (size != cells.get(i).sizeFont.get()) {
                    return -1;
                }
            }
            return size;
        }

        public void setName(String new_name) {
            font_name.set(new_name);
        }

        public String getName() {
            return font_name.get();
        }

        public void setSizeFont(Integer size) {
            for (Cell cell : cells) {
                cell.setSizeFont(size);
            }
        }

        public void changeTranslateX(double delta) {
            for (Cell cell : cells) {
                cell.setTranslateX(cell.getTranslateX() + delta);
            }
        }

    }

    private void deleteFont (FontForTest font) {
        int index = fonts.indexOf(font);
        fonts.remove(font);
        mainPanel_root.getChildren().removeAll(font.cells);
        for (int i = index; i < fonts.size(); i++) {
            fonts.get(i).changeTranslateX(-CELL_SIDE);
        }
    }

    public static void createPane() {
        CreateTestSet createTestSet = new CreateTestSet();
        scene = new Scene(createTestSet);
        stage.setScene(scene);
        //stage.setResizable(false);
        //stage.setFullScreen(true);
        stage.show();
    } //создаёт новое окно для создания тестового набора

    private void changeFont(FontForTest font) {
        Stage stage2 = new Stage();
        stage2.initModality(Modality.APPLICATION_MODAL);
        Pane root = new Pane();

        VBox main_root = new VBox();
        main_root.setSpacing(20);

        TextField fontName_tf = new TextField();
        fontName_tf.setText(font.getName());

        HBox fontSize_root = new HBox();
        Label increaseFont_lab = new Label("+");
        TextField fontSize_tf = new TextField();
        int sizeFont = font.getSize();
        if (sizeFont == -1) {
            fontSize_tf.setPromptText("Неравномерно");
        } else {
            fontSize_tf.setText(String.valueOf(sizeFont));
        }
        Label decreaseFont_lab = new Label("-");
        fontSize_root.getChildren().addAll(increaseFont_lab, fontSize_tf, decreaseFont_lab);

        HBox buttons_root = new HBox();
        Button ok_but = new Button("Сохранить");
        Button delete_but = new Button("Удалить");
        Button cancel_but = new Button("отмена");
        buttons_root.getChildren().addAll(cancel_but, delete_but, ok_but);

        main_root.getChildren().addAll(fontName_tf, fontSize_root, buttons_root);
        root.getChildren().add(main_root);
        Scene scene = new Scene(root, 400, 400);
        stage2.setScene(scene);
        stage2.show();

        cancel_but.setOnMouseClicked(event -> {
            stage2.close();
        });

        ok_but.setOnMouseClicked(event -> {
            if (!fontName_tf.getText().equals(font.font_name)) {
                font.setName(fontName_tf.getText());
            }
            if (sizeFont != Integer.valueOf(fontSize_tf.getText())) {
                font.setSizeFont(Integer.valueOf(fontSize_tf.getText()));
            }
            stage2.close();
        });

        delete_but.setOnMouseClicked(event -> {
            deleteFont(font);
        });
    }
}

enum Panel {
    Top, Left, Main, Bottom
}

