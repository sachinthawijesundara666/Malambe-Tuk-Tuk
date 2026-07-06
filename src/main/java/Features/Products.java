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

    public Products(String code, String name, String brand, Double price, int quantity, String detail, String date, String picture){
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

    //Adds new row to txt file db
    public static void addProduct(String code, String name, String brand, Double price, int quantity, String detail, String date, String picture){

        Products product = new Products(code, name, brand, price, quantity, detail, date, picture);
        String line = product.code + ", " + product.name + ", " + product.brand + ", " + product.price + ", " + product.quantity + ", " + product.detail + ", " + product.date + ", " + product.picture + ", " + product.threshold;
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.append("inventory_legacy.txt", line);
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

    public String update(String Code){
        return null;
    }

}
