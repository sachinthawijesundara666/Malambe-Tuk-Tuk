package Features;

public class Products {
    String code;
    String name;
    String brand;
    Double price;
    int quantity;
    String detail;
    String date;
    String picture;
    String threshold;

    public Products(String code, String name, String brand, double price, int quantity, String detail, String date, String picture){
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.detail = detail;
        this.date = date;
        this.picture = picture;
        if (this.quantity < 5){
            this.threshold = "Low Stock";
        }else this.threshold = "In Stock";
    }

    //Validating Date input
    public static String dateValidator(String date){
        return null;
    }

    //Validating Code
    public static String codeValidator(){
        return null;
    }


    //Adds new row to txt file db
    public static String addProduct(String code, String name, String brand, String price, String quantity, String detail, String date, String picture){
        if (code != null && !code.isEmpty()){
            double priceConv = 0d;
            int quantityConv = 0;

            //To make sure it converts empty string values correctly
            try {

                if (price != null && !price.isEmpty()) {
                    priceConv = Double.parseDouble(price);
                }

                if (quantity != null && !quantity.isEmpty()) {
                    quantityConv = Integer.parseInt(quantity);
                }
            }catch (NumberFormatException e){
                return "NumberFormatError";
            }

            if (name.isEmpty()) {
                return "NoName";
            }

            Products product = new Products(code, name, brand, priceConv, quantityConv, detail, date, picture);
            String line = product.code + ", " + product.name + ", " + product.brand + ", " + product.price + ", " + product.quantity + ", " + product.detail + ", " + product.date + ", " + product.picture + ", " + product.threshold;
            TextFileManager textFileManager = new TextFileManager();
            textFileManager.append("inventory_legacy.txt", line);

        }else return "NoCode";
        return "Success";
    }

    //Loading all Data into objects
    public static Products[] load(){

        TextFileManager textFileManager = new TextFileManager();
        String[] newlines = textFileManager.read("inventory_legacy.txt");

        if (newlines != null) {

            Cleaner cleaner = new Cleaner();
            String[][] cleaned = cleaner.clean(newlines);
            Products[] productlist = new Products[cleaned.length];

            for (int i = 0 ; i < cleaned.length ; i++){
                productlist[i] = new Products(
                        cleaned[i][0],
                        cleaned[i][1],
                        cleaned[i][2],
                        Double.parseDouble(cleaned[i][3]),
                        Integer.parseInt(cleaned[i][4]),
                        cleaned[i][5],
                        cleaned[i][6],
                        cleaned[i][7]
                );
            }
            return productlist;

        }else return null;

    }

    //Deleting data
    public static String delete(String code){
        Products[] prodlist = load();

        if (prodlist == null){
            return "Empty";
        }

        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write("inventory_legacy.txt", "");

        boolean found = false;

        for (int i = 0 ; i<prodlist.length ; i++){

            if (code.equals(prodlist[i].code)) {
                found = true;
                continue;
            }
            String line = prodlist[i].code + ", " +
                        prodlist[i].name + ", " +
                        prodlist[i].brand + ", " +
                        prodlist[i].price + ", " +
                        prodlist[i].quantity + ", " +
                        prodlist[i].detail + ", " +
                        prodlist[i].date + ", " +
                        prodlist[i].picture + ", " +
                        prodlist[i].threshold;

            textFileManager.append("inventory_legacy.txt", line);

        }
        if (found){
            return "Found";
        }else return "Not_Found";
    }

    public static String update(String code, String name, String brand, String price, String quantity, String detail, String date, String picture){
        Products[] prodlist = load();

        if (prodlist == null){
            return "Empty";
        }

        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write("inventory_legacy.txt", "");

        boolean found = false;

        double priceConv = 0d;
        int quantityConv = 0;

        //To make sure it converts empty string values correctly
        try {

            if (price != null && !price.isEmpty()) {
                priceConv = Double.parseDouble(price);
            }

            if (quantity != null && !quantity.isEmpty()) {
                quantityConv = Integer.parseInt(quantity);
            }
        }catch (NumberFormatException e){
            return "NumberFormatError";
        }

        for (int i=0 ; i < prodlist.length ; i++){

            if (code.equals(prodlist[i].code)) {
                found = true;

                if ( name != null && !name.isEmpty()){
                    prodlist[i].name = name;
                }

                if ( brand != null && !brand.isEmpty()) {
                    prodlist[i].brand = brand;
                }

                if (price != null && !price.isEmpty()) {
                    prodlist[i].price = priceConv;
                }

                if (quantity != null && !quantity.isEmpty()) {
                    prodlist[i].quantity = quantityConv;

                    if (prodlist[i].quantity < 5) {
                        prodlist[i].threshold = "Low Stock";
                    } else {
                        prodlist[i].threshold = "In Stock";
                    }
                }

                if (detail != null && !detail.isEmpty()) {
                    prodlist[i].detail = detail;
                }

                if (date != null && !date.isEmpty()) {
                    prodlist[i].date = date;
                }

                if (picture != null && !picture.isEmpty()) {
                    prodlist[i].picture = picture;
                }

            }
            String line = prodlist[i].code + ", " +
                    prodlist[i].name + ", " +
                    prodlist[i].brand + ", " +
                    prodlist[i].price + ", " +
                    prodlist[i].quantity + ", " +
                    prodlist[i].detail + ", " +
                    prodlist[i].date + ", " +
                    prodlist[i].picture + ", " +
                    prodlist[i].threshold;

            textFileManager.append("inventory_legacy.txt", line);
        }
        if (found){
            return "Success";
        }else return "NotFound";
    }
}


