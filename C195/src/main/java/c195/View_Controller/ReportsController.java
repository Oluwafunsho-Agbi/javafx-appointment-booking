
package c195.View_Controller;

import c195.Model.Appointment;
import c195.Model.ContactAppointmentData;
import c195.Model.Reports;
import c195.util.ConnectDB;
import c195.util.DBUtility;
import c195.util.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This populates the TableView and handles the interaction with the
 * user for generating reports.
 */
public class ReportsController implements Initializable {
    @FXML
    private ComboBox<String> contactsComboBox;
    @FXML
    private TableView<Reports> ReportsByMonthTableView;
    @FXML
    private TableColumn<Reports, String> reportByMonthMonthColumn;
    @FXML
    private TableColumn<Reports, Integer> reportByMonthInPersonColumn;
    @FXML
    private TableColumn<Reports, Integer> reportByMonthChatColumn;
    @FXML
    private TableColumn<Reports, Integer> reportByMonthPhoneColumn;
    @FXML
    private TableColumn<Reports, Integer> reportByMonthVideoColumn;
    @FXML
    private TableView<Appointment> contactReportScheduleTableView;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleAppointmentIDColumn;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleTitleColumn;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleCustomerIDColumn;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleTypeColumn;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleStartColumn;
    @FXML
    private TableColumn<Appointment, String> contactReportScheduleEndColumn;
    @FXML
    private TableView<ContactAppointmentData> reportContactAppointmentsTableView;
    @FXML
    private TableColumn<ContactAppointmentData, String> ReportContactColumn;
    @FXML
    private TableColumn<ContactAppointmentData, Integer> ReportsContactTotalColumn;
    @FXML
    private TableColumn<ContactAppointmentData, Integer> ReportContactChatColumn;
    @FXML
    private TableColumn<ContactAppointmentData, Integer> ReportsContactPhoneColumn;
    @FXML
    private TableColumn<ContactAppointmentData, Integer> ReportsContactInpersonColumn;
    @FXML
    private TableColumn<ContactAppointmentData, Integer> ReportsContactVideoColumn;
    @FXML
    private Button ReportsMainMenuButton;
    private boolean isContactScheduleTab = false;
    private Parent root;
    private Stage stage;

    private ObservableList<Appointment> contactScheduleObservableList = FXCollections.observableArrayList();
    private ObservableList<Reports> typesByMonthOL = FXCollections.observableArrayList();
    private ObservableList<ContactAppointmentData> contactsAppointmentsOL = FXCollections.observableArrayList();
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");

    private int monthTypes[][] = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PropertyValueFactory<Appointment, String> apptIDFactory = new PropertyValueFactory<>("AppointmentID");
        PropertyValueFactory<Appointment, String> apptStartFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> apptEndFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, String> apptTitleFactory = new PropertyValueFactory<>("Title");
        PropertyValueFactory<Appointment, String> apptTypeFactory = new PropertyValueFactory<>("Type");
        PropertyValueFactory<Appointment, String> apptCustomerFactory = new PropertyValueFactory<>("CustomerID");

        contactReportScheduleAppointmentIDColumn.setCellValueFactory(apptIDFactory);
        contactReportScheduleStartColumn.setCellValueFactory(apptStartFactory);
        contactReportScheduleEndColumn.setCellValueFactory(apptEndFactory);
        contactReportScheduleTitleColumn.setCellValueFactory(apptTitleFactory);
        contactReportScheduleTypeColumn.setCellValueFactory(apptTypeFactory);

        contactReportScheduleCustomerIDColumn.setCellValueFactory(apptCustomerFactory);
        PropertyValueFactory<Reports, String> monthFactory = new PropertyValueFactory<>("Month");
        PropertyValueFactory<Reports, Integer> newFactory = new PropertyValueFactory<>("InPerson");
        PropertyValueFactory<Reports, Integer> consultationFactory = new PropertyValueFactory<>("Chat");
        PropertyValueFactory<Reports, Integer> followUpFactory = new PropertyValueFactory<>("Phone");
        PropertyValueFactory<Reports, Integer> closeFactory = new PropertyValueFactory<>("Video");

        reportByMonthMonthColumn.setCellValueFactory(monthFactory);
        reportByMonthInPersonColumn.setCellValueFactory(newFactory);
        reportByMonthChatColumn.setCellValueFactory(consultationFactory);
        reportByMonthPhoneColumn.setCellValueFactory(followUpFactory);
        reportByMonthVideoColumn.setCellValueFactory(closeFactory);

        PropertyValueFactory<ContactAppointmentData, String> contactFactory = new PropertyValueFactory<>("Contact");
        PropertyValueFactory<ContactAppointmentData, Integer> chatFactory = new PropertyValueFactory<>("Chat");
        PropertyValueFactory<ContactAppointmentData, Integer> phoneFactory = new PropertyValueFactory<>("Phone");
        PropertyValueFactory<ContactAppointmentData, Integer> inPersonFactory = new PropertyValueFactory<>("InPerson");
        PropertyValueFactory<ContactAppointmentData, Integer> videoFactory = new PropertyValueFactory<>("Video");
        PropertyValueFactory<ContactAppointmentData, Integer> totalFactory = new PropertyValueFactory<>("Total");

        ReportContactColumn.setCellValueFactory(contactFactory);
        ReportContactChatColumn.setCellValueFactory(chatFactory);
        ReportsContactPhoneColumn.setCellValueFactory(phoneFactory);
        ReportsContactInpersonColumn.setCellValueFactory(inPersonFactory);
        ReportsContactTotalColumn.setCellValueFactory(totalFactory);
        ReportsContactVideoColumn.setCellValueFactory(videoFactory);
        fillContactsComboBox();
        try {
            setReportsTypeByMonthTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            setReportContactsAppointmentsByType();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ReportsByMonthTableView.setItems(typesByMonthOL);
        contactReportScheduleTableView.setItems(contactScheduleObservableList);
        reportContactAppointmentsTableView.setItems(contactsAppointmentsOL);
        appendListenerToContactsComboBox();

    }

    /**
     * This adds a listener to the contacts combo box for the 2nd report
     * As the report generates the appointment list based on the contact selected
     * by the user.
     * A Lambda expression is used to provide the code to be executed when
     * This results in fewer lines of code then writing a separate function
     * or an Anonymous class to achieve the same results
     */
    private void appendListenerToContactsComboBox() {
        contactsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            String contactName = newValue;
            try {
                generateReportForContactSchedule(DBUtility.getContactID(contactName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This fills the contact names in the combo box
     */
    private void fillContactsComboBox() {
        List<String> contactList = DBUtility.getContacts();
        ObservableList<String> contactObservableList = FXCollections.observableArrayList();
        contactObservableList.addAll(contactList);
        contactsComboBox.setItems(contactObservableList);
    }

    @FXML
    private void ReportsMainMenuButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("MainScreen.fxml"));
        stage = (Stage) ReportsMainMenuButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This populates the data for the first report which
     * displays the Reports type by each month
     */
    private void setReportsTypeByMonthTable() {
        PreparedStatement ps;
        try {
            ps = ConnectDB.conn.prepareStatement(
                    "SELECT * "
                            + "FROM appointments");

            ResultSet rs = ps.executeQuery();
            typesByMonthOL.clear();
            while (rs.next()) {
                String type = rs.getString("Type");
                String dateTime = rs.getString("Start");
                Integer month = Utility.getMonth(dateTime);
                if (type.equals("In-person")) {
                    monthTypes[month][0]++;
                } else if (type.equals("Chat")) {
                    monthTypes[month][1]++;
                } else if (type.equals("Phone")) {
                    monthTypes[month][2]++;
                } else if (type.equals("Video")) {
                    monthTypes[month][3]++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 12; i++) {
            int inPerson = monthTypes[i][0];
            int chat = monthTypes[i][1];
            int phone = monthTypes[i][2];
            int video = monthTypes[i][3];
            String month = new DateFormatSymbols().getMonths()[i];
            typesByMonthOL.add(new Reports(month, inPerson, chat, phone, video));

        }
    }

    /**
     * This is the 3rd custom report it displays the number
     * of assignments assigned to each contacts
     */
    private void setReportContactsAppointmentsByType() {
        Map<String, ContactAppointmentData> map = new HashMap<>();
        PreparedStatement ps;
        try {
            ps = ConnectDB.conn.prepareStatement(
                    "SELECT * "
                            + "FROM appointments");

            ResultSet rs = ps.executeQuery();
            contactsAppointmentsOL.clear();
            while (rs.next()) {
                Integer contact = Integer.parseInt(rs.getString("Contact_ID"));
                String contactName = DBUtility.getContactName(contact);
                String type = rs.getString("Type");
                ContactAppointmentData contactAppointmentData = map.get(contactName);
                switch (type) {
                    case "In-person":
                        if (contactAppointmentData == null) {
                            contactAppointmentData = new ContactAppointmentData();
                            contactAppointmentData.setContact(contactName);
                            contactAppointmentData.setInPerson(1);
                            map.put(contactName, contactAppointmentData);
                        } else {
                            contactAppointmentData.setInPerson(contactAppointmentData.getInPerson() + 1);
                            map.put(contactName, contactAppointmentData);
                        }
                        break;
                    case "Chat":
                        if (contactAppointmentData == null) {
                            contactAppointmentData = new ContactAppointmentData();
                            contactAppointmentData.setContact(contactName);
                            contactAppointmentData.setChat(1);
                            map.put(contactName, contactAppointmentData);
                        } else {
                            contactAppointmentData.setInPerson(contactAppointmentData.getChat() + 1);
                            map.put(contactName, contactAppointmentData);
                        }
                        break;
                    case "Phone":
                        if (contactAppointmentData == null) {
                            contactAppointmentData = new ContactAppointmentData();
                            contactAppointmentData.setContact(contactName);
                            contactAppointmentData.setPhone(1);
                            map.put(contactName, contactAppointmentData);
                        } else {
                            contactAppointmentData.setPhone(contactAppointmentData.getPhone() + 1);
                            map.put(contactName, contactAppointmentData);
                        }
                        break;
                    case "Video":
                        if (contactAppointmentData == null) {
                            contactAppointmentData = new ContactAppointmentData();
                            contactAppointmentData.setContact(contactName);
                            contactAppointmentData.setVideo(1);
                            map.put(contactName, contactAppointmentData);
                        } else {
                            contactAppointmentData.setVideo(contactAppointmentData.getVideo() + 1);
                            map.put(contactName, contactAppointmentData);
                        }
                        break;
                }
                contactAppointmentData.incrementTotal();

            }
            for (Map.Entry<String, ContactAppointmentData> entry : map.entrySet()) {
                contactsAppointmentsOL.add(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This populates the appointments for the given ContactID
     * @param contactId The contact ID for which the appointments are to be fetched
     *
     */
    private void generateReportForContactSchedule(Integer contactId) {
        PreparedStatement ps;
        try {
            ps = ConnectDB.conn.prepareStatement(
                    "SELECT * "
                            + "FROM appointments, contacts "
                            + "WHERE appointments.Contact_ID = contacts.Contact_ID "
                            + " and appointments.Contact_ID = ? "
                            + "ORDER BY `start`");
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            contactScheduleObservableList.clear();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                int customerID = rs.getInt("Customer_ID");
                String description = rs.getString("Description");
                String contact = rs.getString("Contact_Name");
                String startUTC = rs.getString("Start").substring(0, 19);
                String endUTC = rs.getString("End").substring(0, 19);
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String user = rs.getString("Created_By");
                Integer userID = Integer.parseInt(rs.getString("User_ID"));
                String location = rs.getString("Location");
                contactScheduleObservableList.add(new Appointment(appointmentID, customerID, userID, title, description, location, contact, type, localStartDT, localEndDT, user));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This displays the combo box at the top containing the contacts
     * For the selected contacts it displays all the appointments
     * @param event The event is generated by the javafx once the
     *              2nd report tab is selected
     */
    public void contactSelectionChange(Event event) {
        isContactScheduleTab = !isContactScheduleTab;
        if (isContactScheduleTab)
            contactsComboBox.setVisible(true);
        else
            contactsComboBox.setVisible(false);
    }

}
