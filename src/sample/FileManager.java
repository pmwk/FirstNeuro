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

    public static void writeCells(ListProperty<FontForTest> fonts) {
        JSONObject fonts_json = new JSONObject();
        for (int i = 0; i < fonts.size(); i++) {
            FontForTest font = fonts.get(i);
            JSONArray letters_json = new JSONArray();

            ArrayList<Cell> cells = font.getCells();
            for (int j = 0; j < cells.size(); j++) {
                Cell cell = cells.get(j);
                JSONObject letter = new JSONObject();

                JSONArray rgb_json = new JSONArray();
                int rgb[][] = cell.getRGB();
                for (int k = 0; k < rgb.length; k++) {
                    JSONArray rgb2_json = new JSONArray();
                    for (int n = 0; n < rgb[k].length; n++) {
                        rgb2_json.add(rgb[k][n]);
                    }
                    rgb_json.add(rgb2_json);
                }

                letter.put(String.valueOf(cell.getC()), rgb_json);

                letters_json.add(letter);
            }

            fonts_json.put(font.getName(), letters_json);
        }
        File file = new File("/home/kanumba/Test1");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fonts_json.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end");

        /*try {
            File file = new File("/home/kanumba/Test1");
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeI
            int test[][] = {{1, 2, 3}, {0, 0, 0}, {0, 0, 0}};
            for (int i = 0; i < test.length; i++) {
                dos.write(test[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static ArrayList<FontForTest> getFontsFromJSON () {
        JSONParser parser = new JSONParser();
        JSONObject fonts_json;
        try {
            fonts_json = (JSONObject) parser.parse(new FileReader("/home/kanumba/Test1"));
            Set set = fonts_json.entrySet();
            Object objects[] = set.toArray();
            for (int i = 0; i < objects.length; i++) {
                JSONObject font = (JSONObject) objects[i];
                Set letters = font.keySet();
                //System.out.println(font.);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
