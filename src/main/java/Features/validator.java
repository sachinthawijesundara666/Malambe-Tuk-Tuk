package Features;

import java.util.Locale;

public class validator {

    //Validating Date input
    public static String dateValidator(String date){

        return null;
    }

    //Validating Code
    public static boolean codeValidator(String Code){
        if (Code == null || Code.length()!=4) {
            return false;
        }

        if (!Code.toLowerCase(Locale.ROOT).startsWith("p")){
            return false;
        }

        for(int i=1 ; i<4 ; i++){
            if (Code.charAt(i) < '0' || Code.charAt(i) > '9')
                return false;
        }
        return true;
    }

    //validating Price
    public static Double priceVal(String price) {
        double priceConv = 0d;

        try {
            if (price != null && !price.isEmpty()) {
                priceConv = Double.parseDouble(price);
                return priceConv;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return priceConv;
    }

    //Validating Quantity
    public static Integer quantityVal(String quantity){
        int quantityConv = 0;

        try {
            if (quantity != null && !quantity.isEmpty()) {
                quantityConv = Integer.parseInt(quantity);
                return quantityConv;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return quantityConv;
    }

}