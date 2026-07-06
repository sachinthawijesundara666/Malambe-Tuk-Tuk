package Features;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFileManager {

    //Appending to a text file
    public boolean append(String textfile, String dataObjString){
        try {
         FileWriter writer = new FileWriter("/Data/"+textfile, true);
         writer.write(dataObjString);
         writer.close();
         return true;

        } catch (IOException e) {
            return false;
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
            return items;

        } catch (FileNotFoundException e) {
            return null;
        }
    }

    //Overwritting to a text file
    public boolean write(String textfile, String dataObjString){
        try {
            FileWriter writer = new FileWriter("/Data/"+textfile);
            writer.write(dataObjString);
            writer.close();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

}
