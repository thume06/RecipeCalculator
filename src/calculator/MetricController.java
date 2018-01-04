package calculator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class MetricController implements Initializable, ControlledScreen {
    private ScreensController myController;
    private Main mainClass;
    private int count = 0;
    private static boolean toAdd = false;

    ArrayList<MetricIngredient> metricArray = new ArrayList<MetricIngredient>();
    static ArrayList<Ingredient> addList = new ArrayList<Ingredient>();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    TextInputDialog dialog = new TextInputDialog();

    @FXML Button btnReturn;
    @FXML ListView convertOptions;
    @FXML Button btnAdd;
    @FXML Button btnRemove;
    @FXML TextField ingredientName;
    @FXML ChoiceBox stdAmount;
    @FXML ChoiceBox stdUnits;
    @FXML TextField metricAmount;
    @FXML TextField metricAmount2;

    public void initialize(URL url, ResourceBundle rb) {
        mainClass = Main.getInstance();
        MetricIngredient apflour = new MetricIngredient("flour", 1, "Cup", 125);

        stdUnits.setItems(FXCollections.observableArrayList(
                "Cup", "Tbsp", "Tsp", "Other"));
        stdUnits.setValue("Cup");
        stdAmount.setItems(FXCollections.observableArrayList(
                "1", "1/2", "1/3", "2/3", "1/4", "3/4", "1/8", "Other"));
        stdAmount.setValue("1");
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML public void Calculator(){
        myController.setScreen(Main.screen1ID);
    }

    @FXML public void AddPressed(){
        if(ingredientName.getText().equals("")){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add an ingredient with an empty name field");
            alert.showAndWait();
            return;
        }
        String amntSelection = String.valueOf(stdAmount.getSelectionModel().getSelectedItem());
        Double standardAmount = 0.0;
        if(amntSelection.equals("1")){
            standardAmount = 1.0;
        }
        else if (amntSelection.equals("1/2")){
            standardAmount = .5;
        }
        else if (amntSelection.equals("1/3")){
            standardAmount = 1.0/3.0;
        }
        else if (amntSelection.equals("2/3")){
            standardAmount = 2.0/3.0;
        }
        else if (amntSelection.equals("1/4")){
            standardAmount = .25;
        }
        else if (amntSelection.equals("3/4")){
            standardAmount = .75;
        }
        else if (amntSelection.equals("1/8")){
            standardAmount = .125;
        }
        else if (amntSelection.equals("Other")) {
            dialog.setTitle("Custom amount");
            dialog.setHeaderText("Use this to enter a custom amount. Only whole and decimal values are valid (ex: 4, .5, 4.5)\n\nWARNING: If you are entering cups or tablespoons greater than 1 with abnormal decimal amounts,\nrounding could be too inaccurate for your recipe.");
            dialog.setContentText("Please enter an amount:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    Double.parseDouble(result.get());
                    standardAmount = Double.valueOf(result.get());
                } catch (NumberFormatException e) {
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid decimal or whole number value");
                    alert.showAndWait();
                    return;
                }
            }
            else {
                return;
            }
        }
        if(metricAmount.getText().equals("")){
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add an ingredient with an empty metric amount field");
            alert.showAndWait();
            return;
        }
        try{
            Double.parseDouble(metricAmount.getText());
        }
        catch(NumberFormatException e){
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid decimal or whole number value in metric amount field");
            alert.showAndWait();
            return;
        }

        String standardUnit = String.valueOf(stdUnits.getSelectionModel().getSelectedItem());
        metricArray.add(new MetricIngredient(ingredientName.getText(), standardAmount, standardUnit, Double.valueOf(metricAmount.getText())));
        count = metricArray.size() - 1;
        convertOptions.getItems().add(ingredientName.getText() + " (" + standardAmount + " " +standardUnit + " = " + metricAmount.getText() + " grams)");
    }

    @FXML public void Remove(){
        int selectionIndex = convertOptions.getSelectionModel().getSelectedIndex();
        convertOptions.getItems().remove(selectionIndex);
        convertOptions.getSelectionModel().select(selectionIndex);
        metricArray.remove(selectionIndex);
        convertOptions.getSelectionModel().clearSelection();
    }

    @FXML public void Convert(){
        double grams;
        try{
            Double.parseDouble(metricAmount2.getText());
            grams = Double.valueOf(metricAmount2.getText());
        }
        catch(NumberFormatException e){
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid decimal or whole number value in metric amount field");
            alert.showAndWait();
            return;
        }
        int selectionIndex = convertOptions.getSelectionModel().getSelectedIndex();
        metricArray.get(selectionIndex).GramstoStd(grams);

        confirm.setTitle("Conversion");
        confirm.setHeaderText(null);
        confirm.setContentText("The standard amount of " + grams + " grams for " + metricArray.get(selectionIndex).getName() + " is about " + metricArray.get(selectionIndex).getStdAmnt() + " " + metricArray.get(selectionIndex).getStdUnit() + "\n\nWould you like to add this to your recipe?");
        confirm.setGraphic(null);

        ButtonType buttonTypeOne = new ButtonType("Add");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == buttonTypeCancel){
            return;
        }

        //WHEN THEY HIT ADD
        addList.add(new Ingredient(metricArray.get(selectionIndex).getName(), metricArray.get(selectionIndex).getStdAmnt(), metricArray.get(selectionIndex).getStdUnit()));
        toAdd = true;
    }

    public static boolean getToAdd(){
        return toAdd;
    }
    public static void setToAdd(boolean a){
        toAdd = a;
    }
}
