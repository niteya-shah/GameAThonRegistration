import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import java.io.IOException;

public class Register {

  private Stage myStage;

  private String registrationNumber;

  public String name;

  private Window window;

  private int count;

  @FXML private Label lbl;

  @FXML private CheckBox dota2;

  @FXML private CheckBox fifa;

  @FXML private CheckBox csgo;

  @FXML private CheckBox r6s;

  @FXML private Button makePayment;

  @FXML private Label allPaid;

  public Register(LoginFormController lfc) {

    this.myStage = lfc.myStage;

    this.registrationNumber = lfc.registrationNumber.getText();

    this.name = lfc.name;

    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("registrationForm.fxml"));

      loader.setController(this);

      myStage.setScene(new Scene(loader.load(), 400, 400));

      myStage.setTitle("Register for Event");

    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println(e);
    }
    window = makePayment.getScene().getWindow();
  }

  public Register(CreateUser cu) {

    this.myStage = cu.myStage;

    this.registrationNumber = cu.registrationNumber.getText();

    this.name = cu.name.getText();
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("registrationForm.fxml"));

      loader.setController(this);

      myStage.setScene(new Scene(loader.load(), 400, 400));

      myStage.setTitle("Register for Event");

    } catch (IOException e) {
      e.printStackTrace();
    }
    window = makePayment.getScene().getWindow();
  }

  /**
   * Show the stage that was loaded in the constructor    window =
   * makePayment.getScene().getWindow();
   */

  @FXML
  private void initialize() {
    try {
      populateCheckboxes();
    } catch (Exception e) {
      System.out.println(e);
    }
    lbl.setText("Hello " + name);
    allPaid.setVisible(false);
    makePayment.setOnAction(event -> {
      try {
        pay();
      } catch (Exception e) {
        System.out.println(e);
      }
    });
  }

  private void pay() throws SQLException, ClassNotFoundException {
    int arr[] = {0, 0, 0, 0};
    int new_count = 0;
    if (dota2.isSelected()) {
      arr[1] = 1;
      new_count++;
    }
    if (fifa.isSelected()) {
      arr[0] = 1;
      new_count++;
    }
    if (r6s.isSelected()) {
      arr[3] = 1;
      new_count++;
    }
    if (csgo.isSelected()) {
      arr[2] = 1;
      new_count++;
    }
    if (new_count == count && count != 0) {
      new CreateAlert(Alert.AlertType.ERROR, window, "No new Selection",
                      "You have not selected any new Event");
    }
    Class.forName("org.mariadb.jdbc.Driver");
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost/TAG",
                                               "touchdown", "safer1234");
    Statement statement = c.createStatement();
    String query =
        "Select 1 from `registration` where `registration_number`=\"" +
        registrationNumber + "\"";
    ResultSet result = statement.executeQuery(query);
    if (!result.next()) {
      query =
          "Insert into `registration`(`reg_id`,`registration_number`,`fifa`,`dota2`,`csgo`,`r6s`) value(null,\"" +
          registrationNumber + "\",\"" + arr[0] + "\",\"" + arr[1] + "\",\"" +
          arr[2] + "\",\"" + arr[3] + "\")";
      result = statement.executeQuery(query);
    } else {
      query = "UPDATE `registration` SET `fifa`= \"" + arr[0] +
              "\",`dota2`= \"" + arr[1] + "\",`csgo`= \"" + arr[2] +
              "\",`r6s`= \"" + arr[3] + "\" where `registration_number`=\"" +
              registrationNumber + "\"";
      result = statement.executeQuery(query);
    }
    c.close();
    new CreateAlert(
        Alert.AlertType.INFORMATION, window, "Success",
        "Event Registration was successfull. Your current registration is \n1.Dota 2: " +
            isTrue(arr[1]) + "\n2.CSGO: " + isTrue(arr[2]) +
            "\n3.FIFA: " + isTrue(arr[0]) + "\n4.R6S: " + isTrue(arr[3]));

    Platform.exit();
    System.exit(0);
  }

  private String isTrue(int b) {
    if (b == 1)
      return "Yes";
    else
      return "No";
  }

  private void populateCheckboxes()
      throws SQLException, ClassNotFoundException {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost/TAG",
                                               "touchdown", "safer1234");
    Statement statement = c.createStatement();
    String query =
        "Select `FIFA`,`CSGO`,`R6S`,`DOTA2` from `registration` where `registration_number`=\"" +
        registrationNumber + "\"";
    ResultSet result = statement.executeQuery(query);

    if (result.next()) {
      String value = result.getString("dota2");
      count = 0;
      if (Integer.parseInt(value) == 1) {
        dota2.setSelected(true);
        dota2.setDisable(true);
        count++;
      }
      value = result.getString("fifa");
      if (Integer.parseInt(value) == 1) {
        fifa.setSelected(true);
        fifa.setDisable(true);
        count++;
      }
      value = result.getString("csgo");
      if (Integer.parseInt(value) == 1) {
        csgo.setSelected(true);
        csgo.setDisable(true);
        count++;
      }
      value = result.getString("r6s");
      if (Integer.parseInt(value) == 1) {
        r6s.setSelected(true);
        r6s.setDisable(true);
        count++;
      }

      if (count == 4) {
        makePayment.setDisable(true);
        allPaid.setText("You have Registered for all Events");
        allPaid.setVisible(true);

        new CreateAlert(
            Alert.AlertType.INFORMATION, window, "Registered for All",
            "You have registered for all the Events. Please wait for the email that will convey the date and venue");
      }
    }
    c.close();
  }
}
