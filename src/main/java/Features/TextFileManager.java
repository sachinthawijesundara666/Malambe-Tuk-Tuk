package Features;

import java.io.FileWriter;
import java.io.IOException;

public class TextFileManager {
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

    public void read(String textfile){
    }

}
