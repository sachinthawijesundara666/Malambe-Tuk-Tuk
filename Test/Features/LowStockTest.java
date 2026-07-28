package Features;

import Model.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LowStockTest {

    @Test
    @DisplayName("counts a quantity below the threshold as low stock")
    void quantityBelowThresholdIsLowStock() {
        Products pistonRing = new Products("P021", "Piston Ring Set STD", "Bajaj", 780.0, 2, "Engine", "2024/01/10", "piston_ring.jpg", 8);

        assertTrue(LowStock.isLowStock(pistonRing));
    }

    @Test
    @DisplayName("doesn't count a quantity equal to the threshold as low stock")
    void quantityEqualToThresholdIsNotLowStock() {
        Products wheelBearing = new Products("P022", "Wheel Bearing 6204", "NTN", 980.0, 15, "Engine", "2024/01/10", "wheel_bearing.jpg", 15);

        assertFalse(LowStock.isLowStock(wheelBearing));
    }

    @Test
    @DisplayName("rejects a threshold of zero or below")
    void newThresholdIsRejectedWhenZeroOrBelow() {
        Products drumSpring = new Products("P913", "Drum Brake Spring", "Bajaj", 260.0, 30, "Brakes", "2024/01/10", "drum_spring.jpg", 10);

        assertEquals("ThresholdError", LowStock.setLowStockThreshold(drumSpring, 0));
    }

    @Test
    @DisplayName("saves a new threshold and reloads the same value from disk")
    void newThresholdIsSavedAndReloadableFromDisk() {
        assertEquals("Success", ProductManager.addProduct("P913", "Drum Brake Spring", "Bajaj", "260", "30", "Brakes", "2024/01/10", "drum_spring.jpg"));
        try {
            Products[] products = ProductManager.loadFromNewFile();
            Products drumSpring = null;
            for (Products p : products) {
                if (p.getCode().equals("P913")) {
                    drumSpring = p;
                    break;
                }
            }
            assertNotNull(drumSpring);

            assertEquals("Success", LowStock.setLowStockThreshold(drumSpring, 4));

            Products[] reloaded = ProductManager.loadFromNewFile();
            Products updated = null;
            for (Products p : reloaded) {
                if (p.getCode().equals("P913")) {
                    updated = p;
                    break;
                }
            }
            assertNotNull(updated);
            assertEquals(4, updated.getThreshold());
        } finally {
            ProductManager.delete("P913");
        }
    }
}
