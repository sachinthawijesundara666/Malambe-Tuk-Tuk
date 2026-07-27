package Features;

import Model.CartItem;
import Model.Products;
import java.util.ArrayList;
import Cleaner.TextFileManager;

public class CartManager {
    private static ArrayList<CartItem> Basket = new ArrayList<>();

    public static String AddCart(Products product, int quantity){

        if (quantity>product.getQuantity()){
            return "QuantityError";
        }

        if (quantity<=0){
            return "QuantityError";
        }

        CartItem cartItem = new CartItem(product.getCode(), product.getName(), product.getBrand(), product.getPrice(), quantity, product.getCategory(), product.getPicture());
        for (CartItem basket : Basket){
            if (basket.getCode().equals(cartItem.getCode())){
                return "Duplicate";
            }
        }

        Basket.add(cartItem);
        return "AddedToCart";
    }


    public static void RemoveCart(CartItem cartItem){
        Basket.remove(cartItem);
    }


    public static ArrayList<CartItem> getBasket(){
        return Basket;
    }


    public static String setQuantity(CartItem cartItem, Products product, int quantity){
        if (quantity > product.getQuantity()){
            return "QuantityError";
        }

        if (quantity<=0){
            return "QuantityError";
        }
        cartItem.setCartItemQuantity(quantity);
        return "QuantitySet";
    }


    public static String proceed(){
        ArrayList<CartItem> CartBasket = CartManager.getBasket();

        if (CartBasket.isEmpty()){
            return "EmptyBasket";
        }
        TextFileManager textFileManager = new TextFileManager();
        Products[] productList = ProductManager.loadFromNewFile();

        if (productList == null)
        {
            return "ProductLoadingError";
        }

        for (CartItem cartItem : CartBasket) {

            for (Products p : productList){
                if (cartItem.getCode().equals(p.getCode())){
                    p.setQuantity(p.getQuantity() - cartItem.getQuantity());
                    break;
                }
            }

            String line = cartItem.getCode() + ", " + cartItem.getName() + ", " + cartItem.getBrand() + ", " + cartItem.getPrice() + ", " + cartItem.getQuantity() + ", " + cartItem.getCategory() +  ", " + cartItem.getPicture() + "\n";
            textFileManager.append("ProceedPaymentItems.txt", line);
            if (!textFileManager.getAppendFlag()){
                return "TextFileError";
            }
        }
        textFileManager.append("ProceedPaymentItems.txt", "\n");
        if (!textFileManager.getAppendFlag()){
            return "TextFileError";
        }

        textFileManager.write("Inventory.txt", "");
        if (!textFileManager.getWriteFlag()){
            return "TextFileError";
        }
        
        for(Products product : productList){
            String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand()+ ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getCategory() + ", " + product.getDate() + ", " + product.getPicture() + ", " + product.getThreshold() + "\n";
            textFileManager.append("Inventory.txt", line);
            if (!textFileManager.getAppendFlag()){
                return "TextFileError";
            }
        }
        CartManager.Basket.clear();
        return "Success";
    }

    public static String Total (){

        int ElectricalCount = 0;
        int EngineCount = 0;
        double total = 0d;
        boolean bulk = false;
        boolean synergy = false;
        double totalWithoutDiscount = 0;
        for (CartItem c : Basket) {
            double itemPrice = (c.getPrice() * c.getQuantity());
            totalWithoutDiscount += itemPrice;
            if (c.getQuantity() >= 3) {
                itemPrice = itemPrice * (95d/100d);
                bulk = true;
            }

            if (c.getCategory().equals("engine")) {
                EngineCount += 1;
            }

            if (c.getCategory().equals("electrical")) {
                ElectricalCount += 1;
            }

            total += itemPrice;
        }
        if (EngineCount>=1 && ElectricalCount>=1){
            total = total * 90d/100d;
            synergy = true;

        }
        if (bulk && synergy) {
            return "Total With No Discount: Rs." + String.format("%.2f", totalWithoutDiscount) + "\n" + "Bulk Discount Applied\nSynergy Discount Applied\nTotal: Rs." + String.format("%.2f", total);
        }
        else if (bulk) {
            return "Total With No Discount: Rs." + String.format("%.2f", totalWithoutDiscount) + "\n" +"Bulk Discount Applied\nTotal: Rs." + String.format("%.2f", total);
        }
        else if (synergy) {
            return "Total With No Discount: Rs." + String.format("%.2f", totalWithoutDiscount) + "\n" + "Synergy Discount Applied\nTotal: Rs." + String.format("%.2f", total);
        }
        else {
            return "Total With No Discount: Rs." + String.format("%.2f", totalWithoutDiscount) + "\n" + "No Discounts Applied\nTotal: Rs." + String.format("%.2f", total);
        }
    }
}


