package Features;

import Model.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {

    @Test
    @DisplayName("rejects a badly formatted product code")
    void addProductRejectsMalformedCode() {
        String result = ProductManager.addProduct("P9X9", "Rear Shock Absorber", "Bajaj", "2650", "6", "Bodywork", "2024/02/20", "shock_absorber.jpg");

        assertEquals("CodeFormatError", result);
    }

    @Test
    @DisplayName("rejects a price that isn't a number")
    void addProductRejectsNonNumericPrice() {
        String result = ProductManager.addProduct("P915", "Rear View Mirror Convex", "Local", "twelve hundred", "12", "Bodywork", "2024/02/20", "mirror_convex.jpg");

        assertEquals("NumberFormatError", result);
    }

    @Test
    @DisplayName("adds a product then rejects the same code as a duplicate")
    void addProductSucceedsThenRejectsDuplicateCode() {
        try {
            assertEquals("Success", ProductManager.addProduct("P910", "Rear Shock Absorber", "Bajaj", "2650", "6", "Bodywork", "2024/02/20", "shock_absorber.jpg"));
            assertEquals("Duplicate", ProductManager.addProduct("P910", "Rear Shock Absorber", "Bajaj", "2650", "6", "Bodywork", "2024/02/20", "shock_absorber.jpg"));
        } finally {
            ProductManager.delete("P910");
        }
    }

    @Test
    @DisplayName("reads the real legacy file and handles a missing brand")
    void loadParsesRealLegacyFileAndHandlesMissingBrand() {
        Products[] legacy = ProductManager.load("inventory_legacy.txt");

        assertNotNull(legacy);
        assertEquals(10, legacy.length);
        assertEquals("P001", legacy[0].getCode());
        assertEquals("P004", legacy[3].getCode());
        assertEquals(850.0, legacy[3].getPrice());
        assertEquals("P003", legacy[2].getCode());
        assertEquals("null", legacy[2].getBrand());
    }

    @Test
    @DisplayName("returns null for a file that doesn't exist")
    void loadReturnsNullForFileThatDoesNotExist() {
        assertNull(ProductManager.load("supplier_price_list_2025.txt"));
    }

    @Test
    @DisplayName("merges the legacy and live inventory into one list")
    void loadFromNewFileMergesLegacyAndLiveInventory() {
        Products[] products = ProductManager.loadFromNewFile();

        assertNotNull(products);
        assertTrue(products.length >= 10);
        for (Products product : products) {
            assertNotNull(product.getCode());
        }
    }

    @Test
    @DisplayName("deletes a product then reports it as not found the second time")
    void deleteRemovesProductThenReportsNotFoundOnSecondAttempt() {
        assertEquals("NoCode", ProductManager.delete(""));

        ProductManager.addProduct("P911", "Brake Cable Assembly", "TVS", "540", "25", "Brakes", "2024/02/22", "brake_cable.jpg");
        assertEquals("Success", ProductManager.delete("P911"));
        assertEquals("NotFound", ProductManager.delete("P911"));
    }

    @Test
    @DisplayName("updates a product's fields and saves the changes")
    void updateChangesFieldsAndPersistsThem() {
        try {
            ProductManager.addProduct("P912", "Indicator Lamp 12V", "Philips", "310", "20", "Electrical", "2024/02/25", "indicator_lamp.jpg");

            assertEquals("Success", ProductManager.update("P912", "Indicator Lamp LED", "Osram", "450", "15", "Electrical", "2024/03/01", "indicator_led.jpg"));

            Products[] reloaded = ProductManager.loadFromNewFile();
            Products updated = null;
            for (Products p : reloaded) {
                if (p.getCode().equals("P912")) {
                    updated = p;
                    break;
                }
            }
            assertNotNull(updated);
            assertEquals("Indicator Lamp LED", updated.getName());
            assertEquals(450.0, updated.getPrice());

            assertEquals("NotFound", ProductManager.update("P890", "Ghost Item", "Ghost", "5", "5", "Misc", "2024/01/01", "null"));
        } finally {
            ProductManager.delete("P912");
        }
    }
}
