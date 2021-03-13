package stepDefinition;

import constants.Endpoint;
import factory.ItemFactory;
import factory.StoreFactory;
import factory.UserFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import model.Item;
import org.testng.Assert;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Item_StepDef extends ItemFactory {

    private Endpoint USER_END_POINT;
    private RequestSpecification requestSpec;

    @Given("Item {string} API")
    public void itemAPI(String endPoint) {
        if("create".equals(endPoint)) {
            this.USER_END_POINT = Endpoint.POST;
        }
        if("GET".equals(endPoint)) {
            this.USER_END_POINT = Endpoint.GET;
        }
        if("PUT".equals(endPoint)) {
            this.USER_END_POINT = Endpoint.PUT;
        }
        if("DELETE".equals(endPoint)) {
            this.USER_END_POINT = Endpoint.DELETE;
        }
    }

    @When("I try create item information with {string} and {string}, store_id in request body")
    public void iTryCreateItemInformationWithAndStore_idInRequestBody(String itemName, String price) {

        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", itemName)
                .body(ItemFactory.createItemRequestBody(price));

        executeItemRequest(USER_END_POINT, requestSpec);


    }

    @And("{string} value in response body is equal to store_id")
    public void valueInResponseBodyIsEqualToStore_id(String storeID) {
        JsonPath response = getJsonPathInResponse();
        Assert.assertEquals(
                response.getInt(storeID),
                StoreFactory.getStoreID()
        );
    }

    @And("create item object in context object")
    public void createItemObjectInContextObject() {
        JsonPath response = getJsonPathInResponse();

        ItemFactory.createItem(
                response.getInt("id"),
                response.getString("name"),
                response.getFloat("price"),
                response.getInt("store_id")
        );
    }

    @When("I try get item information with {string}")
    public void iTryGetItemInformationWith(String itemName) {
        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", itemName);

        executeItemRequest(USER_END_POINT, requestSpec);

    }

    @When("I try delete item information with {string}")
    public void iTryDeleteItemInformationWith(String itemName) {
        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", itemName);

        executeItemRequest(USER_END_POINT, requestSpec);
    }

    @And("item has successfully deleted")
    public void itemHasSuccessfullyDeleted() {
        JsonPath response = getJsonPathInResponse();

        Assert.assertEquals(
                response.getString("message"),
                "Item deleted"

        );
    }

    @When("I try put item information with item_name and {string}, store_id in request body")
    public void iTryPutItemInformationWithItem_nameAndStore_idInRequestBody(String price) {

        Item item = ItemFactory.getItem();

        HashMap<String, Object> requestBody = new HashMap();
        requestBody.put("price", Float.parseFloat(price));
        requestBody.put("store_id", item.getStore_id());


        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", ItemFactory.getItem().getItem_name())
                .body(requestBody);

        executeItemRequest(USER_END_POINT, requestSpec);
    }
}
