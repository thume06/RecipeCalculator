package calculator;

import java.awt.*;
import java.io.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorScreen implements Initializable, ControlledScreen {
    static ArrayList<Ingredient> ingredientArray = new ArrayList<Ingredient>();
    ArrayList<Double> conversions = new ArrayList<>();
    ArrayList<String> actions = new ArrayList<>();
    ArrayList<Ingredient> removeArray = new ArrayList<Ingredient>();
    ArrayList<ArrayList<Ingredient>> undoList = new ArrayList<ArrayList<Ingredient>>();

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    TextInputDialog dialog = new TextInputDialog();

    private static ScreensController myController;
    private Main mainClass;
    private double divisionCounter = 0.0;
    public static boolean toLoad = false;
    public static int loadIndex;
    public static String loadText;
    private String name;
    private String unit;
    private double amnt;
    private String amntSelection;
    private int count;
    private int selectionindex;
    private String save;
    public static boolean round = true;

    @FXML Button btnUndo;
    @FXML ImageView imgInfo;
    @FXML ImageView imgDonate;
    @FXML ImageView imgBackground;
    @FXML Button btnRemove;
    @FXML Button btnQuarter;
    @FXML Button btnHalf;
    @FXML Button btnDouble;
    @FXML Button btnQuad;
    @FXML Button btnAdd;
    @FXML Button btnTriple;
    @FXML Button btnClear;
    @FXML Button btnThird;
    @FXML Button btnSave;
    @FXML ListView<String> ingredientList;
    @FXML TextField ingredientName;
    @FXML TextField saveName;
    @FXML ChoiceBox amount;
    @FXML ChoiceBox unitChoices;
    @FXML CheckBox rounding;

    public void initialize(URL url, ResourceBundle rb) {
        mainClass = Main.getInstance();
        InitializeImages();
        unitChoices.setItems(FXCollections.observableArrayList(
                "Cup", "Tbsp", "Tsp", "None", "Other"));
        unitChoices.setValue("Cup");
        amount.setItems(FXCollections.observableArrayList(
                "3", "2", "1", "1/2", "1/3", "2/3", "1/4", "3/4", "1/8", "Other"));
        amount.setValue("1");
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML public void AddPressed(){
        count = 0;
        name = ingredientName.getText();
        if(String.valueOf(unitChoices.getSelectionModel().getSelectedItem()).equals("None")){
            unit = "";
        }
        else{
            unit = String.valueOf(unitChoices.getSelectionModel().getSelectedItem());
        }

        //Makes sure the fields are not empty
        if(name.equals("")){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add an ingredient with an empty name field.");
            alert.showAndWait();
        }
        else {
            //Get the selection of the amount field
            amntSelection = String.valueOf(amount.getSelectionModel().getSelectedItem());
            if(amntSelection.equals("1")){
                amnt = 1;
            }
            else if (amntSelection.equals("2")){
                amnt = 2;
            }
            else if(amntSelection.equals("3")){
                amnt = 3;
            }
            else if (amntSelection.equals("1/2")){
                amnt = .5;
            }
            else if (amntSelection.equals("1/3")){
                amnt = 1.0/3.0;
            }
            else if (amntSelection.equals("2/3")){
                amnt = 2.0/3.0;
            }
            else if (amntSelection.equals("1/4")){
                amnt = .25;
            }
            else if (amntSelection.equals("3/4")){
                amnt = .75;
            }
            else if (amntSelection.equals("1/8")){
                amnt = .125;
            }
            else if (amntSelection.equals("Other")){
                dialog.setTitle("Custom amount");
                dialog.setHeaderText("Use this to enter a custom amount. Only whole and decimal values are valid (ex: 4, .5, 4.5)\n\nWARNING: If you are entering cups or tablespoons greater than 1 with abnormal decimal amounts,\nrounding could be too inaccurate for your recipe.");
                dialog.setContentText("Please enter an amount:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    try{
                        Double.parseDouble(result.get());
                        amnt = Double.valueOf(result.get());
                        dialog.getEditor().clear();
                    }
                    catch(NumberFormatException e){
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid decimal or whole number value.");
                        alert.showAndWait();
                        dialog.getEditor().clear();
                        return;
                    }
                }
                else{
                    dialog.getEditor().clear();
                    return;
                }
            }

            //Code for if all fields have a valid value**********************************************************

            if(unit.equals("Other")){
                dialog.setTitle("Custom unit");
                dialog.setHeaderText("Use this for measurements not listed or other items (ex: oz, ML etc.)");
                dialog.setContentText("Please enter your custom unit:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    if(result.get().equals("")) {
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("A name for the unit must be given.");
                        alert.showAndWait();
                        dialog.getEditor().clear();
                        return;
                    }
                    else{
                        unit = result.get();
                        dialog.getEditor().clear();
                    }
                }
                else{
                    dialog.getEditor().clear();
                    return;
                }
            }
            if(amnt < .125){
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Sorry, to prevent issues you cannot enter a custom amount less than .125 (1/8).");
                alert.showAndWait();
                return;
            }
            ingredientList.getItems().clear();
            ingredientArray.add(new Ingredient(name, amnt, unit));
            actions.add("item");
            while(count < ingredientArray.size()){
                ingredientList.getItems().add(ingredientArray.get(count).getInfo());
                count = count + 1;
            }

            //**************************************************************************************************
        }
    }

    @FXML public void RoundingPressed(){
        if (rounding.isSelected()){
            round = true;
        }
        else{
            round = false;
        }
        count = 0;
        ingredientList.getItems().clear();
        while(count < ingredientArray.size()){
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
    }

    @FXML public void ClearPressed(){
        if(ingredientList.getItems().size() == 0){
            return;
        }
        confirm.setTitle("Clear Recipe");
        confirm.setHeaderText(null);
        confirm.setContentText("WARNING: You can no longer undo actions after clearing the recipe.");
        confirm.setGraphic(null);

        ButtonType buttonTypeOne = new ButtonType("Continue");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == buttonTypeCancel){
            return;
        }
        ingredientList.getItems().clear();
        ingredientArray.clear();
        conversions.clear();
        actions.clear();
        undoList.clear();
        divisionCounter = 0.0;
    }

    @FXML public void QuarterPressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        if(divisionCounter >= 100.0){
            DivisionAlert();
            return;
        }
        ingredientList.getItems().clear();
        divisionCounter = divisionCounter + (100/2.1);
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).quarter();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(4.0);
        actions.add("convert");
    }

    @FXML public void HalfPressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        if(divisionCounter >= 100.0){
            DivisionAlert();
            return;
        }
        ingredientList.getItems().clear();
        divisionCounter = divisionCounter + (100.0/5.1);
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).half();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(2.0);
        actions.add("convert");
    }

    @FXML public void DoublePressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        divisionCounter = divisionCounter + (100.0/5.1);
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).xtwo();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(0.5);
        actions.add("convert");
    }

    @FXML public void QuadPressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        divisionCounter = divisionCounter - (100/2.1);
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).quad();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(0.25);
        actions.add("convert");
    }

    @FXML public void TriplePressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        divisionCounter = divisionCounter - (100/3.1);
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).triple();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(1.0/3.0);
        actions.add("convert");
    }

    @FXML public void ThirdPressed(){
        if(ingredientArray.size() == 0){
            return;
        }
        if(divisionCounter >= 100.0){
            DivisionAlert();
            return;
        }
        ingredientList.getItems().clear();
        divisionCounter = divisionCounter + (100/3.1);
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).third();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(3.0);
        actions.add("convert");
    }

    @FXML public void Remove(){
        if(ingredientList.getSelectionModel().isEmpty()){
            return;
        }
        if (ingredientArray.size() == 1){
            confirm.setTitle("Last Item");
            confirm.setHeaderText(null);
            confirm.setContentText("WARNING: You can no longer undo actions after removing the last item.");
            confirm.setGraphic(null);

            ButtonType buttonTypeOne = new ButtonType("Continue");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirm.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.get() == buttonTypeCancel){
                return;
            }
            divisionCounter = 0.0;
        }
        selectionindex = ingredientList.getSelectionModel().getSelectedIndex();
        ingredientList.getItems().remove(selectionindex);
        ingredientList.getSelectionModel().select(selectionindex);
        removeArray.add(ingredientArray.get(selectionindex));
        ingredientArray.remove(selectionindex);
        ingredientList.getSelectionModel().clearSelection();
        actions.add("remove");
        if (ingredientArray.size() == 0){
            conversions.clear();
            actions.clear();
            undoList.clear();
        }
    }

    @FXML public void Undo(){
        if(actions.size() == 0){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No actions to undo.");
            alert.showAndWait();
            return;
        }

        if(actions.get(actions.size() - 1).equals("convert")){
            //We need to multiply by the value in the .equals, so 4.0 means call Quad method, 2.0 Double, etc.
            if(conversions.get(conversions.size() - 1).equals(4.0)){
                QuadPressed();
            }
            else if(conversions.get(conversions.size() - 1).equals(2.0)){
                DoublePressed();
            }
            else if(conversions.get(conversions.size() - 1).equals(0.5)){
                HalfPressed();
            }
            else if(conversions.get(conversions.size() - 1).equals(0.25)){
                QuarterPressed();
            }
            else if(conversions.get(conversions.size() - 1).equals(1.0/3.0)){
                ThirdPressed();
            }
            else if(conversions.get(conversions.size() - 1).equals(3.0)){
                TriplePressed();
            }
            //This is done twice because we are adding to it by calling the button pressed methods.
            conversions.remove(conversions.size()-1);
            conversions.remove(conversions.size()-1);
            actions.remove(actions.size() - 1);
            actions.remove(actions.size() -1);
        }
        else if(actions.get(actions.size() -1).equals("item")){
            ingredientList.getItems().remove(ingredientArray.size() -1);
            ingredientArray.remove(ingredientArray.size() - 1);
            actions.remove(actions.size() - 1);
        }
        else if(actions.get(actions.size() - 1).equals("remove")){
            ingredientList.getItems().add(removeArray.get(removeArray.size() - 1).getInfo());
            ingredientArray.add(removeArray.get(removeArray.size() - 1));
            removeArray.remove(removeArray.size() -1);
            actions.remove(actions.size() -1);
        }
    }

    @FXML public void Help(){
        myController.setScreen(Main.screen3ID);
    }
    @FXML public void HelpHover(){
        imgInfo.setImage(new Image("images/infoHover.png"));
        imgInfo.setFitWidth(55);
        imgInfo.setFitHeight(48);
        imgInfo.setLayoutX(457.5);
        imgInfo.setLayoutY(17);
    }
    @FXML public void HelpExit(){
        imgInfo.setImage(new Image("images/info.png"));
        imgInfo.setFitWidth(45);
        imgInfo.setFitWidth(39);
        imgInfo.setLayoutX(462);
        imgInfo.setLayoutY(19);
    }
    @FXML public void Donate(){
        openWebpage();
    }
    @FXML public void DonateHover(){
        imgDonate.setImage(new Image("images/donateOver.png"));
        imgDonate.setFitWidth(230);
        imgDonate.setFitHeight(62);
        imgDonate.setLayoutX(37);
        imgDonate.setLayoutY(605);
    }
    @FXML public void DonateExit(){
        imgDonate.setImage(new Image("images/donate.png"));
        imgDonate.setFitWidth(209);
        imgDonate.setFitHeight(52);
        imgDonate.setLayoutX(45);
        imgDonate.setLayoutY(608);
    }

    @FXML public void Save(){
        //Ensuring filename is valid--------------------------------------------
        String originalName = saveName.getText();
        save = saveName.getText().toLowerCase();
        if(ingredientArray.size() == 0){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Cannot save while the recipe list is empty.");
            alert.showAndWait();
            return;
        }
        if(save.length() >= 252){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Recipe name is too long (max length: 252).");
            alert.showAndWait();
            return;
        }
        if(save.equals("")){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("A recipe name must be entered before saving.");
            alert.showAndWait();
            return;
        }

        //Save begins after checks
        try{
            boolean overwritten = false;
            FileOutputStream fos= new FileOutputStream("Recipe Calculator Save/savedRecipes.ser");
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            ArrayList<Ingredient> tempIngredientArray = new ArrayList<Ingredient>();
            count = 0;
            while(count < ingredientArray.size()){
                tempIngredientArray.add(new Ingredient(ingredientArray.get(count).getName(), ingredientArray.get(count).getAmount(), ingredientArray.get(count).getUnit()));
                count++;
            }
            count = 0;
            while(count < Main.savedRecipes.size()){
                if(Main.savedRecipes.get(count).getName().equals(originalName)){
                    Main.savedRecipes.remove(count);
                    overwritten = true;
                }
                count++;
            }
            Main.savedRecipes.add(new Recipe(originalName, tempIngredientArray));
            oos.writeObject(Main.savedRecipes);
            oos.close();
            fos.close();
            Collections.sort(Main.savedRecipes, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe r1, Recipe r2) {
                    String s1 = r1.getName();
                    String s2 = r2.getName();
                    return s1.compareToIgnoreCase(s2);
                }

            });
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            if(overwritten){
                alert.setContentText("Recipe overwritten.");
            }
            else{
                alert.setContentText("Recipe saved.");
            }
            alert.showAndWait();
        }
        catch(IOException ioe){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save recipe.");
            alert.showAndWait();
            ioe.printStackTrace();
        }

    }

    public static Boolean getRound(){
        return round;
    }

    public static void openWebpage() {
        try {
            Desktop.getDesktop().browse(new URL("https://www.paypal.me/thume02").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void SavedRecipes(){
        myController.setScreen(Main.screen2ID);
    }

    public void DivisionAlert(){
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Too many divisions. Stop trying to break the program!");
        alert.showAndWait();
    }

    @FXML public void CheckToLoad(){
        if(toLoad){
            ingredientArray.clear();
            ingredientList.getItems().clear();
            count = 0;
            while(count < Main.savedRecipes.get(loadIndex).getRecipe().size()){
                String ingredientName = Main.savedRecipes.get(loadIndex).getRecipe().get(count).getName();
                double ingredientAmount = Main.savedRecipes.get(loadIndex).getRecipe().get(count).getAmount();
                String ingredientUnit = Main.savedRecipes.get(loadIndex).getRecipe().get(count).getUnit();
                ingredientArray.add(new Ingredient(ingredientName, ingredientAmount, ingredientUnit));
                ingredientList.getItems().add(Main.savedRecipes.get(loadIndex).getRecipe().get(count).getInfo());
                count++;
            }
            saveName.setText(loadText);
            undoList.clear();
            actions.clear();
            toLoad = false;
        }
        else{
            return;
        }
    }

    public void InitializeImages(){
        imgInfo.setImage(new Image("images/info.png"));
        imgDonate.setImage(new Image("images/donate.png"));
        imgBackground.setImage(new Image("images/background.jpg"));
    }
}