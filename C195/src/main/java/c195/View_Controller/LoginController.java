
package c195.View_Controller;

import c195.Model.Appointment;
import c195.Model.User;
import c195.util.ConnectDB;
import c195.util.DBUtility;
import c195.util.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class handles the initial screen which is the login
 * Screen where the user needs to enter the login and password details
 * to enter into the application
 */
public class LoginController implements Initializable {
    @FXML
    private TextField UsernameTextField;
    @FXML
    private Label LoginUsernameLabel;
    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private Label LoginPasswordLabel;
    @FXML
    private Button LoginButton;
    @FXML
    private Label LoginLabel;
    @FXML
    private Label LocationLabel;
    private ResourceBundle resourceBundle;
    private ObservableList<Appointment> appointmentReminderOL = FXCollections.observableArrayList();
    private DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private ZoneId localZoneId = ZoneId.systemDefault();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            resourceBundle = ResourceBundle.getBundle("login", Locale.getDefault());
            this.resourceBundle = resourceBundle;
            Calendar now = Calendar.getInstance();
            TimeZone timeZone = now.getTimeZone();
            LocationLabel.setText(timeZone.getID());
            LoginLabel.setText(resourceBundle.getString("title"));
            LoginUsernameLabel.setText(resourceBundle.getString("username"));
            UsernameTextField.setPromptText(resourceBundle.getString("username"));
            LoginPasswordLabel.setText(resourceBundle.getString("password"));
            PasswordTextField.setPromptText(resourceBundle.getString("password"));
            LoginButton.setText(resourceBundle.getString("signin"));
        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
    }

    /**
     * If there is an appointment within next 15 minutes of user login
     * This will pop up an alert message to indicate that to the user
     */
    private void appointmentAlert() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentReminderOL);
        filteredData.setPredicate(row -> {
                    LocalDateTime rowDate = LocalDateTime.parse(row.getStart().substring(0, 16), datetimeDTF);
                    return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
                }
        );
        if (filteredData.isEmpty()) {
        } else {
            String customer = DBUtility.getCustomerName(filteredData.get(0).getCustomerID());
            String start = filteredData.get(0).getStart().substring(0, 16);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Your upcoming appointment with " + customer
                    + " is currently set to begin at " + start + ".");
            alert.showAndWait();
        }

    }

    /**
     * This will generate the appointments that are within next 15 mins
     */
    private void createReminderList() {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(
                    "SELECT appointments.Appointment_ID, appointments.Customer_ID, appointments.Title, appointments.Description, "
                            + "appointments.`Start`, appointments.`End`, customers.Customer_ID, customers.Customer_Name, appointments.Created_By "
                            + "FROM appointments, customers "
                            + "WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Created_By = ? "
                            + "ORDER BY `Start`");
            ps.setString(1, User.getUsername());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timestamp timestampStart = rs.getTimestamp("start");
                ZonedDateTime startUTC = timestampStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = startUTC.withZoneSameInstant(localZoneId);
                Timestamp timestampEnd = rs.getTimestamp("end");
                ZonedDateTime endUTC = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = endUTC.withZoneSameInstant(localZoneId);
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Description");
                int customerId = rs.getInt("Customer_ID");
                String user = rs.getString("Created_By");
                appointmentReminderOL.add(new Appointment(appointmentId, newLocalStart.toString(), newLocalEnd.toString(), title, type, customerId, user));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void LoginButtonHandler(ActionEvent event) throws SQLException, IOException {
        String usernameInput = UsernameTextField.getText();
        String passwordInput = PasswordTextField.getText();
        int userID = getUserID(usernameInput);
        Parent root;
        Stage stage;
        User user = new User();
        user.setUserID(userID);
        user.setUsername(usernameInput);
        if (isValidPassword(userID, passwordInput)) {
            loginLog(user.getUsername(), "success");
            createReminderList();
            appointmentAlert();
            root = FXMLLoader.load(getClass().getClassLoader().getResource("MainScreen.fxml"));
            stage = (Stage) LoginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            loginLog(User.getUsername(), "failure");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(resourceBundle.getString("loginError"));
            alert.showAndWait();
        }

    }

    /**
     * This will log a line of text in the file login_activity.txt
     * in the form 2021-09-25 18:01:43 test success
     * Which indicates that the login for the user test was successful at the
     * given datatime
     * @param user the name of the user
     * @param status success - indicating the login was successful
     *               failure - indicating the login was unsuccessful
     */
    public void loginLog(String user, String status) {
        try {
            String fileName = "login_activity.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(Utility.getCurrentTimeInUTC() + " " + user + " " + status + " " + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param userID The userId of the user who is trying to login
     * @param password The password provided by the user
     * @return true if the user and password match in the users table,
     *          false otherwise
     * @throws SQLException
     */
    private boolean isValidPassword(int userID, String password) throws SQLException {
        Statement statement = ConnectDB.conn.createStatement();
        String sqlStatement = "SELECT Password FROM users WHERE User_ID ='" + userID + "'";
        ;
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            if (result.getString("Password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param username The username of the user
     * @return userID from users table corresponding to the given username
     * @throws SQLException
     */
    private int getUserID(String username) throws SQLException {
        int userID = -1;
        Statement statement = ConnectDB.conn.createStatement();
        String sqlStatement = "SELECT User_ID FROM users WHERE User_Name ='" + username + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            userID = result.getInt("User_ID");
        }
        return userID;
    }
}
