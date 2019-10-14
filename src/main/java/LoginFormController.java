import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class LoginFormController {

  public final Stage myStage;
  public String name;
  @FXML public TextField registrationNumber;
  @FXML private Button Login;
  @FXML private Button CreateUser;
  @FXML private PasswordField password;
  private Window window;

  public LoginFormController() {

    myStage = new Stage();

    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("loginForm.fxml"));

      loader.setController(this);

      myStage.setScene(new Scene(loader.load()));

      myStage.setTitle("Login Screen");

    } catch (IOException e) {
      e.printStackTrace();
    }
    window = CreateUser.getScene().getWindow();
  }

  public void showStage() { myStage.showAndWait(); }

  @FXML
  private void initialize() {
    Login.setOnAction(event -> {
      try {
        LoginAccount();
      } catch (Exception e) {
        System.out.println(e);
      }
    });

    CreateUser.setOnAction(event -> { CreateUser cu = new CreateUser(this); });
  }

  private void LoginAccount() throws SQLException, ClassNotFoundException {

    Class.forName("org.mariadb.jdbc.Driver");
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost/TAG",
                                               "touchdown", "safer1234");
    Statement statement = c.createStatement();
    String query = "Select `name` from users where `registration_number`=\"" +
                   registrationNumber.getText() + "\" AND `password`=\"" +
                   password.getText() + "\"";
    ResultSet result = statement.executeQuery(query);
    if (!result.next()) {
      new CreateAlert(Alert.AlertType.ERROR, window, "Wrong Credentials",
                      "No user was found with provided Credentials");
      return;
    } else {

      String val = result.getString("name");
      this.name = val;
      new CreateAlert(Alert.AlertType.INFORMATION, window, "Login Sucsessfull ",
                      "You have sucsessfully logged in");
      Register reg = new Register(this);
    }
    c.close();
  }
}
