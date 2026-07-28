package Controller;

import Model.Products;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class InventoryControllerTest {

    @Test
    void bubbleSortOrdersProductsByCodeAscending() throws Exception {
        Products[] products = {
                new Products("P018", "Drive Belt CVT", "Bando", 1650.0, 8, "Engine", "2024/01/15", "drive_belt.jpg", 5),
                new Products("P011", "Chain Sprocket Set 428H", "Diamond", 3200.0, 22, "Engine", "2023/11/03", "sprocket428.jpg", 5),
                new Products("P015", "Handlebar Grip Rubber", "Local", 450.0, 60, "Bodywork", "2024/02/14", "grip_rubber.jpg", 5)
        };

        Method bubbleSortByCode = InventoryController.class.getDeclaredMethod("bubbleSortByCode", Products[].class);
        bubbleSortByCode.setAccessible(true);
        bubbleSortByCode.invoke(new InventoryController(), new Object[]{products});

        assertEquals("P011", products[0].getCode());
        assertEquals("P015", products[1].getCode());
        assertEquals("P018", products[2].getCode());
    }

    @Test
    void groupAndSortByCategoryKeepsEachCategoryContiguousAndSorted() throws Exception {
        Products[] products = {
                new Products("P018", "Drive Belt CVT", "Bando", 1650.0, 8, "Engine", "2024/01/15", "drive_belt.jpg", 5),
                new Products("P015", "Handlebar Grip Rubber", "Local", 450.0, 60, "Bodywork", "2024/02/14", "grip_rubber.jpg", 5),
                new Products("P011", "Chain Sprocket Set 428H", "Diamond", 3200.0, 22, "Engine", "2023/11/03", "sprocket428.jpg", 5),
                new Products("P019", "Seat Cover Vinyl", "Local", 1250.0, 17, "Bodywork", "2024/02/18", "seat_cover.jpg", 5)
        };

        Method groupAndSortByCategory = InventoryController.class.getDeclaredMethod("groupAndSortByCategory", Products[].class);
        groupAndSortByCategory.setAccessible(true);
        Products[] result = (Products[]) groupAndSortByCategory.invoke(new InventoryController(), new Object[]{products});

        // Engine appeared first in the input, so its (code-sorted) group comes first,
        // followed by the Bodywork group -- no interleaving between categories.
        assertEquals("P011", result[0].getCode());
        assertEquals("P018", result[1].getCode());
        assertEquals("P015", result[2].getCode());
        assertEquals("P019", result[3].getCode());
    }
}
