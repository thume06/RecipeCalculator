package calculator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.lang.String;


public class Main extends Application {

    private static Main instance;

    public static String screen1ID = "Calculator";
    public static String screen1File = "Calculator.fxml";
    public static String screen2ID = "Donate";
    public static String screen2File = "Donate.fxml";
    public static String screen3ID = "Help";
    public static String screen3File = "Help.fxml";
    public static String screen4ID = "Metric";
    public static String screen4File = "Metric.fxml";

    private Stage stage;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.screen1ID, Main.screen1File);
        mainContainer.loadScreen(Main.screen2ID, Main.screen2File);
        mainContainer.loadScreen(Main.screen3ID, Main.screen3File);
        mainContainer.loadScreen(Main.screen4ID, Main.screen4File);

        mainContainer.setScreen(Main.screen1ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("Recipe Calculator");
        stage.getIcons().add(new Image("images/recipeBook.png"));
        stage.show();
    }


    public static void main (String[]args){
        launch(args);
    }
}