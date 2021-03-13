package model;

import java.util.List;

public class Store {

    private int storeID;
    private String storeName;
    private List<Item> items;

    public Store(int storeID,String storeName, List<Item> items) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.items = items;
    }

    public int getStoreID() {
        return storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
