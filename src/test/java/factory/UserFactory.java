package factory;

import model.User;

public class UserFactory extends BaseFactory {

    private static int USER_ID;
    private static User USER;
    private static String access_token;
    private static String refresh_token;

    public static User createUserRequestBody(String username, String password){
        USER = new User(username, password);
        return USER;
    }

    public static void setUSER_ID(int userID) {
        USER_ID = userID;
    }
    public static int getUSER_ID() {
        return USER_ID;
    }

    public static User getUser() {
        if (USER != null)
            return USER;
        return null;
    }

    public static String getAccess_token() {
        return access_token;
    }

    public static void setAccess_token(String access_token) {
        UserFactory.access_token = access_token;
    }

    public static String getRefresh_token() {
        return refresh_token;
    }

    public static void setRefresh_token(String refresh_token) {
        UserFactory.refresh_token = refresh_token;
    }
}
