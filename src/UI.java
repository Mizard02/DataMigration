import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UI extends JFrame {
    private JTextField textFieldFileIn, textFieldFolder;
    private JButton browseFileButton;
    private JButton browseFolderButton;

    private boolean stato = false;

    private String nomeTabella, percorsoFileIn, percorsoSalvataggio;

    public UI() {
        setTitle("ProjectMig");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Pannello principale
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel label1 = new JLabel("Seleziona file");
        // Campo di testo per inserire una stringa
        textFieldFileIn = new JTextField();
        // Pulsante per selezionare un file
        browseFileButton = new JButton("esplora📂");
        browseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        JLabel label2 = new JLabel("Inserisci nomeTabella");
        JTextField textFieldNT = new JTextField();

        JLabel label3 = new JLabel("Dove vuoi salvarlo ?");
        textFieldFolder = new JTextField();
        browseFolderButton = new JButton("esplora📂");
        browseFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });

        JButton esegui = new JButton("esegui");
        esegui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nomeTabella=textFieldNT.getText();
                inizioMigrazione();
            }
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(label1)
                                .addComponent(label2)
                                .addComponent(label3)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(textFieldFileIn)
                                .addComponent(textFieldNT)
                                .addComponent(textFieldFolder)
                                .addComponent(esegui)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(browseFileButton)
                                .addComponent(browseFolderButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1)
                                .addComponent(textFieldFileIn)
                                .addComponent(browseFileButton)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2)
                                .addComponent(textFieldNT)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(browseFolderButton)
                                .addComponent(textFieldFolder)
                        )
                        .addComponent(esegui)
        );


        add(panel);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("File di testo", "txt");
        //fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            percorsoFileIn = selectedFile.getAbsolutePath();
            textFieldFileIn.setText(percorsoFileIn);
        }
    }

    private void selectFolder() {
        JFileChooser folderChooser = new JFileChooser();
        //folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = folderChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            percorsoSalvataggio = selectedFolder.getAbsolutePath();
            textFieldFolder.setText(percorsoSalvataggio);
        }
    }

    private void inizioMigrazione(){
        try {

            FileManager fm = new FileManager();
            DBManager dbm = new DBManager(Main.url, Main.username, Main.password);

            fm.leggiFileInput(percorsoFileIn);
            dbm.creaTabella(nomeTabella);
            ResultSet result = dbm.estraiDati(nomeTabella);
            fm.generaTXT(percorsoSalvataggio, result);

            dbm.closeConnection();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        
    }
}

