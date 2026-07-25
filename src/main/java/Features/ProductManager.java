package Features;

import Cleaner.Cleaner;
import Model.Products;
import Cleaner.TextFileManager;

public class ProductManager {

    //Adds new row to txt file db
    public static String addProduct(String code, String name, String brand, String price, String quantity, String detail, String date, String picture){
        int threshold = 10;
        if (code != null && !code.isEmpty()){

            if (!validator.codeValidator(code)){
                return "CodeFormatError";
            }

            if (name == null || name.isEmpty()) {
                return "NoName";
            }

            Double priceConv = validator.priceVal(price);
            if (priceConv == null){
                return "NumberFormatError";
            }

            Integer quantityConv = validator.quantityVal(quantity);
            if (quantityConv == null){
                return "NumberFormatError";
            }
            if (!validator.dateValidator(date)) {
                return "DateFormatError";
            }

            Products[] ProductList = loadFromNewFile();

            if (ProductList == null){
                return "TextFileError";
            }

            boolean foundCode = false;
            for (Products products : ProductList) {
                if (code.equals(products.getCode())) {
                    foundCode = true;
                    break;
                }
            }

            if (!foundCode){
                Products product = new Products(code, name, brand, priceConv, quantityConv, detail, date, picture, threshold);
                String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand()+ ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getCategory() + ", " + product.getDate() + ", " + product.getPicture() + ", " + product.getThreshold() + "\n";
                TextFileManager textFileManager = new TextFileManager();
                textFileManager.append("Inventory.txt", line);
                if (!textFileManager.getAppendFlag()){
                    return "TextFileError";
                }
            }else return "Duplicate";
        }else return "NoCode";
        AuditLogger.log("Item Added " + code);
        return "Success";
    }

    //Loading all Data into objects
    public static Products[] load(String Location){

        Products[] productlist;
        TextFileManager textFileManager = new TextFileManager();

        String[] newlines = textFileManager.read(Location);
        if (!textFileManager.getReadFlag()){
            return null;
        }

        Cleaner cleaner = new Cleaner();
        String[][] cleaned = cleaner.clean(newlines);
        productlist = new Products[cleaned.length];

        for (int i = 0 ; i < cleaned.length ; i++){
            try {

                int threshold = 10;
                if(cleaned[i].length > 8){
                    threshold = Integer.parseInt(cleaned[i][8]);
                }

                if (cleaned[i][3].equals("null")){
                    cleaned[i][3] = "0";
                }

                if (cleaned[i][4].equals("null")){
                    cleaned[i][4] = "0";
                }

                Double priceConv = validator.priceVal(cleaned[i][3]);
                if (priceConv == null){
                    cleaned[i][3] = "0";
                }

                Integer quantityConv = validator.quantityVal(cleaned[i][4]);
                if (quantityConv == null){
                    cleaned[i][4] = "0";
                }

                productlist[i] = new Products(
                        cleaned[i][0],
                        cleaned[i][1],
                        cleaned[i][2],
                        Double.parseDouble(cleaned[i][3]),
                        Integer.parseInt(cleaned[i][4]),
                        cleaned[i][5],
                        cleaned[i][6],
                        cleaned[i][7],
                        threshold
                );
            }catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e){
                return null;
            }
        }
        return productlist;
    }
    //Loads From the new file
    public static Products[] loadFromNewFile() {

        Products[] legacyProducts = load("inventory_legacy.txt");

        if (legacyProducts == null) {
            return null;
        }

        Products[] inventoryProducts = load("Inventory.txt");

        TextFileManager textFileManager = new TextFileManager();

        // Inventory.txt doesn't exist or couldn't be read
        if (inventoryProducts == null || inventoryProducts.length == 0) {

            for (Products product : legacyProducts) {

                String line = product.getCode() + ", " +
                        product.getName() + ", " +
                        product.getBrand() + ", " +
                        product.getPrice() + ", " +
                        product.getQuantity() + ", " +
                        product.getCategory() + ", " +
                        product.getDate() + ", " +
                        product.getPicture() + ", " +
                        product.getThreshold() + "\n";

                textFileManager.append("Inventory.txt", line);

                if (!textFileManager.getAppendFlag()) {
                    return null;
                }
            }

        } else {

            for (Products legacy : legacyProducts) {

                boolean found = false;

                for (Products inventory : inventoryProducts) {

                    if (legacy.getCode().equals(inventory.getCode())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {

                    String line = legacy.getCode() + ", " +
                            legacy.getName() + ", " +
                            legacy.getBrand() + ", " +
                            legacy.getPrice() + ", " +
                            legacy.getQuantity() + ", " +
                            legacy.getCategory() + ", " +
                            legacy.getDate() + ", " +
                            legacy.getPicture() + ", " +
                            legacy.getThreshold() + "\n";

                    textFileManager.append("Inventory.txt", line);

                    if (!textFileManager.getAppendFlag()) {
                        return null;
                    }
                }
            }
        }

        return load("Inventory.txt");
    }

    //Deleting data
    public static String delete(String code){
        if (code == null || code.isEmpty()){
            return "NoCode";
        }

        Products[] prodlist = loadFromNewFile();

        if (prodlist == null){
            return "LoadingError";
        }

        boolean found = false;

        for (Products value : prodlist) {

            if (code.equals(value.getCode())) {
                found = true;
                break;
            }
        }

        if (found){
            TextFileManager textFileManager = new TextFileManager();
            textFileManager.write("Inventory.txt", "");
            if (!textFileManager.getWriteFlag()){
                return "TextFileError";
            }
            for (Products product : prodlist) {

                if (code.equals(product.getCode())) {
                    continue;
                }

                String line = product.getCode() + ", " +
                        product.getName() + ", " +
                        product.getBrand() + ", " +
                        product.getPrice() + ", " +
                        product.getQuantity() + ", " +
                        product.getCategory() + ", " +
                        product.getDate() + ", " +
                        product.getPicture() + ", " +
                        product.getThreshold() + "\n";

                textFileManager.append("Inventory.txt", line);
                if (!textFileManager.getAppendFlag()) {
                    return "TextFileError";
                }
            }

        }else {
            return "NotFound";
        }
        AuditLogger.log("Item Deleted " + code);
        return "Success";
    }

    public static String update(String code, String name, String brand, String price, String quantity, String detail, String date, String picture) {
        if (!validator.codeValidator(code)) {
            return "CodeFormatError";
        }

        Products[] prodlist = loadFromNewFile();

        if (prodlist == null) {
            return "LoadingError";
        }

        boolean found = false;

        for (Products product : prodlist) {

            if (code.equals(product.getCode())) {
                found = true;

                if (name != null && !name.isEmpty()) {
                    product.setName(name);
                }

                if (brand != null && !brand.isEmpty()) {
                    product.setBrand(brand);
                }

                if (price != null && !price.isEmpty()) {
                    Double priceConv = validator.priceVal(price);
                    if (priceConv == null) {
                        return "NumberFormatError";
                    }
                    product.setPrice(priceConv);
                }

                if (quantity != null && !quantity.isEmpty()) {
                    Integer quantityConv = validator.quantityVal(quantity);
                    if (quantityConv == null) {
                        return "NumberFormatError";
                    }
                    product.setQuantity(quantityConv);
                }

                if (detail != null && !detail.isEmpty()) {
                    product.setCategory(detail);
                }

                if (date != null && !date.isEmpty()) {
                    if (!validator.dateValidator(date)) {
                        return "DateFormatError";
                    }
                    product.setDate(date);
                }

                if (picture != null && !picture.isEmpty()) {
                    product.setPicture(picture);
                }
                break;
            }

        }

        if (found) {
            TextFileManager textFileManager = new TextFileManager();
            textFileManager.write("Inventory.txt", "");
            if (!textFileManager.getWriteFlag()) {
                return "TextFileError";
            }

            for (Products product : prodlist) {
                String line = product.getCode() + ", " +
                        product.getName() + ", " +
                        product.getBrand() + ", " +
                        product.getPrice() + ", " +
                        product.getQuantity() + ", " +
                        product.getCategory() + ", " +
                        product.getDate() + ", " +
                        product.getPicture() + ", " +
                        product.getThreshold() + "\n";

                textFileManager.append("Inventory.txt", line);
                if (!textFileManager.getAppendFlag()) {
                    return "TextFileError";
                }
            }
        } else {
            return "NotFound";
        }
        AuditLogger.log("Item Updated " + code);
        return "Success";
    }
}

