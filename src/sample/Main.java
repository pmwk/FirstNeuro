package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

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

        CreateTestSet.createPane();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
