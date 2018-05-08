package calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class SavedRecipes implements Initializable, ControlledScreen{
    private ScreensController myController;
    private Main mainClass;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

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
    //pressing space to interact with the button you can avoid calling this method. Can fix by switching button to ImageView, which must be clicked.
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
        CalculatorScreen.toLoad = true;
        int index = recipeList.getSelectionModel().getSelectedIndex();
        CalculatorScreen.loadIndex = index;
        CalculatorScreen.loadText = Main.savedRecipes.get(index).getName();
        recipeList.getItems().clear();
        loaded = false;
        myController.setScreen(Main.screen1ID);
    }

    @FXML public void DeleteSelectedRecipe(){
        int selectionindex = recipeList.getSelectionModel().getSelectedIndex();
        recipeList.getItems().remove(selectionindex);
        recipeList.getSelectionModel().select(selectionindex);
        Main.savedRecipes.remove(selectionindex);
        recipeList.getSelectionModel().clearSelection();
        Save();
    }

    public void Save(){
        try{
            FileOutputStream fos= new FileOutputStream("savedRecipes.ser");
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(Main.savedRecipes);
            oos.close();
            fos.close();
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Recipe removed.");
            alert.showAndWait();
        }
        catch(IOException ioe){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Failed to remove recipe.");
            alert.showAndWait();
            ioe.printStackTrace();
        }
    }
}
