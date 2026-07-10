package Features;

import Model.Products;
import java.util.ArrayList;
import Cleaner.TextFileManager;

public class Cart {
    private static ArrayList<Products> Basket = new ArrayList<>();

    public static void AddCart(Products product){
        Basket.add(product);
    }

    public static void RemoveCart(Products product){
        Basket.remove(product);
    }

    public static ArrayList<Products> getBasket(){
        return Basket;
    }

    public static String proceed(){
        ArrayList<Products> CartBasket = Cart.getBasket();
        if (CartBasket.isEmpty()){
            return "EmptyBasket";
        }
        TextFileManager textFileManager = new TextFileManager();

        for (Products product : CartBasket) {
            String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand() + ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getDetail() + ", " + product.getDate() + ", " + product.getPicture();
            textFileManager.append("ProceedPaymentItems.txt", line);
            if (!textFileManager.getAppendFlag()){
                return "TextFileError";
            }
        }
        textFileManager.append("ProceedPaymentItems.txt", "");
        if (!textFileManager.getAppendFlag()){
            return "NoLineSpace";
        }
        Cart.Basket.clear();
        return "Success";
    }
}


