package calculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorScreen implements Initializable, ControlledScreen {
    ArrayList<Ingredient> ingredientArray = new ArrayList<Ingredient>();
    ArrayList<Character> invalidChars = new ArrayList<>();
    ArrayList<String> invalidStrings = new ArrayList<>();
    ArrayList<Double> conversions = new ArrayList<>();

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    TextInputDialog dialog = new TextInputDialog();
    DirectoryChooser dchoose = new DirectoryChooser();
    String selectedDirectory;

    private ScreensController myController;
    private Main mainClass;
    private String name;
    private String unit;
    private double amnt;
    private String amntSelection;
    private int count;
    private int selectionindex;
    private String save;
    private Character tempChar;
    private String tempString;
    public static boolean round = true;

    @FXML Button btnUndo;
    @FXML ImageView imgInfo;
    @FXML ImageView imgDonate;
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
        unitChoices.setItems(FXCollections.observableArrayList(
                "Cup", "Tbsp", "Tsp", "Other"));
        unitChoices.setValue("Cup");
        amount.setItems(FXCollections.observableArrayList(
                "1", "1/2", "1/3", "2/3", "1/4", "3/4", "1/8", "Other"));
        amount.setValue("1");

        invalidChars.add('.');
        invalidChars.add('\\');
        invalidChars.add('/');
        invalidChars.add(':');
        invalidChars.add('*');
        invalidChars.add('?');
        invalidChars.add('"');
        invalidChars.add('<');
        invalidChars.add('>');
        invalidChars.add('|');
        invalidStrings.add("aux");
        invalidStrings.add("com1");
        invalidStrings.add("com2");
        invalidStrings.add("com3");
        invalidStrings.add("com4");
        invalidStrings.add("com5");
        invalidStrings.add("com6");
        invalidStrings.add("com7");
        invalidStrings.add("com8");
        invalidStrings.add("com9");
        invalidStrings.add("con");
        invalidStrings.add("lpt1");
        invalidStrings.add("lpt2");
        invalidStrings.add("lpt3");
        invalidStrings.add("lpt4");
        invalidStrings.add("lpt5");
        invalidStrings.add("lpt6");
        invalidStrings.add("lpt7");
        invalidStrings.add("lpt8");
        invalidStrings.add("lpt9");
        invalidStrings.add("nul");
        invalidStrings.add("prn");
        invalidStrings.add("clock$");
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML public void AddPressed(){
        count = 0;
        name = ingredientName.getText();
        unit = String.valueOf(unitChoices.getSelectionModel().getSelectedItem());

        //Makes sure the fields are not empty
        if(name.equals("")){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add an ingredient with an empty name field");
            alert.showAndWait();
        }
        else {
            //Get the selection of the amount field
            amntSelection = String.valueOf(amount.getSelectionModel().getSelectedItem());
            if(amntSelection.equals("1")){
                amnt = 1;
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
                    }
                    catch(NumberFormatException e){
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid decimal or whole number value");
                        alert.showAndWait();
                        return;
                    }
                }
                else{
                    return;
                }
            }

            //Code for if all fields have a valid value**********************************************************
            ingredientList.getItems().clear();

            if(unit.equals("Other")){
                dialog.setTitle("Custom unit");
                dialog.setHeaderText("Use this for measurements not listed or other items (ex: oz, ML etc.)");
                dialog.setContentText("Please enter your custom unit:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    if(result.get().equals("")) {
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("A name for the unit must be given");
                        alert.showAndWait();
                        return;
                    }
                    else{
                        unit = result.get();
                    }
                }
                else{
                    return;
                }
            }
            ingredientArray.add(new Ingredient(name, amnt, unit));
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
        ingredientList.getItems().clear();
        ingredientArray.clear();
        conversions.clear();
    }

    @FXML public void QuarterPressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).quarter();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(4.0);
    }

    @FXML public void HalfPressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).half();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(2.0);
    }

    @FXML public void DoublePressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).xtwo();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(0.5);
    }

    @FXML public void QuadPressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).quad();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(0.25);
    }

    @FXML public void TriplePressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).triple();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(1.0/3.0);
    }

    @FXML public void ThirdPressed(){
        ingredientList.getItems().clear();
        count = 0;
        while(count < ingredientArray.size()){
            ingredientArray.get(count).third();
            ingredientList.getItems().add(ingredientArray.get(count).getInfo());
            count = count + 1;
        }
        conversions.add(3.0);
    }

    @FXML public void Remove(){
        selectionindex = ingredientList.getSelectionModel().getSelectedIndex();
        ingredientList.getItems().remove(selectionindex);
        ingredientList.getSelectionModel().select(selectionindex);
        ingredientArray.remove(selectionindex);
        ingredientList.getSelectionModel().clearSelection();

        if (ingredientArray.size() == 0){
            conversions.clear();
        }
    }

    @FXML public void Undo(){
        if(conversions.size() == 0){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No conversions to undo.");
            alert.showAndWait();
            return;
        }

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
        myController.setScreen(Main.screen2ID);
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
        count = 0;
        while(count < invalidChars.size()){
            tempChar = invalidChars.get(count);
            if (save.contains(String.valueOf(tempChar))){
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid character: " + String.valueOf(tempChar));
                alert.showAndWait();
                return;
            }
            count = count + 1;
        }
        count = 0;
        while(count < invalidStrings.size()){
            tempString = invalidStrings.get(count);
            if (save.contains(tempString)){
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid text in filename: " + tempString);
                alert.showAndWait();
                return;
            }
            count = count + 1;
        }
        //Code executes--------------------------------------------

        confirm.setTitle("Save Recipe");
        confirm.setHeaderText(null);
        confirm.setContentText("Note: Saving a recipe to a directory that already has a file with the same name will overwrite it.");
        confirm.setGraphic(null);

        ButtonType buttonTypeOne = new ButtonType("Select Directory");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == buttonTypeOne){
            selectedDirectory = String.valueOf(dchoose.showDialog(null));

            if (selectedDirectory != ""){
                selectedDirectory = selectedDirectory + "\\" + originalName + ".txt";
                try{
                    FileWriter fileWriter = new FileWriter(selectedDirectory);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    int endCount = 0;
                    while(endCount < ingredientArray.size()){
                        bufferedWriter.write(ingredientArray.get(endCount).getInfo());
                        bufferedWriter.newLine();
                        endCount = endCount + 1;
                    }
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Recipe successfuly saved to: " + selectedDirectory);
                    alert.showAndWait();

                    bufferedWriter.close();
                }
                catch(IOException ex){
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("There was an error saving the recipe.");
                    alert.showAndWait();
                    return;
                }
            }
        }
        else {
            return;
        }
    }

    public static Boolean getRound(){
        return round;
    }

}
