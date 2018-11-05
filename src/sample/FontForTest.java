package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;

public class FontForTest {

    public final static double HEIGHT = 30;
    public final static double FONT_SIZE = 30;

    private String pathFont;
    private SimpleStringProperty font_name = new SimpleStringProperty();
    private ArrayList<Cell> cells = new ArrayList<>();

    public FontForTest(String pathFont) {
        this.pathFont = pathFont;
        font_name = new SimpleStringProperty(pathFont);
    }

    public int getSize() {
        int size = cells.get(0).getSizeFont();
        for (int i = 0; i < cells.size(); i++) {
            if (size != cells.get(i).getSizeFont()) {
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

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    public String getPathFont() {
        return pathFont;
    }

    public void setSizeFont(Integer size) {
        for (Cell cell : cells) {
            cell.setSizeFont(size);
        }
    }

    public void changeTranslateX(double delta) {
        for (Cell cell : cells) {
            cell.getView().setTranslateX(cell.getView().getTranslateX() + delta);
        }
        view.setTranslateX(view.getTranslateX() + delta);
    }

    public Cell getCell (int index) {
        return cells.get(index);
    }

    private Pane view;
    public Pane getView() {
        if (view != null) {
            return view;
        }
        view = new Pane();

        view.setPrefWidth(Cell.CELL_SIDE);
        view.setPrefHeight(HEIGHT);

        Label label = new Label();
        label.textProperty().bind(font_name);
        label.setPrefWidth(Cell.CELL_SIDE - 10);
        label.setTranslateX(5);
        label.setFont(new Font(FONT_SIZE));
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(font_name);
        label.setTooltip(tooltip);

        view.getChildren().add(label);

        view.setOnMouseClicked(event -> {
            openExtendSettings();
        });
        return view;
    }

    private void openExtendSettings() {
        Stage stage2 = new Stage();
        stage2.initModality(Modality.APPLICATION_MODAL);
        Pane root = new Pane();

        VBox main_root = new VBox();
        main_root.setSpacing(20);

        TextField fontName_tf = new TextField();
        fontName_tf.setText(getName());

        HBox fontSize_root = new HBox();
        Label increaseFont_lab = new Label("+");
        TextField fontSize_tf = new TextField();
        int sizeFont = getSize();
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
            if (!fontName_tf.getText().equals(font_name)) {
                setName(fontName_tf.getText());
            }
            if (sizeFont != Integer.valueOf(fontSize_tf.getText())) {
                setSizeFont(Integer.valueOf(fontSize_tf.getText()));
            }
            stage2.close();
        });

        delete_but.setOnMouseClicked(event -> {
            CreateTestSet.getInstance().removeFont(this);
            stage2.close();
        });
    }
}
