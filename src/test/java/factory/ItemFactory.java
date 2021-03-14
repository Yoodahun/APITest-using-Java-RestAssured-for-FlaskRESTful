package factory;

import constants.Endpoint;
import io.restassured.specification.RequestSpecification;
import model.Item;

import java.util.HashMap;

public class ItemFactory extends BaseFactory {


    private static Item item;

    public void executeItemRequest(Endpoint endpoint, RequestSpecification req) {

        switch (endpoint) {
            case GET:
                setResponse(
                        req.when().get("/item/{item_name}")
                                .then().extract().response()
                );
                break;
            case POST:
                setResponse(
                        req.when().post("/item/{item_name}")
                                .then().extract().response()
                );
                break;
            case PUT:
                setResponse(
                        req.when().put("/item/{item_name}")
                                .then().extract().response()
                );
                break;
            default: //DELETE
                setResponse(
                        req.when().delete("/item/{item_name}")
                                .then().extract().response()
                );
                break;

        }

    }

    public static void createItem(int id, String itemName, float price, int storeID) {
        item = new Item(id, itemName, price, storeID);


    }

    public static Item getItem() {
        return item;
    }

    public static void clearItem(){
        item = null;
    }

    public static HashMap createItemRequestBody(String price){
        HashMap<String, Object> requestBody = new HashMap();

        if("null".equals(price)) {
            requestBody.put("store_id", StoreFactory.getStoreID());
        } else {
            requestBody.put("store_id", StoreFactory.getStoreID());
            requestBody.put("price", Float.parseFloat(price));

        }

        return requestBody;

    }
}
