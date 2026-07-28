package Features;

import Model.Products;
import java.util.ArrayList;

public class Search {
    public static Products[] search(Products[] products, String keyword, boolean lowStock, Double minPrice, Double maxPrice, String category){
        ArrayList<Products> result = new ArrayList<>();
        for(Products product : products){
            boolean match = true;
            if(keyword != null && !keyword.isEmpty()){
                String search = keyword.toLowerCase();
                if(!product.getCode().toLowerCase().contains(search) && !product.getName().toLowerCase().contains(search) && !product.getBrand().toLowerCase().contains(search)){
                    match = false;
                }
            }
            if(lowStock){
                if(product.getQuantity() >= product.getThreshold()){
                    match = false;
                }
            }

            if(minPrice != null){
                if(product.getPrice() < minPrice){
                    match = false;
                }
            }

            if(maxPrice != null){
                if(product.getPrice() > maxPrice){
                    match = false;
                }
            }

            if(category != null && !category.equals("All")){
                if(!product.getCategory().equalsIgnoreCase(category)){
                    match = false;
                }
            }

            if(match){
                result.add(product);
            }
        }
        return result.toArray(new Products[0]);
    }
}