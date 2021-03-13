package factory;

import constants.Endpoint;
import io.restassured.specification.RequestSpecification;
import model.Item;
import model.Store;

import java.util.List;

public class StoreFactory extends BaseFactory{

    private static Store store = null;
    private static int store_id;

    public static void createStoreObject(int store_id, String store_name, List<Item> items) {
        store = new Store(
                store_id,
                store_name,
                items
        );
    }

    public static int getStoreID() {
        return store.getStoreID();
    }

    public static int getStore_id() {
        return store_id;
    }

    public static void setStore_id(int store_id) {
        StoreFactory.store_id = store_id;
    }

    public void executeStoreRequest(Endpoint endpoint, RequestSpecification req) {

        switch (endpoint) {
            case GET:
                setResponse(
                        req.when().get("/store/{store_name}")
                                .then().extract().response()
                );
                break;
            case POST:
                setResponse(
                        req.when().post("/store/{store_name}")
                                .then().extract().response()
                );
                break;
            case PUT:
                break;
            default: //DELETE
                setResponse(
                        req.when().delete("/store/{store_name}")
                                .then().extract().response()
                );
                break;

        }

    }

    public static void clearStore() {
        store = null;
        store_id = 0;
    }

}
