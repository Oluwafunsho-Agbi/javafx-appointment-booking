
package c195.View_Controller;

import c195.Model.Customer;
import c195.Model.User;
import c195.util.ConnectDB;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class populates the necessary elements to generate the
 * Customer GUI for the user to interact with using which the
 * user can add/update/delete the Customer details
 */
public class CustomerScreenController implements Initializable {
    @FXML
    private Label CustomerLabel;
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer, String> CustomerIDColumn;
    @FXML
    private TableColumn<Customer, String> CustomerNameColumn;
    @FXML
    private TableColumn<Customer, String> CustomerPhoneColumn;
    @FXML
    private TableColumn<Customer, String> CustomerAddressColumn;
    @FXML
    private TableColumn<Customer, String> CustomerPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> CustomerDivisionColumn;
    @FXML
    private TableColumn<Customer, String> CustomerCountryColumn;
    @FXML
    private TextField CustomerCustomerIDTextField;
    @FXML
    private ComboBox<String> CustomerDivisionComboBox;
    @FXML
    private ComboBox<String> CustomerCountryComboBox;
    @FXML
    private Button CustomerSaveButton;
    @FXML
    private Button CustomerCancelButton;
    @FXML
    private Button CustomerDeleteButton;
    @FXML
    private TextField CustomerCustomerNameTextField;
    @FXML
    private TextField CustomerAddressTextField;
    @FXML
    private TextField CustomerPostalCodeTextField;
    @FXML
    private TextField CustomerPhoneTextField;
    @FXML
    private Button CustomerBackButton;

    private Parent root;
    private Stage stage;
    private ObservableList<Customer> customerOL = FXCollections.observableArrayList();
    private ObservableList<String> divisionOptions = FXCollections.observableArrayList();
    private ObservableList<String> countryOptions = FXCollections.observableArrayList();
    private boolean customerUpdate = false;
    private boolean customerAdd = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PropertyValueFactory<Customer, String> custCustomerIDFactory = new PropertyValueFactory<>("customerID");
        PropertyValueFactory<Customer, String> custNameFactory = new PropertyValueFactory<>("customerName");
        PropertyValueFactory<Customer, String> custPhoneFactory = new PropertyValueFactory<>("phone"); //String value "CustomerPhone" calls getCustomerPhone method
        PropertyValueFactory<Customer, String> custCountryFactory = new PropertyValueFactory<>("country");
        PropertyValueFactory<Customer, String> custDivisionFactory = new PropertyValueFactory<>("division");
        PropertyValueFactory<Customer, String> custAddressFactory = new PropertyValueFactory<>("address");
        PropertyValueFactory<Customer, String> custPostalCodeFactory = new PropertyValueFactory<>("postalCode");

        CustomerIDColumn.setCellValueFactory(custCustomerIDFactory);
        CustomerNameColumn.setCellValueFactory(custNameFactory);
        CustomerPhoneColumn.setCellValueFactory(custPhoneFactory);
        CustomerCountryColumn.setCellValueFactory(custCountryFactory);
        CustomerDivisionColumn.setCellValueFactory(custDivisionFactory);
        CustomerAddressColumn.setCellValueFactory(custAddressFactory);
        CustomerPostalCodeColumn.setCellValueFactory(custPostalCodeFactory);
        CustomerCustomerIDTextField.setText("Auto Generated");
        disableCustomerFields();
        try {
            updateCustomerTableView();
            try {
                fillCountryComboBox();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CustomerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        customerListener(newValue);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
        addListenerToCustomerCountryComboBox();

    }

    /**
     * This adds the listener to the country combo box.
     * When a country is changed the division combo box is automatically
     * populated with the divisions for that country.
     * The lambda expression is used here to add a listener
     */
    private void addListenerToCustomerCountryComboBox() {
        CustomerCountryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            String country = CustomerCountryComboBox.getValue();
            try {
                if (oldValue != null)
                    fillDivisionComboBox(getCountryId(country), new Data(oldValue, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This function will be called anytime an update has been made to
     * a customer so that the TableView could be updated with the changes
     * @throws SQLException
     */
    public void updateCustomerTableView() throws SQLException {
        customerOL.clear();
        Statement stmt = ConnectDB.conn.createStatement();
        String sqlStatement = "select * from customers as c,first_level_divisions as d, countries  where c.Division_ID=d.Division_ID and countries.Country_ID=d.Country_ID";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
            Customer cust = new Customer();
            cust.setCustomerID(result.getInt("Customer_ID"));
            cust.setCustomerName(result.getString("Customer_Name"));
            cust.setPhone(result.getString("Phone"));
            cust.setAddress(result.getString("Address"));
            cust.setPostalCode(result.getString("Postal_Code"));
            cust.setDivision(result.getString("Division"));
            cust.setCountry(result.getString("Country"));
            customerOL.addAll(cust);
        }
        CustomerTable.setItems(customerOL);
    }

    public void clearTextFields() {
        CustomerLabel.setText("");
        CustomerCustomerIDTextField.setText("");
        CustomerCustomerNameTextField.setText("");
        CustomerAddressTextField.setText("");
        CustomerDivisionComboBox.setValue("");
        CustomerCountryComboBox.setValue("");
        CustomerPostalCodeTextField.setText("");
        CustomerPhoneTextField.setText("");
    }

    /**
     *
     * @param country The country name
     * @return The Country Id from the countries table corresponding to the given
     *         country name
     * @throws Exception
     */
    public String getCountryId(String country) throws Exception {
        String sqlStatement = "select Country_ID from countries where Country = ?";
        PreparedStatement pst = ConnectDB.conn.prepareStatement(sqlStatement);
        pst.setString(1, country);
        ResultSet result = pst.executeQuery();
        String countryID = null;
        while (result.next()) {
            countryID = result.getString("Country_ID");
        }
        pst.close();
        result.close();
        return countryID;
    }

    /**
     *
     * @param countryID The country ID of a country
     * @param data This stores the old country value and the old division values
     *             when the user changed them from the combo boxes
     * @throws Exception
     */
    public void fillDivisionComboBox(String countryID, Data data) throws Exception {
        String sqlStatement = "SELECT Division FROM first_level_divisions where Country_ID=?";
        PreparedStatement pst = ConnectDB.conn.prepareStatement(sqlStatement);
        pst.setString(1, countryID);
        ResultSet result = pst.executeQuery();
        divisionOptions.clear();
        while (result.next()) {
            Customer cust = new Customer();
            cust.setDivision(result.getString("Division"));
            divisionOptions.add(cust.getDivision());
        }
        CustomerDivisionComboBox.setItems(divisionOptions);
        if (data.oldCountryValue != null && !divisionOptions.isEmpty())
            CustomerDivisionComboBox.setValue(divisionOptions.get(0));
        else if (data.divisionValue != null)
            CustomerDivisionComboBox.setValue(data.divisionValue);
        pst.close();
        result.close();
    }

    /**
     * This populates the country combo box with country names from
     * the countries table
     * @throws Exception
     */
    public void fillCountryComboBox() throws  Exception {
        Statement stmt = ConnectDB.conn.createStatement();
        String sqlStatement = "SELECT Country FROM countries";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
            Customer cust = new Customer();
            cust.setCountry(result.getString("Country"));
            countryOptions.add(cust.getCountry());
            CustomerCountryComboBox.setItems(countryOptions);
        }
        stmt.close();
        result.close();
    }

    @FXML
    private void CustomerSaveButtonHandler(ActionEvent event) throws Exception {
        if (CustomerCustomerNameTextField.getText() != null && customerAdd || customerUpdate) {
            if (validCustomer()) {
                if (customerAdd) {
                    saveCustomer();
                    clearTextFields();
                    updateCustomerTableView();
                } else if (customerUpdate) {
                    updateCustomer();
                    clearTextFields();
                    updateCustomerTableView();
                }
            }
        }
    }

    private void saveCustomer() throws Exception {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, CustomerCustomerNameTextField.getText());
            ps.setString(2, CustomerAddressTextField.getText());
            ps.setString(3, CustomerPostalCodeTextField.getText());
            ps.setString(4, CustomerPhoneTextField.getText());
            ps.setString(5, User.getUsername());
            ps.setString(6, User.getUsername());
            ps.setInt(7, getDivisionID(CustomerDivisionComboBox.getValue()));
            ps.executeUpdate();
            ps.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearTextFields();
        disableCustomerFields();
        updateCustomerTableView();
        customerAdd = false;
        customerUpdate = false;
    }

    private void deleteCustomer(Customer customer) throws Exception {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("DELETE from customers WHERE customers.Customer_ID = ?");
            ps.setInt(1, customer.getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearTextFields();
        disableCustomerFields();
        updateCustomerTableView();
    }

    private void updateCustomer() throws Exception {
        try {
            PreparedStatement ps;
            ps = ConnectDB.conn.prepareStatement("UPDATE customers "
                    + "SET Address = ?, Division_ID = ?, Postal_Code = ?, Phone = ?,  Last_Updated_By = ? " +
                    " where customers.Customer_ID=?");
            ps.setString(1, CustomerAddressTextField.getText());
            ps.setInt(2, getDivisionID(CustomerDivisionComboBox.getValue()));
            ps.setString(3, CustomerPostalCodeTextField.getText());
            ps.setString(4, CustomerPhoneTextField.getText());
            ps.setString(5, User.getUsername());
            ps.setString(6, CustomerCustomerIDTextField.getText());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        clearTextFields();
        disableCustomerFields();
        updateCustomerTableView();
        customerAdd = false;
        customerUpdate = false;
    }

    private int getDivisionID(String division) throws Exception {
        String sqlStatement = "select Division_ID from first_level_divisions where Division= ?";
        PreparedStatement pst = ConnectDB.conn.prepareStatement(sqlStatement);
        pst.setString(1, division);
        ResultSet result = pst.executeQuery();
        Integer divisionID = null;
        while (result.next()) {
            divisionID = result.getInt("Division_ID");
        }
        pst.close();
        result.close();
        return divisionID;
    }

    /**
     * This validates the information entered by the user
     * so that none of the entries are null or invalid
     * @return
     */
    private boolean validCustomer() {
        String customerName = CustomerCustomerNameTextField.getText().trim();
        String address = CustomerAddressTextField.getText().trim();
        String country = CustomerCountryComboBox.getValue().trim();
        String postalCode = CustomerPostalCodeTextField.getText().trim();
        String phone = CustomerPhoneTextField.getText().trim();
        String errorMessage = "";

        if (customerName == null || customerName.length() == 0) {
            errorMessage += "Please enter Customer Name.\n";
        }
        if (address == null || address.length() == 0) {
            errorMessage += "Please enter an address.\n";
        }
        if (country == null) {
            errorMessage += "Please Select a Country.\n";
        }
        if (postalCode == null || postalCode.length() == 0) {
            errorMessage += "Please enter a valid Postal Code.\n";
        }
        if (phone == null || phone.length() == 0) {
            errorMessage += "Please enter a Phone Number (including Area Code).";
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

    public void disableCustomerFields() {
        CustomerCustomerIDTextField.setDisable(true);
        CustomerCustomerNameTextField.setDisable(true);
        CustomerAddressTextField.setDisable(true);
        CustomerDivisionComboBox.setDisable(true);
        CustomerCountryComboBox.setDisable(true);
        CustomerPostalCodeTextField.setDisable(true);
        CustomerPhoneTextField.setDisable(true);
        CustomerSaveButton.setDisable(true);
        CustomerCancelButton.setDisable(true);
        CustomerDeleteButton.setDisable(true);
    }

    public void enableCustomerFields() {
        CustomerCustomerIDTextField.setDisable(false);
        CustomerCustomerIDTextField.setEditable(false);
        CustomerCustomerNameTextField.setDisable(false);
        CustomerAddressTextField.setDisable(false);
        CustomerDivisionComboBox.setDisable(false);
        CustomerCountryComboBox.setDisable(false);
        CustomerPostalCodeTextField.setDisable(false);
        CustomerPhoneTextField.setDisable(false);
        CustomerSaveButton.setDisable(false);
        CustomerCancelButton.setDisable(false);
        CustomerDeleteButton.setDisable(false);
    }

    public void customerListener(Customer customer) throws SQLException, Exception {
        int custId = customer.getCustomerID();
        CustomerLabel.setText("Update");
        customerUpdate = true;
        customerAdd = false;
        enableCustomerFields();
        String sqlStatement = "select * from customers as c,first_level_divisions as d, countries  where c.Customer_ID=? and c.Division_ID=d.Division_ID and countries.Country_ID=d.Country_ID";
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sqlStatement);
        ps.setInt(1, custId);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            CustomerCustomerIDTextField.setText(Integer.toString(result.getInt("Customer_ID")));
            CustomerCustomerNameTextField.setText(result.getString("Customer_Name"));
            CustomerAddressTextField.setText(result.getString("Address"));
            String country = result.getString("Country");
            fillDivisionComboBox(getCountryId(country), new Data(null, result.getString("Division")));
            CustomerDivisionComboBox.setValue(result.getString("Division"));
            CustomerCountryComboBox.setValue(result.getString("Country"));
            CustomerPostalCodeTextField.setText(result.getString("Postal_Code"));
            CustomerPhoneTextField.setText(result.getString("Phone"));
        }
    }

    @FXML
    private void CustomerCancelButtonHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            customerUpdate = false;
            customerAdd = false;
            clearTextFields();
            disableCustomerFields();
        }
    }

    @FXML
    private void CustomerAddButtonHandler(ActionEvent event) throws SQLException {
        clearTextFields();
        CustomerLabel.setText("Add");
        CustomerCustomerIDTextField.setText("Auto Generated");
        customerAdd = true;
        customerUpdate = false;
        enableCustomerFields();
    }

    @FXML
    private void CustomerDeleteButtonHandler(ActionEvent event
    ) throws Exception {
        if (CustomerTable.getSelectionModel().getSelectedItem() != null) {
            Customer cust = CustomerTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Are you sure you want to DELETE customerID - " + cust.getCustomerID() + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteAppointments(cust);
                deleteCustomer(cust);
                clearTextFields();
                disableCustomerFields();
                updateCustomerTableView();
            }
        }
    }

    private void deleteAppointments(Customer customer) {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("DELETE from appointments WHERE Customer_ID = ?");
            ps.setInt(1, customer.getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void CustomerBackButtonHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("MainScreen.fxml"));
        stage = (Stage) CustomerBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private class Data {
        String oldCountryValue;
        String divisionValue;

        public Data(String _oldCountryValue, String _divisionValue) {
            oldCountryValue = _oldCountryValue;
            divisionValue = _divisionValue;
        }
    }
}
