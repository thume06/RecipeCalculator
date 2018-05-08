package calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ResourceBundle;

public class SavedRecipes implements Initializable, ControlledScreen{
    private ScreensController myController;
    private Main mainClass;

    private boolean loaded = false;

    @FXML ImageView background;
    @FXML ListView recipeList;

    public void initialize(URL url, ResourceBundle rb){
        mainClass = Main.getInstance();
        InitializeImages();
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    private void InitializeImages(){
        background.setImage(new Image("images/background.jpg"));
    }

    //This method is only called when the mouse is detected over the screen when it is switched, so by having the mouse off the screen and
    //pressing enter you can avoid calling this method. Not really sure how to fix this.
    @FXML public void ScreenLoaded(){
        if(!loaded){
            int count = 0;
            while(count < Main.savedRecipes.size()){
              recipeList.getItems().add(Main.savedRecipes.get(count).getName());
              count++;
            }
        }
        loaded = true;
    }

    @FXML public void ReturnToCalculator(){
        recipeList.getItems().clear();
        loaded = false;
        myController.setScreen(Main.screen1ID);
    }

    @FXML public void LoadSelectedRecipe(){
        recipeList.getItems().clear();
        loaded = false;
        myController.setScreen(Main.screen1ID);
    }
}
