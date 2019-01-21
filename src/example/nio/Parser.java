package example.nio;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class Parser extends JFrame {

    public static final FilenameFilter FILTR_ZIP = (dir, name) -> {
        String lowercaseName = name.toLowerCase();
        return lowercaseName.endsWith(".zip");
    };

    private static final FileFilter jfilechooserFilter =
            new FileNameExtensionFilter("Zip", "*.zip");

    private JList<File> list = new JList<>();
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JButton buttonStart;
    private JButton buttonExit;

    private JMenuBar menuBar = new JMenuBar();
    private JFileChooser fileChooser = new JFileChooser();
    private DefaultListModel<File> listModel = new DefaultListModel<>();

    private enum ACTION {
        ADD("Dodaj"),
        DELETE("Usuń"),
        START("Start"),
        CLOSE("Koniec");

        private String name;

        ACTION(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public Parser(){
        setComponents();
    }

    public static void main(String[] args) {

        new Parser();

    }

    private void setComponents(){
        this.setBounds(300,200, 400,300);
        this.setVisible(true);
        this.setTitle("Parser");
        this.setJMenuBar(menuBar);

        List<File> filesList = Arrays.asList(new File(System.getProperty("user.dir")).listFiles(FILTR_ZIP));
        listModel.addAll(filesList);

        Action actionAdd = new Akcja(
                ACTION.ADD,
                "Dodaj plik do listy",
                "ctrl A",
                new ImageIcon("dodaj.png")
        );
        Action actionDelete = new Akcja(
                ACTION.DELETE,
                "Usuń plik z listy",
                "ctrl D",
                new ImageIcon("usun.png"));
        Action actionStart = new Akcja(
                ACTION.START,
                "Rozpocznij pracę",
                "ctrl Z");
        Action actionExit = new Akcja(
                ACTION.CLOSE,
                "Zamknij program",
                "alt F4");


        buttonAdd = new JButton(actionAdd);
        buttonDelete = new JButton(actionDelete);
        buttonStart = new JButton(actionStart);
        buttonExit = new JButton(actionExit);

        buttonAdd.setName("Dodaj");
        buttonDelete.setName("Usuń");
        buttonStart.setName("Start");
        buttonExit.setName("Koniec");

        JMenu menuFile = menuBar.add(new JMenu("Pliki"));

        menuFile.add(actionAdd);
        menuFile.add(actionDelete);
        menuFile.add(actionStart);
        menuFile.addSeparator();
        menuFile.add(actionExit);

        JScrollPane scrollPane = new JScrollPane(list);
        list.setBorder(BorderFactory.createEtchedBorder());
        list.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if(value != null){
                    value = ((File)value).getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        list.setModel(listModel);
        GroupLayout layout = new GroupLayout(this.getContentPane());


        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(scrollPane, 100, 150, Short.MAX_VALUE)
                        .addContainerGap(0, Short.MAX_VALUE)
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(buttonAdd)
                                        .addComponent(buttonDelete)
                                        .addComponent(buttonStart)
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addComponent(buttonAdd)
                                        .addComponent(buttonDelete)
                                        .addGap(5, 40, Short.MAX_VALUE)
                                        .addComponent(buttonStart)
                        )
        );

        this.getContentPane().setLayout(layout);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
    }

    private class Akcja extends AbstractAction{

        private ACTION action;

        public Akcja(ACTION action, String desc, String keyboardShortcut){
            this.action = action;
            this.putValue(Action.NAME, action.toString());
            this.putValue(Action.SHORT_DESCRIPTION, desc);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyboardShortcut));
        }

        public Akcja(ACTION action, String desc, String keyboardShortcut, Icon icon){
            this(action, desc, keyboardShortcut);
            this.putValue(Action.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (action){
                case ADD:
                    System.out.println("Dodawanie pliku");
                    addEntry();
                    break;

                case DELETE:
                    deleteEntry();
                    System.out.println("Usuwanie pliku");
                    break;

                case START:
                    System.out.println("Działanie parsera");
                    break;

                case CLOSE:
                    System.exit(0);
                    break;

            }
        }

        private void addEntry(){

            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(jfilechooserFilter);
            fileChooser.setMultiSelectionEnabled(true);

            int tmp = fileChooser.showDialog(rootPane, "Dodaj");

            if(tmp == JFileChooser.APPROVE_OPTION){
                File[] files = fileChooser.getSelectedFiles();

                for(File file: files){
                    if(!listModel.contains(file)) {
                        listModel.addElement(file);
                    }
                }
            }

        }

        private void deleteEntry(){
            int[] indices = list.getSelectedIndices();
            for(int i = 0; i < indices.length; i++){
                listModel.remove(indices[i] - i);
            }
        }

    }
}
