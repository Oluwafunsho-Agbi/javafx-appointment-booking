
package c195.View_Controller;

import c195.util.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * After the user logs in the Main screen is displays in the GUI
 * using which user can select the Customers/Appointments/Reports button
 * for appropriate purposes.
 */
public class MainScreenController implements Initializable {
    @FXML
    private Button MainCustomersButton;
    private Parent root;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void MainCustomersButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("CustomerScreen.fxml"));
        stage = (Stage) MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void MainAppointmentsButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("AppointmentAdd.fxml"));
        stage = (Stage) MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void MainReportsButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("Reports.fxml"));
        stage = (Stage) MainCustomersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void MainExitButtonHandler(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ConnectDB.closeConnection();
            System.exit(0);
        }
    }

}
