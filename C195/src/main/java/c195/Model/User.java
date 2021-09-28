
package c195.Model;

/**
 * This stores the User details.
 * The User logs in to the application on the
 * initial screen and add/updates the appointments
 * or customers
 */
public class User {
    private static int userID;
    private static String username;
    private static String password;
    public User() {
        userID = 0;
        username = null;
        password = null;

    }

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public static int getUserID() {
        return userID;
    }

    public static String getUsername() {
        return username;
    }

    public String getPassword() {
        return this.password;
    }

    public static void setUserID(int userID) {
        User.userID = userID;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static void setPassword(String password) {
        User.password = password;
    }
}
