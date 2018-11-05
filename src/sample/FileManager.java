package sample;

import javafx.beans.property.ListProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class FileManager {

    public static ArrayList<String> getPathFont() {
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
    }

    public static int[][] getRGBFromPane (Pane pane) {
        int rgb[][] = new int[40][40];
        WritableImage wi = new WritableImage(40, 40);
        BufferedImage bi = SwingFXUtils.fromFXImage(pane.snapshot(new SnapshotParameters(), wi), null);

        int w = bi.getWidth();
        int h = bi.getHeight();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = bi.getRGB(j, i);

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

        return rgb;
    }

    public static void writeCells(String name, ListProperty<FontForTest> fonts) {
        ArrayList<SimpleFontForTest> fonts_for_save = new ArrayList<>();
        for (int i = 0; i < fonts.size(); i++) {
            FontForTest font  = fonts.get(i);
            ArrayList<Cell> cells = font.getCells();

            ArrayList<SimpleLetter> letters_for_save = new ArrayList<>();
            for (int j = 0; j < cells.size(); j++) {
                Cell cell = cells.get(j);
                letters_for_save.add(new SimpleLetter(cell.getC(), cell.getRGB()));
            }
            fonts_for_save.add(new SimpleFontForTest(font.getName(), letters_for_save));

            System.out.println("Шрифт " + i);
        }
        try {
            FileOutputStream fos = new FileOutputStream(name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(fonts_for_save);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("End");

    }

    public static ArrayList<SimpleFontForTest> getFontsFromData(String name) {
        ArrayList<SimpleFontForTest> result = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            result = (ArrayList<SimpleFontForTest>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
