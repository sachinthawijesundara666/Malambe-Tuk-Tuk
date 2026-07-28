package Features;

import Model.CartItem;
import Model.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartManagerTest {

    @BeforeEach
    void clearBasket() {
        CartManager.getBasket().clear();
    }

    @Test
    void addingSameProductTwiceIsRejectedAsDuplicate() {
        Products cable = new Products("P040", "Speedometer Cable", "Local", 480.0, 14, "Engine", "2024/01/10", "speedo_cable.jpg", 5);

        assertEquals("AddedToCart", CartManager.AddCart(cable, 2));
        assertEquals("Duplicate", CartManager.AddCart(cable, 1));
    }

    @Test
    void removingCartItemEmptiesBasket() {
        Products plateLight = new Products("P041", "Number Plate Light", "Local", 220.0, 20, "Electrical", "2024/01/10", "plate_light.jpg", 5);
        CartManager.AddCart(plateLight, 1);

        CartItem inBasket = CartManager.getBasket().get(0);
        CartManager.RemoveCart(inBasket);

        assertEquals(0, CartManager.getBasket().size());
    }

    @Test
    void basketReflectsAddedItem() {
        Products kickLever = new Products("P042", "Kick Start Lever", "Bajaj", 650.0, 9, "Engine", "2024/01/10", "kick_lever.jpg", 5);
        CartManager.AddCart(kickLever, 1);

        assertEquals(1, CartManager.getBasket().size());
        assertEquals("P042", CartManager.getBasket().get(0).getCode());
    }

    @Test
    void quantityCanBeRaisedWithinStockButNotBeyondIt() {
        Products airFilter = new Products("P043", "Air Filter Foam", "Uni Filter", 900.0, 10, "Engine", "2024/01/10", "air_filter.jpg", 5);
        CartManager.AddCart(airFilter, 2);
        CartItem inBasket = CartManager.getBasket().get(0);

        assertEquals("QuantitySet", CartManager.setQuantity(inBasket, airFilter, 6));
        assertEquals(6, inBasket.getQuantity());
        assertEquals("QuantityError", CartManager.setQuantity(inBasket, airFilter, 40));
    }

    // ---- TC-CD: Cart discount rules ----

    @Test
    void bulkDiscountAppliesAtQuantityThree() {
        Products tyreTube = new Products("P030", "Tyre Tube 3.00-10", "CEAT", 850.0, 20, "Bodywork", "2024/01/05", "tyre_tube.jpg", 5);
        CartManager.AddCart(tyreTube, 3);

        String result = CartManager.Total();

        assertTrue(result.contains("Bulk Discount Applied"));
        assertTrue(result.contains("Total: Rs.2422.50"));
    }

    @Test
    void noDiscountBelowBulkThreshold() {
        Products cableTies = new Products("P031", "Cable Tie Pack (50pcs)", "Local", 120.0, 30, "Bodywork", "2024/01/05", "cable_ties.jpg", 5);
        CartManager.AddCart(cableTies, 2);

        String result = CartManager.Total();

        assertTrue(result.contains("No Discounts Applied"));
        assertTrue(result.contains("Total: Rs.240.00"));
    }

    @Test
    void synergyDiscountAppliesForEngineAndElectricalTogether() {
        Products pistonKit = new Products("P032", "Piston Kit 4-Stroke", "Bajaj", 500.0, 10, "engine", "2024/01/05", "piston_kit.jpg", 5);
        Products ignitionCoil = new Products("P033", "Ignition Coil Unit", "Bajaj", 500.0, 10, "electrical", "2024/01/05", "ignition_coil.jpg", 5);
        CartManager.AddCart(pistonKit, 1);
        CartManager.AddCart(ignitionCoil, 1);

        String result = CartManager.Total();

        assertTrue(result.contains("Synergy Discount Applied"));
        assertTrue(result.contains("Total: Rs.900.00"));
    }

    @Test
    void synergyDiscountDoesNotApplyWhenCategoryCaseDiffers() {
        // Documents a real limitation: CartManager.Total() compares category with a
        // case-sensitive .equals("engine")/.equals("electrical"), so the capitalised
        // category text actually stored in Inventory.txt ("Engine", "Electrical")
        // never triggers the synergy discount.
        Products radiatorFan = new Products("P034", "Radiator Cooling Fan", "Bajaj", 300.0, 10, "Engine", "2024/01/05", "radiator_fan.jpg", 5);
        Products batteryClamp = new Products("P035", "Battery Terminal Clamp", "Local", 300.0, 10, "Electrical", "2024/01/05", "battery_clamp.jpg", 5);
        CartManager.AddCart(radiatorFan, 1);
        CartManager.AddCart(batteryClamp, 1);

        String result = CartManager.Total();

        assertTrue(result.contains("No Discounts Applied"));
        assertTrue(result.contains("Total: Rs.600.00"));
    }

    // ---- TC-SD: Stock deduction ----

    @Test
    void checkoutOnEmptyBasketIsRejected() {
        assertEquals("EmptyBasket", CartManager.proceed());
    }

    @Test
    void checkoutDeductsPurchasedQuantityFromLiveStock() {
        assertEquals("Success", ProductManager.addProduct("P914", "Clutch Plate Kit", "Bajaj", "1450", "12", "Engine", "2024/03/01", "clutch_plate.jpg"));
        try {
            Products[] products = ProductManager.loadFromNewFile();
            Products clutchPlate = null;
            for (Products p : products) {
                if (p.getCode().equals("P914")) {
                    clutchPlate = p;
                    break;
                }
            }
            assertNotNull(clutchPlate);

            assertEquals("AddedToCart", CartManager.AddCart(clutchPlate, 4));
            assertEquals("Success", CartManager.proceed());

            Products[] reloaded = ProductManager.loadFromNewFile();
            Products updated = null;
            for (Products p : reloaded) {
                if (p.getCode().equals("P914")) {
                    updated = p;
                    break;
                }
            }
            assertNotNull(updated);
            assertEquals(8, updated.getQuantity());
        } finally {
            ProductManager.delete("P914");
        }
    }
}
