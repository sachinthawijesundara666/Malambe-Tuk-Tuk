package Features;

public class Dealers {
    String code;
    String name;
    String contact;
    String location;

    public Dealers(String code, String name, String contact, String location){
        this.code = code;
        this.name = name;
        this.contact = contact;
        this.location = location;
    }

    public static Dealers [] read(){
        TextFileManager textFileManager = new TextFileManager();
        String[] newlines = textFileManager.read("dealers_legacy.txt");

        if (newlines != null) {

            Cleaner cleaner = new Cleaner();
            String[][] cleaned = cleaner.clean(newlines);
            Dealers[] dealerlist = new Dealers[cleaned.length];

            for (int i = 0 ; i < cleaned.length ; i++){
                dealerlist[i] = new Dealers(
                        cleaned[i][0],
                        cleaned[i][1],
                        cleaned[i][2],
                        cleaned[i][3]
                );
            }
            return dealerlist;

        }else return null;
    }
}
