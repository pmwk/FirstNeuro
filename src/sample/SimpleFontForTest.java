package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class SimpleFontForTest implements Serializable {

    String fontName;
    ArrayList<SimpleLetter> letters = new ArrayList<>();

    public SimpleFontForTest(String fontName, ArrayList<SimpleLetter> letters) {
        this.fontName = fontName;
        this.letters = letters;
    }
}
