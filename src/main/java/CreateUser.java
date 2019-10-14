import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import java.io.IOException;

public class CreateUser {

  public Stage myStage;

  @FXML public TextField registrationNumber;

  @FXML public TextField name;

  @FXML private TextField surname;

  @FXML private PasswordField password;

  @FXML private TextField mobile_no;

  @FXML private ComboBox<String> Year;

  @FXML private Button createUser;

  private Window window;

  public CreateUser(LoginFormController lfc) {

    this.myStage = lfc.myStage;

    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("createUser.fxml"));

      loader.setController(this);

      myStage.setScene(new Scene(loader.load(), 600, 600));

      myStage.setTitle("Create new User");

    } catch (IOException e) {
      e.printStackTrace();
    }
    window = createUser.getScene().getWindow();
  }

  @FXML
  private void initialize() {
    String years[] = new String[] {"1", "2", "3", "4"};

    ObservableList<String> data = FXCollections.observableArrayList(years);
    Year.setItems(data);
    createUser.setOnAction(event -> {
      try {
        createu();
      } catch (Exception e) {
        System.out.println(e);
      }
    });
  }

  private void createu() throws SQLException, ClassNotFoundException {
    if (registrationNumber.getText().isEmpty() || name.getText().isEmpty() ||
        surname.getText().isEmpty() || password.getText().isEmpty() ||
        mobile_no.getText().isEmpty() || (Year.getValue() == null)) {
      new CreateAlert(Alert.AlertType.ERROR, window, "Fields Empty",
                      "Please Fill all the Fields");
      return;
    } else {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection c = DriverManager.getConnection("jdbc:mysql://localhost/TAG",
                                                 "touchdown", "safer1234");
      Statement statement = c.createStatement();
      String query =
          "Insert into `users`(`name`,`Surname`,`registration_number`,`password`,`mobile_no`,`YEAR`) VALUES(\"" +
          name.getText() + "\",\"" + surname.getText() + "\",\"" +
          registrationNumber.getText() + "\",\"" + password.getText() +
          "\",\"" + mobile_no.getText() + "\",\"" +
          Integer.parseInt(Year.getValue()) + "\")";
      ResultSet result = statement.executeQuery(query);
      c.close();
      new CreateAlert(Alert.AlertType.INFORMATION, window, "Success",
                      "You have sucsessfully created a new profile");
      Register reg = new Register(this);
    }
  }
}
