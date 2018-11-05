package sample;

import java.io.Serializable;

public class SimpleLetter implements Serializable{

    char c;
    int rgb[][];

    public SimpleLetter(char c, int[][] rgb) {
        this.c = c;
        this.rgb = rgb;
    }
}
