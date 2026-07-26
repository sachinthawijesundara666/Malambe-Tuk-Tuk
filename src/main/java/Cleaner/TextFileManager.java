package Cleaner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextFileManager {
    boolean appendFlag;
    boolean readFlag;
    boolean writeFlag;

    private File loadFile(String textfile) throws IOException {
        File file = new File("src/main/resources/Data/" + textfile);
        if (!file.exists()) {
            throw new IOException("src/main/resources/Data/" + textfile + " not found");
        }
        return file;
    }

    public void append(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter(loadFile(textfile), true);
            writer.write(dataObjString);
            writer.close();
            this.appendFlag = true;
        } catch (IOException e) {
            this.appendFlag = false;
        }
    }

    public String[] read(String textfile){
        try {
            File file = loadFile(textfile);
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
            FileWriter writer = new FileWriter(loadFile(textfile));
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