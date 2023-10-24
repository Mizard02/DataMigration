import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FileManager {

    private Map<String, String> sqlToJava = new HashMap<>();
    private String FileIn;
    private String FileOut;

    public static ArrayList<String> nomiCampi = new ArrayList<String>();
    public static ArrayList<Integer> lunghezza = new ArrayList<Integer>();
    public static ArrayList<String> tipo = new ArrayList<String>();

    public FileManager(){
        sqlToJava.put("Integer", "INT");
        sqlToJava.put("Date", "DATE");
        sqlToJava.put("String", "VARCHAR");
        sqlToJava.put("Boolean", "BOOLEAN");
        sqlToJava.put("Long", "BIGINT");
    }

    public void leggiFileInput(String FI){
        try {
            BufferedReader lettore = new BufferedReader(new FileReader(FI));
            String riga = lettore.readLine(), token;
            StringTokenizer tokenizer;

            while ((riga = lettore.readLine()) != null) {
                tokenizer = new StringTokenizer(riga, ";");
                for (int i = 0; i < 6; i++) {
                    token = tokenizer.nextToken();
                    if (i == 0)
                        nomiCampi.add(token);
                    else if (i == 3)
                        lunghezza.add(Integer.valueOf(token));
                    else if (i == 4)
                        tipo.add(sqlToJava.get(token));
                }
            }
            lettore.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void generaTXT(String FO, ResultSet result){
        try {
            ResultSetMetaData metaData = result.getMetaData();
            int numColumns = metaData.getColumnCount();

            BufferedWriter scrittore = new BufferedWriter(new FileWriter(FO));

            while (result.next()) {
                // Itera attraverso tutte le colonne e stampa i dati
                for (int i = 1; i <= numColumns; i++) {
                    int n = 0;
                    String campo = result.getString(i);
                    if (campo == null)
                        campo = " ";
                    if (campo.length() != lunghezza.get(i - 1))
                        n = lunghezza.get(i - 1) - campo.length();
                    for (int j = 0; j < n; j++)
                        campo += " ";
                    scrittore.write(campo);
                }
                scrittore.newLine();
            }
            scrittore.close();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
