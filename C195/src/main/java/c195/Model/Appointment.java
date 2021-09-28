
package c195.Model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * This class captures the various fields in appointments table
 */
public class Appointment {

    private int appointmentID;
    private int customerID;
    private int userID;
    private Customer customer;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private String type;
    private String startTime;
    private String endTime;
    private String customerName;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;


    public Appointment(int appointmentID, int customerID, int userID, String title, String description, String location,
                       String contact, String type, String startTime, String endTime, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {

        setAppointmentID(appointmentID);
        setCustomerID(customerID);
        setUserID(userID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContactName(contact);
        setType(type);
        setStart(startTime);
        setEnd(endTime);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
    }

    public Appointment(int appointmentID, String startTime, String endTime, String title, String type, int customerId, String user) {
        setAppointmentID(appointmentID);
        setStart(startTime);
        setEnd(endTime);
        setTitle(title);
        setType(type);
        setCustomerID(customerId);
        setCreatedBy(user);
    }

    public Appointment(int appointmentID, int customerID, int userID, String title, String description, String location, String contact, String type, String startTime, String endTime, String user) {

        setAppointmentID(appointmentID);
        setCustomerID(customerID);
        setUserID(userID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContactName(contact);
        setType(type);
        setStart(startTime);
        setEnd(endTime);
        setCreatedBy(user);

    }

    public Appointment(String startTime, String endTime, String title, String type, String customer, String consultant) {
        setStart(startTime);
        setEnd(endTime);
        setTitle(title);
        setType(type);
        setCustomerName(customer);
        setCreatedBy(consultant);
    }

    public Appointment() {
    }

    public int getAppointmentID() {
        return this.appointmentID;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getType() {
        return this.type;
    }

    public String getStart() {
        return this.startTime;
    }

    public String getEnd() {
        return this.endTime;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getConsultantName() {
        return this.createdBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart(String startTime) {
        this.startTime = startTime;
    }

    public void setEnd(String endTime) {
        this.endTime = endTime;
    }

    private void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    private void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;

    }
}