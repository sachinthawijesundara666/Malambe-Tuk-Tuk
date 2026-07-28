package Cleaner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextFileManagerTest {

    private static final String SCRATCH_FILE = "temp_stock_ledger.txt";

    @BeforeAll
    static void createScratchFile() throws IOException {
        File file = new File("src/main/resources/Data/" + SCRATCH_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @AfterAll
    static void deleteScratchFile() {
        new File("src/main/resources/Data/" + SCRATCH_FILE).delete();
    }

    @Test
    void write() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write(SCRATCH_FILE, "P916, Wiring Harness Kit, Bajaj\n");

        assertTrue(textFileManager.getWriteFlag());
        assertArrayEquals(new String[]{"P916, Wiring Harness Kit, Bajaj"}, textFileManager.read(SCRATCH_FILE));
    }

    @Test
    void append() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write(SCRATCH_FILE, "P916, Wiring Harness Kit, Bajaj\n");
        textFileManager.append(SCRATCH_FILE, "P917, Brake Fluid DOT4 500ml, Castrol\n");

        assertTrue(textFileManager.getAppendFlag());
        assertArrayEquals(
                new String[]{"P916, Wiring Harness Kit, Bajaj", "P917, Brake Fluid DOT4 500ml, Castrol"},
                textFileManager.read(SCRATCH_FILE)
        );
    }

    @Test
    void read() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write(SCRATCH_FILE, "P918, Fork Oil Seal Set, Kayaba\nP919, Chain Lube Spray, Motul\n");

        String[] lines = textFileManager.read(SCRATCH_FILE);
        assertArrayEquals(new String[]{"P918, Fork Oil Seal Set, Kayaba", "P919, Chain Lube Spray, Motul"}, lines);

        assertNull(textFileManager.read("supplier_returns_manifest.txt"));
    }

    @Test
    void getAppendFlag() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.append("supplier_returns_manifest.txt", "P920, Rim Tape 17in, Local");

        assertFalse(textFileManager.getAppendFlag());
    }

    @Test
    void getReadFlag() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write(SCRATCH_FILE, "P921, Grip Foam Pair, ProGrip\n");
        textFileManager.read(SCRATCH_FILE);

        assertTrue(textFileManager.getReadFlag());
    }

    @Test
    void getWriteFlag() {
        TextFileManager textFileManager = new TextFileManager();
        textFileManager.write(SCRATCH_FILE, "P922, Throttle Cable, Bajaj\n");

        assertTrue(textFileManager.getWriteFlag());
    }
}
