package model;

public class Item {
    private int item_id;
    private String item_name;
    private float price;
    private int store_id;

    public Item(int id, String item_name, float price, int store_id) {
        this.item_id = id;
        this.item_name = item_name;
        this.price = price;
        this.store_id = store_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
