package Model;

public class CartItem {
    private String itemCode;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String picture;

    public CartItem(String code, String name, String brand, double price, int quantity, String detail, String picture){
        this.itemCode = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = detail;
        this.picture = picture;
    }

    public String getCode() {
        return this.itemCode;
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

    public String getPicture() {return this.picture;}

    public void setCartItemQuantity(int quantity) {this.quantity = quantity;}

    public void setCartItemPrice(double price){this.price = price;}

}
