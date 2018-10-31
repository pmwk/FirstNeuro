package sample;

/*
* создаёт изображения букв в разных шрифтах
* в формате .bmp*/

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CreateTestBMP {

    public static void startTest() {
        /*File fonts_folders = new File("src/Resource/Fonts/");
        if (fonts_folders.exists()) {
            int i = 0;
            char c = 'К';
            for (File font : fonts_folders.listFiles()) {
                Label label  = new Label("K");
                label.setFont(Font.loadFont(CreateTestBMP.class.getResourceAsStream("/Resource/Fonts/" + font.getName()), 40));
                label.setPrefWidth(40);
                label.setPrefHeight(40);
                Main.root.getChildren().add(label);
                createSnapshot(label, c + String.valueOf(i));
                i++;
            }
        } else {
            System.out.println("Папка не найдена");
        }*/
        //RGBfromPng();
        testReadRGBFromPng();
    }

    private static void createSnapshot(Label label, String name) {
        File file = new File("src/Resource/Data/" + name + ".png");
        try {
            file.createNewFile();
            WritableImage wi = new WritableImage((int)label.getPrefWidth(),(int) label.getPrefHeight());
            label.snapshot(new SnapshotParameters(), wi);
            BufferedImage bi = new BufferedImage((int)label.getPrefWidth(),(int) label.getPrefHeight(), BufferedImage.TYPE_INT_RGB);
            ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] RGBfromPng () {
        int[][] rgb = new int[40][40];
        File file = new File("src/Resource/Data/К3.png");
        try {
            BufferedImage image = ImageIO.read(file);

            int w = image.getWidth();
            int h = image.getHeight();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int pixel = image.getRGB(j, i);

                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    if (red == 255 && green == 255 && blue == 255) {
                        rgb[i][j] = 0;
                    } else {
                        rgb[i][j] = 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


        return rgb;
    }



    /*public static void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);

    }

    private static void marchThroughImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                printPixelARGB(pixel);
                System.out.println("");
            }
        }
    }*/

    private static double side = 10;
    private static void testReadRGBFromPng () {
        Pane root = new Pane();
        root.setPrefHeight(side * 40);
        root.setPrefWidth(side * 40);

        int rgb[][] = RGBfromPng();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                Rectangle rect = new Rectangle(side, side);
                if (rgb[i][j] == 1) {
                    rect.setFill(Color.BLACK);
                } else {
                    rect.setFill(Color.WHITE);
                }
                rect.setTranslateX(j * side);
                rect.setTranslateY(i * side);
                root.getChildren().add(rect);
            }
        }

        Main.root.getChildren().add(root);
    }
}
