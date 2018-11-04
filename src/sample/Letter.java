package sample;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Letter {

    public static final double FONT_SIZE = 30;

    private char CHAR;

    public Letter(char aChar) {
        this.CHAR = aChar;
    }

    public char getChar() {
        return CHAR;
    }

    public Label getView() {
        Label label = new Label(String.valueOf(CHAR));
        label.setFont(new Font(FONT_SIZE));

        return label;
    }
}
