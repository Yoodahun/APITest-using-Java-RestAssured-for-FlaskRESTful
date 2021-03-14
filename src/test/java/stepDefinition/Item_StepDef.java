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

        if ("create_test_item1".equals(itemName)) {
            StoreFactory.createStoreObject(52, "store_for_test", null);
        }

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

        if (!"null".equals(price)) {
            requestBody.put("price", Float.parseFloat(price));
        }
        if( item.getStore_id() != 0) {
            requestBody.put("store_id", item.getStore_id());
        }



        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", ItemFactory.getItem().getItem_name())
                .body(requestBody);

        executeItemRequest(USER_END_POINT, requestSpec);
    }

    @When("I try get items information")
    public void iTryGetItemsInformation() {
        requestSpec = given().log().all().spec(getRequestSpec());

        if(UserFactory.getUser() != null) {
            requestSpec.header("Authorization", "Bearer "+ UserFactory.getAccessToken());
        }

        setResponse(
                requestSpec.when().get("/items")
                .then().extract().response()
        );


    }

    @And("item_name value in items response body is equal to {string}")
    public void item_nameValueInItemsResponseBodyIsEqualTo(String itemName) {
        JsonPath response = getJsonPathInResponse();


        if (UserFactory.getUser() == null) {
            Assert.assertEquals(
                    response.getString("items[0]"),
                    itemName
            );
        } else {
            Assert.assertEquals(
                    response.getString("items[0].name"),
                    itemName
            );
            System.out.println(response.get("items"));
            ItemFactory.createItem(
                    response.getInt("items[0].id"),
                    response.getString("items[0].name"),
                    response.getFloat("items[0].price"),
                    response.getInt("items[0].store_id")

            );
            StoreFactory.createStoreObject(
                    response.getInt("items[0].store_id"),
                    "store_for_test",
                    null
            );
        }


    }



    @And("message is An item with {string} already exists")
    public void messageIsAnItemWithAlreadyExists(String itemName) {
        JsonPath response = getJsonPathInResponse();

        Assert.assertEquals(
                response.getString("message"),
                "An item with '" + itemName +"' already exists"
        );
    }

    @When("I try create item information with {string} and {string}, store_id {string} in request body")
    public void iTryCreateItemInformationWithAndStore_idInRequestBody(String itemName, String price, String storeID) {

        HashMap<String, Object> requestBody = ItemFactory.createItemRequestBody(price);

        if ("no_store_item".equals(itemName)) {
            requestBody.put("store_id", 402);
        }
        if ("null".equals(storeID)) {
            requestBody.remove("store_id");
        }

        requestSpec = given().log().all().spec(getRequestSpec())
                .header("Authorization", "Bearer "+ UserFactory.getAccessToken())
                .pathParam("item_name", itemName)
                .body(requestBody);

        executeItemRequest(USER_END_POINT, requestSpec);

    }

    @And("store_id is Every item needs a store id")
    public void store_idIsEveryItemNeedsAStoreId() {
        JsonPath response = getJsonPathInResponse();

        Assert.assertEquals(
                response.getString("message.store_id"),
                "Every item needs a store id."
        );
    }

//    @When("I try delete item information with {string} in request body")
//    public void iTryDeleteItemInformationWithInRequestBody(String itemName) {
//
//    }
}
