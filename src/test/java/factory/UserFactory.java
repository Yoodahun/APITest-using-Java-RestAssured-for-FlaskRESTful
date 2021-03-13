package factory;

import model.User;

import java.util.HashMap;

public class UserFactory extends BaseFactory {

    private static int USER_ID;
    private static User USER;
    private static String ACCESS_TOKEN;
    private static String REFRESH_TOKEN;

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

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        UserFactory.ACCESS_TOKEN = accessToken;
    }

    public static String getRefreshToken() {
        return REFRESH_TOKEN;
    }

    public static void setRefreshToken(String refreshToken) {
        UserFactory.REFRESH_TOKEN = refreshToken;
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

    public static void clearUser() {
        USER_ID = 0;
        USER = null;
        ACCESS_TOKEN = null;
        REFRESH_TOKEN = null;
    }
}
