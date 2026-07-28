package Features;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class validatorTest {

    @Test
    void dateValidator() {
        assertTrue(validator.dateValidator("2024/07/18"));
        assertFalse(validator.dateValidator("2024/02/30"));
        assertFalse(validator.dateValidator("18-07-2024"));
    }

    @Test
    void codeValidator() {
        assertTrue(validator.codeValidator("P027"));
        assertFalse(validator.codeValidator("T027"));
        assertFalse(validator.codeValidator("P27"));
    }

    @Test
    void priceVal() {
        assertEquals(1875.50, validator.priceVal("1875.50"));
        assertNull(validator.priceVal("several thousand"));
    }

    @Test
    void quantityVal() {
        assertEquals(24, validator.quantityVal("24"));
        assertNull(validator.quantityVal("a dozen"));
    }
}
