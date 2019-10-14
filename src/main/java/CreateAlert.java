import javafx.scene.control.Alert;
import javafx.stage.Window;

public class CreateAlert {

  CreateAlert(Alert.AlertType alertType, Window window, String title,
              String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.initOwner(window);
    alert.showAndWait();
  }
}
