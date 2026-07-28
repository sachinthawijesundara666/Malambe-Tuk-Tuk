package Features;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class validatorTest {

    @Test
    @DisplayName("accepts a real date and rejects an impossible or badly formatted one")
    void dateValidator() {
        assertTrue(validator.dateValidator("2024/07/18"));
        assertFalse(validator.dateValidator("2024/02/30"));
        assertFalse(validator.dateValidator("18-07-2024"));
    }

    @Test
    @DisplayName("accepts a correctly formatted code and rejects an incorrect one")
    void codeValidator() {
        assertTrue(validator.codeValidator("P027"));
        assertFalse(validator.codeValidator("T027"));
        assertFalse(validator.codeValidator("P27"));
    }

    @Test
    @DisplayName("reads a valid price and rejects text that isn't a number")
    void priceVal() {
        assertEquals(1875.50, validator.priceVal("1875.50"));
        assertNull(validator.priceVal("several thousand"));
    }

    @Test
    @DisplayName("reads a valid quantity and rejects text that isn't a number")
    void quantityVal() {
        assertEquals(24, validator.quantityVal("24"));
        assertNull(validator.quantityVal("a dozen"));
    }
}
