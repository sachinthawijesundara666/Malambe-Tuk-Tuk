package Model;

public class Products {
    private final String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String date;
    private String picture;
    private int threshold;

    public Products(String code, String name, String brand, double price, int quantity, String category, String date, String picture, int threshold) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.date = date;
        this.picture = picture;
        this.threshold = threshold;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getBrand() {
        return this.brand;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDate() {
        return this.date;
    }

    public String getPicture() {return this.picture;}

    public int getThreshold() { return threshold;}

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) { this.price = price; }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {this.date = date;}

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setThreshold(int threshold) { this.threshold = threshold; }

}




