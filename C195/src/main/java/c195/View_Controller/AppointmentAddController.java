
package c195.View_Controller;

import c195.Model.Appointment;
import c195.Model.Customer;
import c195.Model.User;
import c195.util.ConnectDB;
import c195.util.DBUtility;
import c195.util.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static c195.util.DBUtility.getContactName;

/**
 * This class sets up the TableView and various handlers
 * For add/update the existing appointments
 * It provides the necessary GUI controls to ease this process
 */
public class AppointmentAddController implements Initializable {
    @FXML
    private ToggleGroup radioToggleGroup;
    @FXML
    private Label AppointmentLabel;
    @FXML
    private TableView<Appointment> AppointmentTable;
    @FXML
    private TextField AppointmentTitleTextField;
    @FXML
    private TextField AppointmentDescriptionTextField;
    @FXML
    private DatePicker AppointmentDatePicker;
    @FXML
    private TextField AppointmentIdTextField;
    @FXML
    private ComboBox<String> AppointmentTypeComboBox;
    @FXML
    private ComboBox<String> AppointmentContactComboBox;
    @FXML
    private ComboBox<String> AppointmentLocationComboBox;
    @FXML
    private ComboBox<String> AppointmentStartComboBox;
    @FXML
    private ComboBox<String> AppointmentEndComboBox;
    @FXML
    private Button AppointmentSaveButton;
    @FXML
    private Button AppointmentDeleteButton;
    @FXML
    private Button AppointmentAddButton;
    @FXML
    private TableColumn<Appointment, String> TableColumnAppointmentID;
    @FXML
    private TableColumn<Appointment, String> TableColumnTitle;
    @FXML
    private TableColumn<Appointment, String> TableColumnDescription;
    @FXML
    private TableColumn<Appointment, String> TableColumnLocation;
    @FXML
    private TableColumn<Appointment, String> TableColumnContactName;
    @FXML
    private TableColumn<Appointment, String> TableColumnType;
    @FXML
    private TableColumn<Appointment, String> TableColumnStart;
    @FXML
    private TableColumn<Appointment, String> TableColumnEnd;
    @FXML
    private TableColumn<Appointment, String> TableColumnCustomerID;
    @FXML
    private TableColumn<Appointment, String> TableColumnUserID;
    @FXML
    private Label AppointmentTableLabel;
    @FXML
    private TextField AppointmentCustomerTextField;
    @FXML
    private Button AppointmentBackButton;

    private Parent root;
    private Stage stage;
    private Customer selectedCustomer = new Customer();
    private Appointment selectedAppointment;
    private boolean appointmentUpdate = false;
    private boolean appointmentAdd = false;
    private final ZoneId localZoneID = ZoneId.systemDefault();
    ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PropertyValueFactory<Appointment, String> appointmentIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, String> titleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, String> descriptionFactory = new PropertyValueFactory<>("Description");
        PropertyValueFactory<Appointment, String> locationFactory = new PropertyValueFactory<>("Location");
        PropertyValueFactory<Appointment, String> contactNameFactory = new PropertyValueFactory<>("ContactName");
        PropertyValueFactory<Appointment, String> typeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, String> startFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> endFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, String> customerIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<Appointment, String> UserIDFactory = new PropertyValueFactory<>("UserID");

        TableColumnAppointmentID.setCellValueFactory(appointmentIDFactory);
        TableColumnTitle.setCellValueFactory(titleFactory);
        TableColumnDescription.setCellValueFactory(descriptionFactory);
        TableColumnLocation.setCellValueFactory(locationFactory);
        TableColumnContactName.setCellValueFactory(contactNameFactory);
        TableColumnType.setCellValueFactory(typeFactory);
        TableColumnStart.setCellValueFactory(startFactory);
        TableColumnEnd.setCellValueFactory(endFactory);
        TableColumnCustomerID.setCellValueFactory(customerIDFactory);
        TableColumnUserID.setCellValueFactory(UserIDFactory);
        AppointmentCustomerTextField.setEditable(false);
        try {
            populateAppointmentsTableView("All");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            fillContactList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillLocationList();
        fillStartTimesList();
        AppointmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        if (newValue != null) {
                            appointmentListener(newValue);
                            AppointmentAddButton.setDisable(true);
                            AppointmentDeleteButton.setDisable(false);
                            AppointmentSaveButton.setDisable(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        AppointmentDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
            }
        });

        disableAppointmentFields();
        AppointmentDeleteButton.setDisable(true);
        AppointmentSaveButton.setDisable(true);
        radioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton radioButton = (RadioButton) newValue;
                try {
                    populateAppointmentsTableView(radioButton.getText());
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @param radioButton This can be All, Current Week or Current Month
     *                    It populates the view with the appointments in the
     *                    selected period
     * @throws SQLException
     */
    public void populateAppointmentsTableView(String radioButton) throws SQLException {
        String sqlStatement;
        PreparedStatement ps = null;
        String startDate = null;
        String endDate = null;
        switch (radioButton) {
            case "All":
                sqlStatement = "select * from appointments";
                ps = ConnectDB.conn.prepareStatement(sqlStatement);
                break;
            case "Current Week":
                sqlStatement = "select * from appointments where Date(Start)>= ? and Date(Start)<=?";
                ps = ConnectDB.conn.prepareStatement(sqlStatement);
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().plusWeeks(1).toString();
                ps.setString(1, startDate);
                ps.setString(2, endDate);

                break;
            case "Current Month":
                sqlStatement = "select * from appointments where Date(Start)>=? and Date(Start)<=?";
                ps = ConnectDB.conn.prepareStatement(sqlStatement);
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().plusMonths(1).toString();
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                break;
        }
        ResultSet result = ps.executeQuery();
        appointmentObservableList.clear();
        while (result.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentID(result.getInt("Appointment_ID"));
            appointment.setTitle(result.getString("Title"));
            appointment.setDescription(result.getString("Description"));
            appointment.setLocation(result.getString("Location"));
            appointment.setType(result.getString("Type"));
            String rsStartDate = result.getString("Start");
            String rsEndDate = result.getString("End");
            appointment.setStart(convertToLocal(rsStartDate));
            appointment.setEnd(convertToLocal(rsEndDate));
            appointment.setCustomerID(Integer.parseInt(result.getString("Customer_ID")));
            appointment.setUserID(Integer.parseInt(result.getString("User_ID")));
            appointment.setContactName(getContactName(Integer.parseInt(result.getString("Contact_ID"))));
            appointmentObservableList.addAll(appointment);
        }
        AppointmentTable.setItems(appointmentObservableList);

    }

    /**
     *
     * @param dateTime A dataTime string in the format yyyy-MM-dd HH:mm:ss
     * @return A dateTime string in the format yyyy-MM-dd HH:mm:ss after converting
     *         the provided dataTime String from UTC to the users local time zone
     */
    public String convertToLocal(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZoneID);
        return zonedDateTime.toLocalDateTime().format(dateTimeFormatter);
    }

    /**
     * This disables the Fields in the appointments so that
     * user cannot do any modification to it.
     * This is done when no record is selected by the user to
     * update appointment so these elements are not needed
     */
    public void disableAppointmentFields() {
        AppointmentIdTextField.setDisable(true);
        AppointmentCustomerTextField.setDisable(true);
        AppointmentTitleTextField.setDisable(true);
        AppointmentDescriptionTextField.setDisable(true);
        AppointmentTypeComboBox.setDisable(true);
        AppointmentContactComboBox.setDisable(true);
        AppointmentLocationComboBox.setDisable(true);
        AppointmentDatePicker.setDisable(true);
        AppointmentStartComboBox.setDisable(true);
        AppointmentEndComboBox.setDisable(true);
    }

    /**
     * This will populate the appointment selected by the user in the table view
     * to the table on the right side. Which can be updated thereafter
     * @param appointment The appointment selected by the user from the table view
     * @throws SQLException
     * @throws Exception
     */
    public void appointmentListener(Appointment appointment) throws SQLException, Exception {
        enableAppointmentFields();
        AppointmentTableLabel.setText("Update");
        appointmentUpdate = true;
        appointmentAdd = false;
        String sqlStatement = "select * from appointments where Appointment_ID=?";
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sqlStatement);
        ps.setInt(1, appointment.getAppointmentID());
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            AppointmentIdTextField.setText(String.valueOf(appointment.getAppointmentID()));
            AppointmentTitleTextField.setText(appointment.getTitle());
            AppointmentDescriptionTextField.setText(appointment.getDescription());
            String startDateTime = appointment.getStart();
            String endDateTime = appointment.getEnd();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startLocalDateTime = LocalDateTime.parse(startDateTime, format);
            LocalDateTime endLocalDateTime = LocalDateTime.parse(endDateTime, format);
            AppointmentDatePicker.setValue(LocalDate.of(startLocalDateTime.getYear(), startLocalDateTime.getMonth(), startLocalDateTime.getDayOfMonth()));
            AppointmentTypeComboBox.setValue(appointment.getType());
            AppointmentContactComboBox.setValue(appointment.getContactName());
            AppointmentLocationComboBox.setValue(appointment.getLocation());
            AppointmentStartComboBox.setValue(startLocalDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            AppointmentEndComboBox.setValue(endLocalDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            AppointmentCustomerTextField.setText(String.valueOf(appointment.getCustomerID()));
        }
    }

    /**
     * This enables the appointment fields in the table on the right side.
     * so that user can add/update
     */
    private void enableAppointmentFields() {
        AppointmentIdTextField.setDisable(false);
        AppointmentIdTextField.setEditable(false);
        AppointmentCustomerTextField.setDisable(false);
        AppointmentCustomerTextField.setEditable(true);
        AppointmentDescriptionTextField.setDisable(false);
        AppointmentDatePicker.setDisable(false);
        AppointmentTypeComboBox.setDisable(false);
        AppointmentContactComboBox.setDisable(false);
        AppointmentLocationComboBox.setDisable(false);
        AppointmentStartComboBox.setDisable(false);
        AppointmentEndComboBox.setDisable(false);
        AppointmentCustomerTextField.setDisable(false);
        AppointmentTitleTextField.setDisable(false);

    }

    private void updateAppointmentFields() {
        AppointmentLabel.setText("Update Appointment");
        selectedCustomer.setCustomerID(selectedAppointment.getCustomerID());
        String startLocal = selectedAppointment.getStart();
        String endLocal = selectedAppointment.getEnd();
        LocalDateTime localDateTimeStart = LocalDateTime.parse(startLocal, datetimeDTF);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(endLocal, datetimeDTF);
        LocalDate localDate = localDateTimeStart.toLocalDate();
        AppointmentCustomerTextField.setText(selectedAppointment.getCustomerName());
        AppointmentTitleTextField.setText(selectedAppointment.getTitle());
        AppointmentDescriptionTextField.setText(selectedAppointment.getDescription());
        AppointmentTypeComboBox.setValue(selectedAppointment.getType());
        AppointmentContactComboBox.setValue(selectedAppointment.getContactName());
        AppointmentLocationComboBox.setValue(selectedAppointment.getLocation());
        AppointmentDatePicker.setValue(localDate);
        AppointmentStartComboBox.getSelectionModel().select(localDateTimeStart.toLocalTime().format(timeDTF));
        AppointmentEndComboBox.getSelectionModel().select(localDateTimeEnd.toLocalTime().format(timeDTF));

    }

    /**
     * Once the user add/updates an appointment by using
     * the right pane the fields will be cleared and reset.
     */
    private void clearAppointmentFields() {
        AppointmentCustomerTextField.setText("");
        AppointmentTitleTextField.setText("");
        AppointmentDescriptionTextField.setText("");
        AppointmentTypeComboBox.getSelectionModel().clearSelection();
        AppointmentContactComboBox.getSelectionModel().clearSelection();
        AppointmentLocationComboBox.getSelectionModel().clearSelection();
        AppointmentDatePicker.setValue(null);
        AppointmentStartComboBox.getSelectionModel().clearSelection();
        AppointmentEndComboBox.getSelectionModel().clearSelection();
    }

    /**
     * This populates the start times so that user
     * can select the start time of the appointment
     */
    private void fillStartTimesList() {
        LocalTime time = LocalTime.of(0, 0, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(60);
        } while (!time.equals(LocalTime.of(0, 0, 0)));
        AppointmentStartComboBox.setItems(startTimes);
        AppointmentEndComboBox.setItems(endTimes);
        AppointmentStartComboBox.getSelectionModel().select(LocalTime.of(8, 0, 0).format(timeDTF));
        AppointmentEndComboBox.getSelectionModel().select(LocalTime.of(9, 0, 0).format(timeDTF));
    }

    /**
     * This populates all the contact names in a combobox
     * so that the user can select a particular contact for this particular appointment
     * @throws Exception
     */
    private void fillContactList() throws Exception {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        PreparedStatement ps = ConnectDB.conn.prepareStatement("SELECT * from contacts");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            contactList.add(result.getString("Contact_Name"));
        }
        AppointmentContactComboBox.setItems(contactList);
    }

    /**
     * populates the list of locations
     */
    private void fillLocationList() {
        ObservableList<String> locationList = FXCollections.observableArrayList();
        locationList.addAll("Phoenix", "New York", "Tampa", "Orlando", "Dallas", "London", "Liverpool");
        AppointmentLocationComboBox.setItems(locationList);
    }

    /**
     * This will validate each element of the appointment
     * It will check
     * 1. If the appointment overlaps with an existing appointment
     * 2. If any of the fields are not set
     * 3. If the appointment falls out of the business hours 8 am ET to 10 pm ET
     * @return true if the fields entered by the user are valid,
     *          false otherwise
     */
    private boolean isAppointmentValid() {
        String appointmentID = AppointmentIdTextField.getText();
        String customerID = AppointmentCustomerTextField.getText();
        String title = AppointmentTitleTextField.getText();
        String description = AppointmentDescriptionTextField.getText();
        String type = AppointmentTypeComboBox.getValue();
        String contact = AppointmentContactComboBox.getValue();
        String location = AppointmentLocationComboBox.getValue();
        LocalDate localDate = AppointmentDatePicker.getValue();
        String errorMessage = "";
        LocalTime startTime = null;
        LocalTime endTime = null;
        ZonedDateTime startUTC = null;
        ZonedDateTime endUTC = null;
        LocalDateTime startDT = null;
        LocalDateTime endDT = null;
        try {
            startTime = LocalTime.parse(AppointmentStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
            endTime = LocalTime.parse(AppointmentEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);
            startDT = LocalDateTime.of(localDate, startTime);
            endDT = LocalDateTime.of(localDate, endTime);
            startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
            endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        } catch (Exception e) {
            errorMessage = "Invalid Start or End Time or Date\n";
        }
        LocalDateTime startLocalDateTimeInET = Utility.convertZoneLocalDateTime(startDT, localZoneID, ZoneId.of("US/Eastern"));
        LocalDateTime endLocalDateTimeInET = Utility.convertZoneLocalDateTime(endDT, localZoneID, ZoneId.of("US/Eastern"));

        if (startLocalDateTimeInET.getHour() < 8 || endLocalDateTimeInET.getHour() >= 22) {
            errorMessage += "Timing must be between business hours 8 am ET to 10 pm ET\n";
        }
        if (!Utility.isNumeric(customerID) || !DBUtility.isValidCustomerId(Integer.parseInt(customerID))) {
            errorMessage += "You must enter a valid customer ID.\n";
        }
        if (title == null || title.length() == 0) {
            errorMessage += "You must enter an Appointment title.\n";
        }
        if (description == null || description.length() == 0) {
            errorMessage += "You must enter an appointment description.\n";
        }
        if (type == null || type.length() == 0) {
            errorMessage += "You must select an Appointment type.\n";
        }
        if (contact == null || contact.length() == 0) {
            errorMessage += "You must select an Appointment contact.\n";
        }
        if (location == null || location.length() == 0) {
            errorMessage += "You must select an Appointment location.\n";
        }
        if (startUTC == null) {
            errorMessage += "You must select a Start time";
        }
        if (endUTC == null) {
            errorMessage += "You must select an End time.\n";
        } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)) {
            errorMessage += "End time must be after Start time.\n";
        } else {

            if (hasConflict(startUTC, endUTC,appointmentID)) {
                errorMessage += "Appointment times conflict with Consultant's other appointments.\n";
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

    }

    /**
     *
     * @param newStart The newStartDate of the appointment
     * @param newEnd The newEndDate of the appointment
     * @param appointmentID This will be null in case of new appointment otherwise will
     *                      have the actual appointmentID in case of update
     * @return true if the appointment doesn't overlap with any other
     *         existing appointment, false otherwise
     */
    private boolean hasConflict(ZonedDateTime newStart, ZonedDateTime newEnd, String appointmentID) {
        try {
            PreparedStatement pst = ConnectDB.conn.prepareStatement("select * from appointments" +
                    " where ((End > ? and Start < ? and ? > End) or " +
                    "(start < ? and ? < End) or " +
                    "(? < Start and ? > Start and ? < End) or " +
                    "(? < Start and ? > End) or " +
                    "(Start=? and End=?) or " +
                    "(Start = ? and (? < End or End < ?)) or " +
                    "((? < Start or ? > Start) and End = ?)) and Appointment_ID!=?");
            pst.setTimestamp(1, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(2, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(3, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(4, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(5, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(6, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(7, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(8, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(9, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(10, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(11, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(12, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(13, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(14, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(15, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(16, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(17, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(18, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setString(19, appointmentID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves the appointment fields entered by the user
     * into the database
     * @throws Exception
     */
    private void saveAppointment() throws Exception {
        LocalDate localDate = AppointmentDatePicker.getValue();
        LocalTime localStartTime = LocalTime.parse(AppointmentStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime localEndTime = LocalTime.parse(AppointmentEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalDateTime startDT = LocalDateTime.of(localDate, localStartTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, localEndTime);
        ZonedDateTime startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("INSERT INTO appointments (Customer_ID, User_ID, Title, Description, Location, Contact_ID, Type, Start, End, Created_By, Last_Updated_By) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(AppointmentCustomerTextField.getText()));
            ps.setInt(2, User.getUserID());
            ps.setString(3, AppointmentTitleTextField.getText());
            ps.setString(4, AppointmentDescriptionTextField.getText());
            ps.setString(5, AppointmentLocationComboBox.getValue().trim());
            ps.setInt(6, DBUtility.getContactID(AppointmentContactComboBox.getValue().trim()));
            ps.setString(7, AppointmentTypeComboBox.getValue().trim());
            ps.setTimestamp(8, sqlStartTS);
            ps.setTimestamp(9, sqlEndTS);
            ps.setString(10, User.getUsername());
            ps.setString(11, User.getUsername());
            ps.executeUpdate();
            ps.getGeneratedKeys();
            clearAppointmentFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an exsiting appointment with the changes made by user
     * @throws Exception
     */
    private void updateAppointment() throws Exception {
        LocalDate localDate = AppointmentDatePicker.getValue();
        LocalTime localStartTime = LocalTime.parse(AppointmentStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime localEndTime = LocalTime.parse(AppointmentEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalDateTime startDT = LocalDateTime.of(localDate, localStartTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, localEndTime);
        ZonedDateTime startUTC = startDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("UPDATE appointments "
                    + "SET Customer_ID = ?, User_ID = ?, Title = ?, Description = ?, "
                    + "Location = ?, Contact_ID = ?, Type = ?, Start = ?, End = ?, Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ? "
                    + "WHERE Appointment_ID = ?");
            if (selectedCustomer.getCustomerID() <= 0) {
                selectedCustomer.setCustomerID(AppointmentTable.getSelectionModel().getSelectedItem().getCustomerID());
            }
            ps.setInt(1, selectedCustomer.getCustomerID());
            ps.setInt(2, User.getUserID());
            ps.setString(3, AppointmentTitleTextField.getText());
            ps.setString(4, AppointmentDescriptionTextField.getText());
            ps.setString(5, AppointmentLocationComboBox.getSelectionModel().getSelectedItem());
            ps.setInt(6, DBUtility.getContactID(AppointmentContactComboBox.getSelectionModel().getSelectedItem()));
            ps.setString(7, AppointmentTypeComboBox.getSelectionModel().getSelectedItem());
            ps.setTimestamp(8, sqlStartTS);
            ps.setTimestamp(9, sqlEndTS);
            ps.setString(10, User.getUsername());
            ps.setString(11, AppointmentIdTextField.getText());
            ps.executeUpdate();
            clearAppointmentFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void AppointmentCancelButtonHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("AppointmentsMain.fxml"));
            stage = (Stage) AppointmentSaveButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    /**
     * This will save the appointment in the database
     * @param event The even is generated by the JavaFX when the Save Button is pressed
     * @throws Exception
     */
    @FXML
    private void AppointmentSaveButtonHandler(ActionEvent event) throws Exception {
        RadioButton radioButton = (RadioButton) radioToggleGroup.getSelectedToggle();
        if (isAppointmentValid() && (appointmentUpdate || appointmentAdd)) {
            if (appointmentAdd) {
                saveAppointment();
                clearAppointmentFields();
                populateAppointmentsTableView(radioButton.getText());
                disableAppointmentFields();
                AppointmentAddButton.setDisable(false);

            } else if (appointmentUpdate) {
                updateAppointment();
                clearAppointmentFields();
                populateAppointmentsTableView(radioButton.getText());
                disableAppointmentFields();
            }
            AppointmentSaveButton.setDisable(true);
        }
    }

    /**
     * This will take the user to the previous screen which is the Main Screen
     * @param event The event is generated by the javaFX when Back button is pressed
     * @throws IOException
     */
    @FXML
    private void AppointmentBackButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("MainScreen.fxml"));
        stage = (Stage) AppointmentBackButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public void AppointmentAddButtonHandler(ActionEvent actionEvent) {
        clearAppointmentFields();
        AppointmentLabel.setText("Add");
        AppointmentIdTextField.setText("Auto Generated");
        appointmentAdd = true;
        appointmentUpdate = false;
        enableAppointmentFields();
        AppointmentAddButton.setDisable(true);
        AppointmentDeleteButton.setDisable(true);
        AppointmentSaveButton.setDisable(false);
    }

    /**
     * This will delete the selected appointment by the user
     * @param actionEvent The event is generated when the delete button is pressed
     * @throws Exception
     */
    public void AppointmentDeleteButtonHandler(ActionEvent actionEvent) throws Exception {
        if (AppointmentTable.getSelectionModel().getSelectedItem() != null) {
            Appointment appointment = AppointmentTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Are you sure you want to DELETE appointment ID - " + appointment.getAppointmentID() + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteAppointment(appointment);
                clearAppointmentFields();
                populateAppointmentsTableView("All");
                disableAppointmentFields();
            }
        }
        AppointmentAddButton.setDisable(false);
        AppointmentDeleteButton.setDisable(true);
    }

    /**
     * This will delete the appointment fromt he appointments table
     * @param appointment The appointment to be deleted from the database
     * @throws Exception
     */
    private void deleteAppointment(Appointment appointment) throws Exception {
        Appointment appt = appointment;
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ? ");
            ps.setInt(1, appt.getAppointmentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
