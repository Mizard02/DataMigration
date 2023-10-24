import java.sql.*;

public class DBManager {

    private String url, username, password;

    private String insertQuery = "INSERT INTO provaui (id_object, tipo_atto, data_atto, oggetto, ufficio, ufficio_firma, autore, data_esecutivita, ie, num_generale, anno_num_generale, num_settore, anno_num_settore, cod_settore, data_pubb_ini, data_pubb_fin, ripubblicazione, data_ripubb_ini, data_ripubb_fin, ratifica, num_verb_ratificato, data_verb_ratificato, note, data_annullamento, motivo_annullamento, destinatari, ordinanza_sindaco, data_adozione, data_tx_albo, data_tx_capigruppo, data_tx_prefettura, data_tx_difensore_civico, data_tx_oreco, num_protocollo, data_protocollo, data_notifica, data_scadenza, sospensione, data_sospensione, giorni_sospensione, proroga, data_proroga, gg_proroga, nuova_scadenza, tipo_discussione)\n" +
            "VALUES (1, \t\t2, \t'2023-10-06', 'Oggetto di esempio', 'Ufficio A', 'Ufficio Firma', 'Autore 1', '2023-10-07', true, 12345, \t\t2023, \t\t\t\t9876, \t\t\t2022, \t\t\t'ABC', '2023-10-08', '2023-10-10', \t\t\tfalse, \t\t\tnull, \t\t\tnull, \t\t\ttrue, \t\t54321, \t\t\t'2023-10-09', 'Note di esempio', '2023-10-11', 'Motivo di annullamento', 'Destinatari di esempio', false, '2023-10-12', '2023-10-13', '2023-10-14', '2023-10-15', \t\t\t\t'2023-10-16',      '2023-10-17', \t77777, \t\t\t'2023-10-17', '2023-10-18', '2023-10-19', \t\ttrue, \t\t'2023-10-20', \t\t\t30, \t\tfalse, '2023-09-21', \t\t0, \t\t'2023-10-21', 'Discussione di esempio');\n" +
            "\n" +
            "INSERT INTO provaui (id_object, tipo_atto, data_atto, oggetto, ufficio)\n" +
            "VALUES (2, 3, '2023-10-06', 'Esempio di oggetto', 'Ufficio A');\n" +
            "\n" +
            "INSERT INTO provaui (id_object, tipo_atto, data_atto, oggetto, ufficio)\n" +
            "VALUES (3, 4, '2023-10-07', 'Un altro oggetto', 'Ufficio B');\n";
    private Connection connection;

    public DBManager(String url, String username, String p) throws SQLException {
        this.url = url;
        this.username = username;
        password=p;

        connection = DriverManager.getConnection(url, username, p);
    }

    public void creaTabella(String nomeTabella) throws SQLException {
        String query;
        Statement executor;

        executor = connection.createStatement();
            query = "CREATE TABLE " + nomeTabella + " ()";
            executor.execute(query);

            for(int i=0;i<FileManager.nomiCampi.size();i++){
                query = "ALTER TABLE " + nomeTabella +
                        " ADD " + FileManager.nomiCampi.get(i) + " " +
                        FileManager.tipo.get(i);
                if(FileManager.nomiCampi.get(i).contains("id"))
                    query = query + " PRIMARY KEY;";
                else if(FileManager.tipo.get(i) == "VARCHAR")
                    query = query + "(" + FileManager.lunghezza.get(i) + ");";
                //System.out.println(query);
                executor.executeUpdate(query);
            }

            executor.execute(insertQuery);

            executor.close();
    }

    public ResultSet estraiDati(String nomeTabella) throws SQLException {
        String query = "SELECT * FROM " + nomeTabella;
        Statement executor = connection.createStatement();
        return executor.executeQuery(query);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
