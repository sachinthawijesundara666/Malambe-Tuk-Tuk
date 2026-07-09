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

            Products[] ProductList = load();

            if (ProductList == null){
                return "TextFileError";
            }

            boolean foundCode = false;
            for (int i = 0 ; i< ProductList.length ; i++){
                if (code.equals(ProductList[i].getCode())){
                    foundCode = true;
                    break;
                }
            }

            if (!foundCode){
                Products product = new Products(code, name, brand, priceConv, quantityConv, detail, date, picture);
                String line = product.getCode() + ", " + product.getName() + ", " + product.getBrand()+ ", " + product.getPrice() + ", " + product.getQuantity() + ", " + product.getDetail() + ", " + product.getDate() + ", " + product.getPicture();
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
            return "Empty";
        }

        boolean found = false;

        for (int i = 0 ; i<prodlist.length ; i++) {

            if (code.equals(prodlist[i].getCode())) {
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
            for (int i = 0 ; i<prodlist.length ; i++) {

                if (code.equals(prodlist[i].getCode())){
                    continue;
                }

                String line = prodlist[i].getCode() + ", " +
                        prodlist[i].getName() + ", " +
                        prodlist[i].getBrand() + ", " +
                        prodlist[i].getPrice() + ", " +
                        prodlist[i].getQuantity() + ", " +
                        prodlist[i].getDetail() + ", " +
                        prodlist[i].getDate() + ", " +
                        prodlist[i].getPicture() + ", " ;

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
            return "Empty";
        }

        boolean found = false;

        for (int i = 0; i < prodlist.length; i++) {

            if (code.equals(prodlist[i].getCode())) {
                found = true;

                if (name != null && !name.isEmpty()) {
                    prodlist[i].setName(name);
                }

                if (brand != null && !brand.isEmpty()) {
                    prodlist[i].setBrand(brand);
                }

                if (price != null && !price.isEmpty()) {
                    Double priceConv = validator.priceVal(price);
                    if (priceConv == null) {
                        return "NumberFormatError";
                    }
                    prodlist[i].setPrice(priceConv);
                }

                if (quantity != null && !quantity.isEmpty()) {
                    Integer quantityConv = validator.quantityVal(quantity);
                    if (quantityConv == null) {
                        return "NumberFormatError";
                    }
                    prodlist[i].setQuantity(quantityConv);
                }

                if (detail != null && !detail.isEmpty()) {
                    prodlist[i].setDetail(detail);
                }

                if (date != null && !date.isEmpty()) {
                    if (!validator.dateValidator(date)){
                        return "DateFormatError";
                    }
                    prodlist[i].setDate(date);
                }

                if (picture != null && !picture.isEmpty()) {
                    prodlist[i].setPicture(picture);
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

            for (int i = 0; i < prodlist.length; i++) {
                String line = prodlist[i].getCode() + ", " +
                        prodlist[i].getName() + ", " +
                        prodlist[i].getBrand() + ", " +
                        prodlist[i].getPrice() + ", " +
                        prodlist[i].getQuantity() + ", " +
                        prodlist[i].getDetail() + ", " +
                        prodlist[i].getDate() + ", " +
                        prodlist[i].getPicture() + ", ";

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

