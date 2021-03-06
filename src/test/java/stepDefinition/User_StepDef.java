package stepDefinition;

import factory.UserFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class User_StepDef extends UserFactory {

    private static String USER_END_POINT = null;
    private RequestSpecification requestSpec;

    @Given("User {string} API")
    public void user_api(String endPoint) {
        if ("Register".equals(endPoint)) {
            USER_END_POINT = "/register";

        } else if("DELETE".equals(endPoint) || "GET".equals(endPoint)) {
            USER_END_POINT = "/user/";
        } else if("Login".equals(endPoint)) {
            USER_END_POINT = "/login";
        }
    }


    @When("I try create user with {string} and {string} in request body")
    public void createUser_username_password_inRequestBody(String username, String password) {
        requestSpec = given().log().all().spec(getRequestSpec());
//                .body(UserFactory.createUserRequestBody(username, password));

        if (!"null".equals(username) && !"null".equals(password)) {
            requestSpec.body(UserFactory.createUserRequestBody(username, password));
        } else {
            requestSpec.body(UserFactory.createUserRequestBodyNull(username, password));
        }

        setResponse(
                requestSpec.when().post(USER_END_POINT)
                        .then().extract().response()
        );
    }


    @And("user has successfully {string}")
    public void userHasSuccessfully(String result) {
        JsonPath response = getResponse().body().jsonPath();

        if ("created".equals(result)) {
            Assert.assertEquals(
                    response.getString("message"),
                    "User created successfully"
            );

            UserFactory.setUSER_ID(response.getInt("id"));
        } else if("deleted".equals(result)) {
            Assert.assertEquals(
                    response.getString("message"),
                    "User deleted successfully."
            );

        }



    }

    @When("I try delete user information with user_id")
    public void deleteUserUsingUserID() {
        requestSpec = given().log().all().spec(getRequestSpec())
                        .pathParam("userID", UserFactory.getUSER_ID());

        setResponse(
                requestSpec.when().delete(USER_END_POINT+"{userID}")
                        .then().extract().response()
        );

    }

    @When("I try get user information with user_id")
    public void getUserInfoUsingUserID() {
        requestSpec = given().log().all().spec(getRequestSpec())
                .pathParam("userID", UserFactory.getUSER_ID());

        setResponse(
                requestSpec.when().get(USER_END_POINT+"{userID}")
                        .then().extract().response()
        );

    }

    @And("user is not found")
    public void userIsNotFound() {
        JsonPath response = getResponse().body().jsonPath();

        Assert.assertEquals(
                response.getString("message"),
                "User not found"
        );
    }

    @When("I try login user with {string} and {string}")
    public void loginUserUsing_username_And_password(String username, String password) {
        User user;
        if (UserFactory.getUser() == null || "not_registered_user".equals(username)) {
            user = UserFactory.createUserRequestBody(username, password);
        } else {
            user = UserFactory.getUser();
        }

        requestSpec = given().log().all().spec(getRequestSpec())
                .body(user);
        setResponse(
                requestSpec.when().post(USER_END_POINT)
                        .then().extract().response()
        );
    }

    @And("save access_token and refresh_token in response object")
    public void saveAccess_tokenAndRefresh_tokenInResponseObject() {
        UserFactory.setAccessToken(getJsonPathInResponse().getString("access_token"));
        UserFactory.setRefreshToken(getJsonPathInResponse().getString("refresh_token"));



    }


    @And("message is {string}")
    public void assertMessageIs(String messageBody) {
        Assert.assertEquals(
                getJsonPathInResponse().getString("message"),
                messageBody
        );
    }

    @And("{string} is cannot be let blank")
    public void isCannotBeLetBlank(String key) {
        Assert.assertEquals(
                getJsonPathInResponse().getString("message."+key),
                "This field cannot be let blank."
        );
    }

    @When("logout without access token")
    public void logoutWithoutAccessToken() {
        requestSpec = given().log().all().spec(getRequestSpec());

        setResponse(
                requestSpec.when().post("/logout")
                .then().extract().response()
        );

    }

    @When("logout with wrong access token")
    public void logoutWithWrongAccessToken() {
        requestSpec = given().log().all().spec(getRequestSpec())
            .header("Authorization", "Bearer wrongToken");

        setResponse(
                requestSpec.when().post("/logout")
                        .then().extract().response()
        );
    }

    @When("logout user information")
    public void logoutUserInformation() {
        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken());

        setResponse(
                requestSpec.when().post("/logout")
                        .then().extract().response()
        );
    }
}
