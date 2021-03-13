package stepDefinition;

import constants.Endpoint;
import factory.BaseFactory;
import factory.StoreFactory;
import factory.UserFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import model.Item;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class Store_StepDef extends StoreFactory {

    private Endpoint USER_END_POINT;
    private RequestSpecification requestSpec;

    @Given("Store {string} API")
    public void setStoreEndPoint(String endPoint) {
        if ("create".equals(endPoint)) {
            this.USER_END_POINT = Endpoint.POST;
        } else if ("DELETE".equals(endPoint)){
            this.USER_END_POINT = Endpoint.DELETE;
        }
    }

    @When("I try create store information with {string}")
    public void createStoreUsing_storeName(String storeName) {

        requestSpec = given().log().all().spec(getRequestSpec())
                .pathParam("store_name", storeName);

        executeStoreRequest(USER_END_POINT, requestSpec, storeName);
    }

    @And("create store object in response object")
    public void createStoreObjectInResponseObject() {
        JsonPath response = getResponse().body().jsonPath();

        createStoreObject(
                response.getInt("id"),
                response.getString("name"),
                response.<Item>getList("items")
        );
    }

    @When("I try delete store information with {string}")
    public void iTryDeleteStoreInformationWith(String storeName) {
        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccess_token())
                .pathParam("store_name", storeName);

        executeStoreRequest(USER_END_POINT, requestSpec, storeName);

    }
















}
