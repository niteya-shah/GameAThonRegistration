import javafx.application.Application;
import javafx.stage.Stage;
//import java.sql.*;

public class GameAThonRegistration extends Application {

  public static void main(String[] args) {
    try{
      launch(args);
    }
    catch(Exception e){}
  }

    @Override
    public void start(Stage primaryStage){
        LoginFormController lfc = new LoginFormController();
        try{
        lfc.showStage();
      }
      catch(Exception e){}
    }
}
