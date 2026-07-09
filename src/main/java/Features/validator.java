package Features;

import java.util.Locale;

public class validator {

    //Validating Date input
    public static boolean dateValidator(String date) {

        if (date == null || date.isEmpty()) {
            return false;
        }

        String[] dateParts = date.split("/");

        if (dateParts.length != 3) {
            return false;
        }

        String yearStr = dateParts[0];
        String monthStr = dateParts[1];
        String dayStr = dateParts[2];

        if (yearStr.length() != 4 || monthStr.length() != 2 || dayStr.length() != 2) {
            return false;
        }

        int year;
        int month;
        int day;

        try {
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (month < 1 || month > 12) {
            return false;
        }

        int[] daysOfMonth = {31,28,31,30,31,30,31,31,30,31,30,31};

        // Leap year
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            daysOfMonth[1] = 29;
        }

        if (day < 1 || day > daysOfMonth[month - 1]) {
            return false;
        }

        return true;
    }

    //Validating Code
    public static boolean codeValidator(String Code){
        if (Code == null || Code.isEmpty()) {
            return false;
        }

        if ( Code.length()!=4){
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