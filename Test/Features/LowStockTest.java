package Features;

import Model.Products;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LowStockTest {

    @Test
    void belowThreshold() {
        Products pistonRing = new Products("P021", "Piston Ring Set STD", "Bajaj", 780.0, 2, "Engine", "2024/01/10", "piston_ring.jpg", 8);

        assertTrue(LowStock.isLowStock(pistonRing));
    }

    @Test
    void atThreshold() {
        Products wheelBearing = new Products("P022", "Wheel Bearing 6204", "NTN", 980.0, 15, "Engine", "2024/01/10", "wheel_bearing.jpg", 15);

        assertFalse(LowStock.isLowStock(wheelBearing));
    }

    @Test
    void invalidThreshold() {
        Products drumSpring = new Products("P913", "Drum Brake Spring", "Bajaj", 260.0, 30, "Brakes", "2024/01/10", "drum_spring.jpg", 10);

        assertEquals("ThresholdError", LowStock.setLowStockThreshold(drumSpring, 0));
    }

    @Test
    void setThreshold() {
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
