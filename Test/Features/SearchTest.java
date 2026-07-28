package Features;

import Model.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    private final Products[] catalogue = {
            new Products("P050", "Front Disc Brake Pad", "Bajaj", 1450.0, 3, "Brakes", "2024/01/10", "disc_pad.jpg", 10),
            new Products("P051", "Rear Drum Brake Shoe", "TVS", 980.0, 40, "Brakes", "2024/01/10", "drum_shoe.jpg", 10),
            new Products("P052", "LED Headlight Bulb H4", "Philips", 2200.0, 18, "Electrical", "2024/01/10", "headlight_led.jpg", 10)
    };

    @Test
    @DisplayName("matches a keyword against part of the name, regardless of case")
    void keywordMatchesPartialNameCaseInsensitively() {
        Products[] result = Search.search(catalogue, "disc", false, null, null, "All");

        assertEquals(1, result.length);
        assertEquals("P050", result[0].getCode());
    }

    @Test
    @DisplayName("returns only products from the selected category")
    void categoryFilterReturnsOnlyMatchingCategory() {
        Products[] result = Search.search(catalogue, "", false, null, null, "Brakes");

        assertEquals(2, result.length);
        assertEquals("P050", result[0].getCode());
        assertEquals("P051", result[1].getCode());
    }

    @Test
    @DisplayName("returns only items that are below their threshold")
    void lowStockFilterReturnsOnlyItemsBelowThreshold() {
        Products[] result = Search.search(catalogue, "", true, null, null, "All");

        assertEquals(1, result.length);
        assertEquals("P050", result[0].getCode());
    }

    @Test
    @DisplayName("returns only items priced within the given range")
    void priceRangeFilterReturnsOnlyItemsWithinRange() {
        Products[] result = Search.search(catalogue, "", false, 2000.0, 3000.0, "All");

        assertEquals(1, result.length);
        assertEquals("P052", result[0].getCode());
    }
}
