package calculator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable, ControlledScreen{
    private Main mainClass;
    private ScreensController myController;

    @FXML Button btnReturn;
    @FXML ImageView background;

    public void initialize(URL url, ResourceBundle rb) {
        mainClass = Main.getInstance();
        background.setImage(new Image("images/background.jpg"));
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML public void calcScreen(){
        myController.setScreen(Main.screen1ID);
    }

}

