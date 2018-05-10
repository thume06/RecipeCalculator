package calculator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.String;
import java.util.ArrayList;


public class Main extends Application {

    private static Main instance;

    public static ArrayList<Recipe> savedRecipes = new ArrayList<Recipe>();

    public static String screen1ID = "Calculator";
    public static String screen1File = "Calculator.fxml";
    public static String screen2ID = "SavedRecipes";
    public static String screen2File = "SavedRecipes.fxml";
    public static String screen3ID = "Help";
    public static String screen3File = "Help.fxml";

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

        mainContainer.setScreen(Main.screen1ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("Recipe Calculator");
        stage.getIcons().add(new Image("images/recipeBook.png"));
        //Filter makes so that space can no longer be pressed to interact with controls (except for typing).
        stage.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
            if ( k.getCode() == KeyCode.SPACE){
                k.consume();
            }
        });
        stage.show();
    }

    public void resize(double w, double h){
        stage.setWidth(w);
        stage.setHeight(h);
    }

    public static void Load() {
        //Begin loading from .ser
        try {
            FileInputStream fis = new FileInputStream("Recipe Calculator Save/savedRecipes.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedRecipes = (ArrayList<Recipe>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {

            c.printStackTrace();
            return;
        }
    }

    public static void CreateSaveDirectory(){
        new File("Recipe Calculator Save").mkdirs();
    }

    public static void main (String[]args){
        File saveDir = new File("Recipe Calculator Save/savedRecipes.ser");
        if(saveDir.exists()) {
            Load();
        }
        else{
            CreateSaveDirectory();
        }
        launch(args);
    }

}