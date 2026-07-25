package Cleaner;

import java.util.ArrayList;
import java.util.List;

public class Cleaner {

    public String[][] clean(String[] rawLines) {
        List<String[]> cleanedRows = new ArrayList<>();

        for (String line : rawLines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            line = line.replaceAll("([A-Za-z]+ \\d{1,2}), (\\d{4})", "$1#$2");
            line = line.replaceAll("(\\d{1,2}) [A-Za-z]+, (\\d{4})", "$1#$2");

            String[] parts = line.split("[,;|]");

            int columns = Math.max(8, parts.length);
            String[] row = new String[columns];

            for (int i = 0; i < columns; i++) {
                if (i < parts.length) {
                    String value = parts[i].trim().replace("#", ",");

                    value = value.replaceAll("(?i)^rs\\.?\\s*", "");

                    if (value.isEmpty()) {
                        value = "null";
                    }
                    row[i] = value;
                } else {
                    row[i] = "null";
                }
            }
            row[6] = cleanDate(row[6]);
            cleanedRows.add(row);
        }
        return cleanedRows.toArray(new String[0][]);
    }

    private String cleanDate(String date) {

        if (date.equals("null")) {
            return "null";
        }

        date = date.replace(",", " ");
        String[] parts = date.split("[ /-]");

        if (parts.length != 3) {
            return "null";
        }

        try {
            String day;
            String month;
            String year;

            if (parts[0].length() == 4) {
                year = parts[0];
                month = parts[1];
                day = parts[2];
            }

            else if (isMonth(parts[0])) {
                month = getMonth(parts[0]);
                day = parts[1];
                year = parts[2];
            }

            else if (isMonth(parts[1])) {
                day = parts[0];
                month = getMonth(parts[1]);
                year = parts[2];
            }

            else {
                day = parts[0];
                month = parts[1];
                year = parts[2];
            }
            return year + "/" + format(month) + "/" + format(day);

        } catch (Exception e) {
            return "null";
        }
    }


    private boolean isMonth(String value) {
        String m = value.toLowerCase();
        return m.startsWith("jan") ||
                m.startsWith("feb") ||
                m.startsWith("mar") ||
                m.startsWith("apr") ||
                m.startsWith("may") ||
                m.startsWith("jun") ||
                m.startsWith("jul") ||
                m.startsWith("aug") ||
                m.startsWith("sep") ||
                m.startsWith("oct") ||
                m.startsWith("nov") ||
                m.startsWith("dec");
    }


    private String getMonth(String value) {
        String m = value.toLowerCase();
        if (m.startsWith("jan")) return "01";
        if (m.startsWith("feb")) return "02";
        if (m.startsWith("mar")) return "03";
        if (m.startsWith("apr")) return "04";
        if (m.startsWith("may")) return "05";
        if (m.startsWith("jun")) return "06";
        if (m.startsWith("jul")) return "07";
        if (m.startsWith("aug")) return "08";
        if (m.startsWith("sep")) return "09";
        if (m.startsWith("oct")) return "10";
        if (m.startsWith("nov")) return "11";
        if (m.startsWith("dec")) return "12";
        return "null";
    }

    private String format(String value) {
        if (value.length() == 1) {
            return "0" + value;
        }
        return value;
    }
}