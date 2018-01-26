package calculator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddController implements Initializable, ControlledScreen {
    private ScreensController myController;
    private Main mainClass;

    static ArrayList<MetricIngredient> metricArray = new ArrayList<MetricIngredient>();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    TextInputDialog dialog = new TextInputDialog();
    private int count = 0;
    protected static boolean toAdd = false;
    protected static String addString = "Null";

    @FXML TextField ingredientName;
    @FXML TextField metricAmount;
    @FXML ChoiceBox stdAmount;
    @FXML ChoiceBox stdUnits;

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    public void initialize(URL url, ResourceBundle rb) {
        mainClass = Main.getInstance();
        stdUnits.setItems(FXCollections.observableArrayList(
                "Standard Unit", "Cup", "Tbsp", "Tsp"));
        stdUnits.setValue("Standard Unit");
        stdAmount.setItems(FXCollections.observableArrayList(
                "Standard Amount", "1", "1/2", "1/3", "2/3", "1/4", "3/4", "1/8", "Other"));
        stdAmount.setValue("Standard Amount");
    }
    @FXML public void Cancel(){
        ingredientName.clear();
        metricAmount.clear();
        stdAmount.setValue("Standard Amount");
        stdUnits.setValue("Standard Unit");
        mainClass.resize(534, 710);
        myController.setScreen(Main.screen4ID);
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
        else{
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a standard amount");
            alert.showAndWait();
            return;
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
        if(standardUnit.equals("Standard Unit")){
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a standard unit");
            alert.showAndWait();
            return;
        }
        metricArray.add(new MetricIngredient(ingredientName.getText(), standardAmount, standardUnit, Double.valueOf(metricAmount.getText())));
        String add = metricArray.get(metricArray.size()-1).getTextInfo();
        try {
            Files.write(Paths.get("conversions.txt"), ("," + add).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        count = metricArray.size() - 1;
        toAdd = true;
        addString = (ingredientName.getText() + " (" + standardAmount + " " +standardUnit + " = " + metricAmount.getText() + " grams)");
        ingredientName.clear();
        metricAmount.clear();
        stdAmount.setValue("Standard Amount");
        stdUnits.setValue("Standard Unit");
        mainClass.resize(534, 710);
        myController.setScreen(Main.screen4ID);
    }

}
