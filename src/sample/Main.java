package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    static VBox root;

    @Override
    public void start(Stage stage) throws Exception{
        /*root = new VBox();
        ScrollPane new_root = new ScrollPane();

        new_root.setContent(root);

        Scene scene = new Scene(new_root, 200, 800);
        stage.setScene(scene);
        stage.show();*/

        //CreateTestBMP.startTest();

        //CreateTestSet.createPane();
        //ArrayList<SimpleFontForTest> fonts = FileManager.getFontsFromData();

        /*Scanner in = new Scanner(System.in);
        while (true) {
            int font = in.nextInt();
            int letter = in.nextInt();

            int rgb[][] = fonts.get(font).letters.get(letter).rgb;
            for (int i = 0; i < rgb.length; i++) {
                for (int j = 0; j < rgb[i].length; j++) {
                    System.out.print(rgb[i][j]);
                }
                System.out.println();
            }
        }*/

        Pane root = new MainMenu();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
