package Features;

import Cleaner.Cleaner;
import Model.Products;
import Cleaner.TextFileManager;

public class ProductManager {
    //Adds new row to txt file db
    public static String addProduct(String code, String name, String brand, String price, String quantity, String detail, String date, String picture){

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

            Products[] ProductList = load();

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
                Products product = new Products(code, name, brand, priceConv, quantityConv, detail, date, picture);
                String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand()+ ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getCategory() + ", " + product.getDate() + ", " + product.getPicture() + "\n";
                TextFileManager textFileManager = new TextFileManager();
                textFileManager.append("inventory_legacy.txt", line);
                if (!textFileManager.getAppendFlag()){
                    return "TextFileError";
                }
            }else return "Duplicate";
        }else return "NoCode";
        return "Success";
    }

    //Loading all Data into objects
    public static Products[] load(){

        Products[] productlist;
        TextFileManager textFileManager = new TextFileManager();

        String[] newlines = textFileManager.read("inventory_legacy.txt");
        if (!textFileManager.getReadFlag()){
            return null;
        }

        Cleaner cleaner = new Cleaner();
        String[][] cleaned = cleaner.clean(newlines);
        productlist = new Products[cleaned.length];

        for (int i = 0 ; i < cleaned.length ; i++){
            try {
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
            }catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e){
                return null;
            }
        }
        return productlist;
    }

    //Deleting data
    public static String delete(String code){
        if (code == null || code.isEmpty()){
            return "NoCode";
        }

        Products[] prodlist = load();

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
            textFileManager.write("inventory_legacy.txt", "");
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
                        product.getPicture() + "\n";

                textFileManager.append("inventory_legacy.txt", line);
                if (!textFileManager.getAppendFlag()) {
                    return "TextFileError";
                }
            }

        }else {
            return "NotFound";
        }
        return "Success";
    }

    public static String update(String code, String name, String brand, String price, String quantity, String detail, String date, String picture) {
        if (!validator.codeValidator(code)) {
            return "CodeFormatError";
        }

        Products[] prodlist = load();

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
            textFileManager.write("inventory_legacy.txt", "");
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
                        product.getPicture() + "\n";

                textFileManager.append("inventory_legacy.txt", line);
                if (!textFileManager.getAppendFlag()) {
                    return "TextFileError";
                }
            }
        } else {
            return "NotFound";
        }return "Success";
    }
}

