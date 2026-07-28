package Features;

import Cleaner.TextFileManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditLoggerTest {

    @Test
    @DisplayName("adds a timestamped line to the audit log")
    void log() {
        TextFileManager textFileManager = new TextFileManager();
        String[] before = textFileManager.read("audit_log.txt");
        int beforeCount = before == null ? 0 : before.length;

        AuditLogger.log("Month-End Stock Audit Completed");

        String[] after = textFileManager.read("audit_log.txt");
        assertEquals(beforeCount + 1, after.length);
        assertTrue(after[after.length - 1].matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} - Month-End Stock Audit Completed"));
    }
}
