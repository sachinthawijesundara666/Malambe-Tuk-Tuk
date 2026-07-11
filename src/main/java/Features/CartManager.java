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

        CartItem cartItem = new CartItem(product.getCode(), product.getName(), product.getBrand(), product.getPrice(), quantity, product.getDetail(), product.getPicture());
        for (CartItem basket : Basket){
            if (basket.getCartItemCode().equals(cartItem.getCartItemCode())){
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
        if (quantity> product.getQuantity()){
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
        Products[] productList = ProductManager.load();

        if (productList == null)
        {
            return "ProductLoadingError";
        }

        for (CartItem cartItem : CartBasket) {

            for (Products p : productList){
                if (cartItem.getCartItemCode().equals(p.getCode())){
                    p.setQuantity(p.getQuantity() - cartItem.getCartItemQuantity());
                    break;
                }
            }

            String line = cartItem.getCartItemCode() + ", " + cartItem.getCartItemName() + ", " + cartItem.getCartItemBrand() + ", " + cartItem.getCartItemPrice() + ", " + cartItem.getCartItemQuantity() + ", " + cartItem.getCartItemDetail() +  ", " + cartItem.getPicture() + "\n";
            textFileManager.append("ProceedPaymentItems.txt", line);
            if (!textFileManager.getAppendFlag()){
                return "TextFileError";
            }
        }
        textFileManager.append("ProceedPaymentItems.txt", "\n");
        if (!textFileManager.getAppendFlag()){
            return "TextFileError";
        }

        textFileManager.write("inventory_legacy.txt", "");
        if (!textFileManager.getWriteFlag()){
            return "TextFileError";
        }
        
        for(Products product : productList){
            String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand()+ ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getDetail() + ", " + product.getDate() + ", " + product.getPicture() + "\n";
            textFileManager.append("inventory_legacy.txt", line);
            if (!textFileManager.getAppendFlag()){
                return "TextFileError";
            }
        }
        CartManager.Basket.clear();
        return "Success";
    }
}


