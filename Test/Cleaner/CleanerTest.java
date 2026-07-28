package Cleaner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CleanerTest {

    private final Cleaner cleaner = new Cleaner();

    @Test
    @DisplayName("strips the Rs. price prefix and converts a written-out date")
    void stripsPricePrefixAndHumanisedDate() {
        String[] raw = {
                "P011, Chain Sprocket Set 428H, Diamond, Rs. 3200.00, 22, Engine, Nov 3, 2023, sprocket428.jpg"
        };

        String[] row = cleaner.clean(raw)[0];

        assertEquals("P011", row[0]);
        assertEquals("Chain Sprocket Set 428H", row[1]);
        assertEquals("Diamond", row[2]);
        assertEquals("3200.00", row[3]);
        assertEquals("22", row[4]);
        assertEquals("Engine", row[5]);
        assertEquals("2023/11/03", row[6]);
        assertEquals("sprocket428.jpg", row[7]);
    }

    @Test
    @DisplayName("skips blank and whitespace-only lines")
    void skipsBlankAndWhitespaceOnlyLines() {
        String[] raw = {
                "",
                "   ",
                "P012, Rear Brake Shoe, Bajaj, 1400, 16, Brakes, 2024/03/10, shoe_rear.jpg"
        };

        String[][] result = cleaner.clean(raw);

        assertEquals(1, result.length);
        assertEquals("P012", result[0][0]);
    }

    @Test
    @DisplayName("reads a row that uses commas")
    void parsesCommaDelimitedRow() {
        String[] raw = {"P012, Rear Brake Shoe, Bajaj, 1400, 16, Brakes, 2024/03/10, shoe_rear.jpg"};

        String[] row = cleaner.clean(raw)[0];

        assertEquals("Rear Brake Shoe", row[1]);
        assertEquals("Bajaj", row[2]);
        assertEquals("2024/03/10", row[6]);
    }

    @Test
    @DisplayName("reads a row that uses pipes, with a numeric date")
    void parsesPipeDelimitedRowWithNumericDate() {
        String[] raw = {"P013|CDI Unit Digital|Bajaj|3100|9|Electrical|22/04/2024|cdi_unit.png"};

        String[] row = cleaner.clean(raw)[0];

        assertEquals("CDI Unit Digital", row[1]);
        assertEquals("3100", row[3]);
        assertEquals("2024/04/22", row[6]);
    }

    @Test
    @DisplayName("reads a row that uses semicolons, with an ISO-style date")
    void parsesSemicolonDelimitedRowWithIsoDate() {
        String[] raw = {"P014; Fuel Filter Inline; Malkey; 350; 40; Engine; 2023-12-01; fuelfilter.jpg"};

        String[] row = cleaner.clean(raw)[0];

        assertEquals("Fuel Filter Inline", row[1]);
        assertEquals("Malkey", row[2]);
        assertEquals("2023/12/01", row[6]);
    }

    @Test
    @DisplayName("turns a blank brand into null")
    void blankBrandFieldBecomesNull() {
        String[] raw = {"P015, Handlebar Grip Rubber, , 450, 60, Bodywork, 2024/02/14, grip_rubber.jpg"};

        String[] row = cleaner.clean(raw)[0];

        assertEquals("null", row[2]);
    }

    @Test
    @DisplayName("turns a blank picture into null")
    void blankPictureFieldBecomesNull() {
        String[] raw = {"P016, Wheel Bearing 6204, NTN, 980, 25, Engine, 2024/01/20, "};

        String[] row = cleaner.clean(raw)[0];

        assertEquals("null", row[7]);
    }
}
