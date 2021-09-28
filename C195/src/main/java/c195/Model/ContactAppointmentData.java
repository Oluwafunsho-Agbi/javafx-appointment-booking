package c195.Model;

/**
 * This class is used to generate the 3rd Custom report
 * which can be viewed by going to the third tab after clicking
 * on Reports button on the main screen.
 * The third custom report displays the appointments assigned
 * to each contact
 */
public class ContactAppointmentData {
    private String contact;
    private Integer inPerson;
    private Integer chat;
    private Integer phone;
    private Integer video;
    // total = inPerson + chat + phone + video
    private Integer total;

    public ContactAppointmentData() {
        inPerson = 0;
        chat = 0;
        phone = 0;
        total = 0;
        video = 0;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getInPerson() {
        return inPerson;
    }

    public void setInPerson(Integer inPerson) {
        this.inPerson = inPerson;
    }

    public Integer getChat() {
        return chat;
    }

    public void setChat(Integer chat) {
        this.chat = chat;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getVideo() {
        return video;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    /**
     * This should be called once any changes
     * has been made to the phone/video/in-person/chat
     * as the total of them will get incremented
     */
    public void incrementTotal() {
        total++;
    }
}
