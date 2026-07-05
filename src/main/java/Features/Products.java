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

    public Products(String code, String name, String brand, Double price, int quantity, String detail, String date, String picture){
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.detail = detail;
        this.date = date;
        this.picture = picture;
    }

    //Adds new row to txt file db
    public static void addProduct(String code, String name, String brand, Double price, int quantity, String detail, String date, String picture){

        String line = code + ", " + name + ", " + brand + ", " + price + ", " + quantity + ", " + detail + ", " + date + ", " + picture;
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.append("inventory_legacy.txt", line);
    }

    //Loading all Data into objects
    public static Products[] load(){

        TextFileManager textFileManager = new TextFileManager();
        String[] newlines = textFileManager.read("dealers_legacy.txt");

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

}
