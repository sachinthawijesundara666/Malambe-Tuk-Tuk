package Features;

import Model.Dealers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DealerManagerTest {

    @Test
    @DisplayName("loads all the dealers from the real dealer file")
    void load() {
        Dealers[] dealers = DealerManager.load();

        assertNotNull(dealers);
        assertEquals(8, dealers.length);
        assertEquals("D101", dealers[0].getCode());
        assertEquals("Sunil Motors", dealers[0].getName());
        assertEquals("null", dealers[2].getContact());
    }

    @Test
    @DisplayName("picks 4 different dealers at random")
    void dealerRandomSelect() {
        Dealers[] selected = DealerManager.DealerRandomSelect();

        assertNotNull(selected);
        assertEquals(4, selected.length);

        HashSet<String> codes = new HashSet<>();
        for (Dealers dealer : selected) {
            codes.add(dealer.getCode());
        }
        assertEquals(4, codes.size());
    }
}
