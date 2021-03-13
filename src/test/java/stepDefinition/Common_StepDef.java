package stepDefinition;

import factory.BaseFactory;
import factory.UserFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class Common_StepDef extends BaseFactory {

    @Then("response status code is {int}")
    public void responseStatusCodeIs(int statusCode) {
        Assert.assertEquals(
                getResponse().statusCode(),
                statusCode
        );
    }

    @And("{string} value in response body is equal to {string}")
    public void valueInResponseBodyIsEqualTo(String key, String expected_value) {
        Assert.assertEquals(
                getResponse().body().jsonPath().getString(key),
                expected_value
        );
    }

    @And("response body has key that {string} and {string}")
    public void responseBodyHasKeyThatAnd(String key1, String key2) {

        if (getResponse().body().jsonPath().get(key1) != null ||
                getResponse().body().jsonPath().get(key2) != null) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }

    }

    @And("user has been logged in")
    public void userHasBeenLoggedIn() {

        Assert.assertNotEquals(
                UserFactory.getUser(),
                null
        );

    }

    @And("description is {string}")
    public void descriptionIs(String descriptionMessage) {
        Assert.assertEquals(
                getJsonPathInResponse().getString("description"),
                descriptionMessage
        );
    }
}


