package Cleaner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFileManager {
    boolean appendFlag;
    boolean readFlag;
    boolean writeFlag;

    //Appending to a text file
    public void append(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter("/Data/"+textfile, true);
            writer.write(dataObjString);
            writer.close();
            this.appendFlag = true;

        } catch (IOException e) {
            this.appendFlag = false;
        }
    }

    //Reading a Text File
    public String[] read(String textfile){

        try {
            int count = 0;
            File file = new File("/Data/"+textfile);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                scanner.nextLine();
                count++;
            }
            scanner.close();
            String[] items = new String[count];

            Scanner scanner1 = new Scanner(file);

            for (int i=0 ; i<count ; i++){
                String line = scanner1.nextLine();
                items[i] = line;
            }
            scanner1.close();
            this.readFlag = true;
            return items;

        } catch (FileNotFoundException e) {
            this.readFlag = false;
            return null;
        }
    }

    //Overwritting to a text file
    public void write(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter("/Data/"+textfile);
            writer.write(dataObjString);
            writer.close();
            this.writeFlag = true;

        } catch (IOException e) {
            this.writeFlag = false;
        }
    }

    public boolean getAppendFlag(){
        return appendFlag;
    }

    public boolean getReadFlag(){
        return readFlag;
    }

    public boolean getWriteFlag(){
        return writeFlag;
    }

}