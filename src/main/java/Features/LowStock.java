package Features;

import Model.Products;
import Cleaner.TextFileManager;

public class LowStock {

    public static String setLowStockThreshold(Products selectedProduct, int threshold) {

        if (threshold <= 0) {
            return "ThresholdError";
        }

        Products[] products = ProductManager.loadFromNewFile();

        if (products == null) {
            return "LoadingError";
        }

        for (Products product : products) {
            if (product.getCode().equals(selectedProduct.getCode())) {
                product.setThreshold(threshold);
                break;
            }
        }

        TextFileManager textFileManager = new TextFileManager();

        textFileManager.write("Inventory.txt", "");
        if (!textFileManager.getWriteFlag()) {
            return "TextFileError";
        }

        for (Products product : products) {

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
        AuditLogger.log("Threshold Changed " + selectedProduct.getCode());
        return "Success";
    }

    public static boolean isLowStock(Products product){
        return product.getQuantity() < product.getThreshold();
    }
}
