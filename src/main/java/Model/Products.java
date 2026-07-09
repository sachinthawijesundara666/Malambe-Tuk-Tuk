package Model;

public class Products {
    private final String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String detail;
    private String date;
    private String picture;
    private String threshold;

    public Products(String code, String name, String brand, double price, int quantity, String detail, String date, String picture) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.detail = detail;
        this.date = date;
        this.picture = picture;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }

    public String getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDate(String date) {this.date = date;}

    public void setPicture(String picture) {
        this.picture = picture;
    }

}




