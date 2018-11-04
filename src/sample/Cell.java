package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class Cell {

    private static final int CELL_FONT_SIZE = 32;
    public static final double CELL_SIDE = 120;

    private IntegerProperty sizeFont = new SimpleIntegerProperty(CELL_FONT_SIZE);
    private char c;
    private String pathFont;

    public Cell(char c, String pathFont) {
        this.c = c;
        this.pathFont = pathFont;
    }

    public void setSizeFont(int size) {
        sizeFont.set(size);
    }

    public int getSizeFont() {
        return sizeFont.get();
    }

    private Label sizeFont_lab = new Label();
    private Pane view;
    public Pane getView () {
        if (view != null) {
            return view;
        }
        view = new Pane();

        view.setPrefHeight(CELL_SIDE);
        view.setPrefWidth(CELL_SIDE);
        view.setStyle("-fx-background-color: white");

        workspace_root = new Pane();
        Label label = new Label(String.valueOf(c));
        label.setFont(Font.loadFont(this.getClass().getResourceAsStream("/Resource/Fonts/" + pathFont), CELL_FONT_SIZE));

        Rectangle mask = new Rectangle(40, 40);
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

        view.getChildren().addAll(workspace_root, fontChange_root);

        view.setOnMouseEntered(event -> {
            view.setStyle("-fx-background-color: lightgrey");
        });

        view.setOnMouseExited(event -> {
            view.setStyle("-fx-background-color: white");
        });

        workspace_root.setOnMouseClicked(event -> {
            Stage stage = new Stage();
            Pane root = new Pane();
            int rgb[][] = FileManager.getRGBFromPane(workspace_root);
            for (int i = 0; i < rgb.length; i++) {
                for (int j = 0; j < rgb[i].length; j++) {
                    Rectangle rect = new Rectangle(10, 10);
                    if (rgb[i][j] == 1) {
                        rect.setFill(Color.BLACK);
                    } else {
                        rect.setFill(Color.WHITE);
                    }
                    rect.setTranslateX(j * 10);
                    rect.setTranslateY(i * 10);
                    root.getChildren().add(rect);
                }
            }
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);
            stage.show();
        });

        increaseFont_lab.setOnMouseClicked(event -> {
            setSizeFont(sizeFont.get() + 1);
        });

        decreaseFont_lab.setOnMouseClicked(event -> {
            setSizeFont(sizeFont.get() - 1);
        });


        sizeFont_lab.textProperty().bind(sizeFont.asString());
        sizeFont.addListener((ov, old_value, new_value) -> {
            label.setFont(Font.loadFont(this.getClass().getResourceAsStream("/Resource/Fonts/" + pathFont), new_value.doubleValue()));
        });

        return view;
    }

    private Pane workspace_root;
    public int[][] getRGB() {
        return FileManager.getRGBFromPane(workspace_root);
    }

    public char getC() {
        return c;
    }
}
