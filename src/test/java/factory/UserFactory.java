package factory;

import model.User;

import java.util.HashMap;

public class UserFactory extends BaseFactory {

    private static int USER_ID;
    private static User USER;
    private static String access_token;
    private static String refresh_token;

    public static User createUserRequestBody(String username, String password){

        if (username == null) {
            USER = new User(null, password);
        } else if (password == null) {
            USER = new User(username, null);
        } else {
            USER = new User(username, password);
        }
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

    public static HashMap createUserRequestBodyNull(String username, String password) {
        HashMap<String, Object> requestBody = new HashMap();

        if ("null".equals(username)) {
            requestBody.put("password", password);
        }
        if ("null".equals(password)) {
            requestBody.put("username", username);
        }

        return requestBody;
    }
}
