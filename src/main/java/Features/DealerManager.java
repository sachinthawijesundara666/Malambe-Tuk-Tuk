package Features;

import Cleaner.Cleaner;
import Cleaner.TextFileManager;
import Model.Dealers;
import java.util.Random;

public class DealerManager {

    public static Dealers[] load() {

        Dealers[] dealerlist;
        TextFileManager textFileManager = new TextFileManager();

        String[] newlines = textFileManager.read("dealers_legacy.txt");
        if (!textFileManager.getReadFlag()) {
            return null;
        }

        Cleaner cleaner = new Cleaner();
        String[][] cleaned = cleaner.clean(newlines);
        dealerlist = new Dealers[cleaned.length];

        for (int i = 0; i < cleaned.length; i++) {
            try {
                dealerlist[i] = new Dealers(
                        cleaned[i][0],
                        cleaned[i][1],
                        cleaned[i][2],
                        cleaned[i][3]

                );
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                return null;
            }
        }
        return dealerlist;
    }

    public static Dealers[] DealerRandomSelect(){
        Dealers[] dealers = load();
        Random random = new Random();

        if (dealers == null || dealers.length < 4){
            return null;
        }

        for (int i = 0 ; i<dealers.length; i++){
            int j = random.nextInt(dealers.length);

            Dealers temp = dealers[j];

            dealers[j] = dealers[i];

            dealers[i] = temp;
        }

        Dealers[] SelectedDealers = new Dealers[4];

        for (int i = 0; i<4;i++){
            SelectedDealers[i] = dealers[i];
        }
        return SelectedDealers;
    }
}
