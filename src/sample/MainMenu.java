package sample;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainMenu extends Pane {

    public MainMenu() {
        super();
        setPrefSize(400, 700);

        VBox menu_root = new VBox();

        Button createNewData_but = new Button("Создать новый набор данных");
        Button learnNeuro_but = new Button("Начать обучение нейросети");

        menu_root.getChildren().addAll(createNewData_but, learnNeuro_but);

        getChildren().add(menu_root);

        createNewData_but.setOnMouseClicked(event -> {
            CreateTestSet.createPane();
        });
    }
}
