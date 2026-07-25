package Features;

import Cleaner.TextFileManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    public static void log(String action){
        TextFileManager textFileManager = new TextFileManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String line = timestamp + " - " + action + "\n";
        textFileManager.append("audit_log.txt", line);
    }
}