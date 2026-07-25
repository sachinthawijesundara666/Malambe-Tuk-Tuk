package Cleaner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class TextFileManager {
    boolean appendFlag;
    boolean readFlag;
    boolean writeFlag;

    private File resolveDataFile(String textfile) throws IOException {
        URL resource = TextFileManager.class.getResource("/Data/" + textfile);
        if (resource == null) {
            throw new IOException("/Data/" + textfile + " not found on classpath");
        }
        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public void append(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter(resolveDataFile(textfile), true);
            writer.write(dataObjString);
            writer.close();
            this.appendFlag = true;
        } catch (IOException e) {
            this.appendFlag = false;
        }
    }

    public String[] read(String textfile){
        try {
            File file = resolveDataFile(textfile);
            int count = 0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){ scanner.nextLine(); count++; }
            scanner.close();

            String[] items = new String[count];
            Scanner scanner1 = new Scanner(file);
            for (int i=0 ; i<count ; i++){ items[i] = scanner1.nextLine(); }
            scanner1.close();
            this.readFlag = true;
            return items;
        } catch (IOException e) {
            this.readFlag = false;
            return null;
        }
    }

    public void write(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter(resolveDataFile(textfile));
            writer.write(dataObjString);
            writer.close();
            this.writeFlag = true;
        } catch (IOException e) {
            this.writeFlag = false;
        }
    }

    public boolean getAppendFlag(){ return appendFlag; }
    public boolean getReadFlag(){ return readFlag; }
    public boolean getWriteFlag(){ return writeFlag; }
}