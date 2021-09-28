
package c195.Model;

import java.sql.Date;

/**
 * This class corresponds to the customers table
 * in the database;
 */
public class Customer {

    private int customerID;
    private String customerName;
    private String address;
    private String division;
    private String postalCode;
    private String phone;
    private String country;
    private Date lastUpdate;
    private String lastUpdateBy;

    public Customer() {

    }

    public Customer(int customerID, String customerName, int active, String address, String division, String postalCode, String phone, String country, Date lastUpdate, String lastUpdateBy) {
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddress(address);
        setDivision(division);
        setPostalCode(postalCode);
        setPhone(phone);
        setCountry(country);
        setLastUpdate(lastUpdate);
        setLastUpdateBy(lastUpdateBy);

    }

    public Customer(int customerID, String customerName) {
        setCustomerID(customerID);
        setCustomerName(customerName);
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getDivision() {
        return division;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDivision(String city) {
        this.division = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
