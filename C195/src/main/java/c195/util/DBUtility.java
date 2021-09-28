package c195.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class which contains
 * several methods that are commonly used by
 * other classes. The class specifically contains methods that have DB operation.
 * The main intent of this class is to avoid code duplicacy and code reuse
 * easily
 */
public class DBUtility {
    /**
     *
     * @param customerID The ID of the customer
     * @return true if the ID is present in the customers table
     *         otherwise will return false
     */
    public static boolean isValidCustomerId(Integer customerID) {
        int count = 0;
        try {
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement("select * from customers where Customer_ID=?");
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
            }

        } catch (SQLException sqlException) {
            return false;

        }
        if (count == 0)
            return false;
        return true;
    }

    /**
     *
     * @param customerID The ID of the customer
     * @return The name of the customer from the customers table which is
     *          stored in the Customer_Name column or null if no such customerID
     *          is present
     */
    public static String getCustomerName(Integer customerID) {
        try {
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement("select * from customers where Customer_ID=?");
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("Customer_Name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @return The list of all contact names in the contacts table
     */
    public static List<String> getContacts(){
        List<String> contactList=new ArrayList<>();
        try {
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement("select * from contacts");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contactList.add(resultSet.getString("Contact_Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     *
     * @param contactName The contact name of the person in the contacts table
     * @return The corresponding contact ID of the contact from the contacts table
     *         or null in case the contact name is not found.
     * @throws Exception
     */
    public static Integer getContactID(String contactName) throws Exception {
        PreparedStatement ps = ConnectDB.conn.prepareStatement("select * from contacts where Contact_Name=?");
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();
        Integer contactId = null;
        while (rs.next()) {
            contactId = rs.getInt("Contact_ID");
        }
        return contactId;
    }

    /**
     *
     * @param contactId The contact ID of the contact in the contacts table
     * @return The contact name from the contacts table corresponding to the
     *          provided contact ID. returns null in case the given contactID
     *          is not present
     * @throws SQLException
     */
    public static  String getContactName(Integer contactId) throws SQLException {
        String sqlStatement = "select * from contacts where Contact_ID=?";
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sqlStatement);
        ps.setInt(1, contactId);
        ResultSet result = ps.executeQuery();
        String contactName = null;
        while (result.next()) {
            contactName = result.getString("Contact_Name");
        }
        return contactName;
    }
}
