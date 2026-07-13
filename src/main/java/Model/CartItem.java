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

    public String getCartItemCode() {
        return this.itemCode;
    }

    public String getCartItemName() {
        return this.name;
    }

    public String getCartItemBrand() {
        return this.brand;
    }

    public double getCartItemPrice() {
        return this.price;
    }

    public int getCartItemQuantity() {
        return this.quantity;
    }

    public String getCartItemCategory() {
        return this.category;
    }

    public String getPicture() {return this.picture;}

    public void setCartItemQuantity(int quantity) {this.quantity = quantity;}

    public void setCartItemPrice(double price){this.price = price;}

}
