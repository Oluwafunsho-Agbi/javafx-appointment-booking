
package c195.Model;

/**
 * This class is used to generate the monthly
 * data report for each appointment
 */
public class Reports {
    private String month;
    private int inPerson;
    private int chat;
    private int phone;
    private int video;

    public Reports(String month, int inPerson, int chat, int phone, int video) {
        this.month = month;
        this.inPerson = inPerson;
        this.chat = chat;
        this.phone = phone;
        this.video = video;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getInPerson() {
        return inPerson;
    }

    public void setInPerson(int inPerson) {
        this.inPerson = inPerson;
    }

    public int getChat() {
        return chat;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }


}
