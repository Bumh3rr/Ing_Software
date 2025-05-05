package bumh3r.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar {

    //Menus
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    //Items del Menu Archivo
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;
    //Items del Menu Editar
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    //Items del Menu Ayuda
    private JMenuItem aboutItem;

    public MyMenuBar() {
        fileMenu = new JMenu("Archivo");
        editMenu = new JMenu("Editar");
        helpMenu = new JMenu("Ayuda");

        newItem = new JMenuItem("Nuevo");
        openItem = new JMenuItem("Abrir...");
        saveItem = new JMenuItem("Guardar");
        exitItem = new JMenuItem("Salir");

        exitItem.addActionListener((e) -> {
            System.exit(0);
        });

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator(); 
        fileMenu.add(exitItem);

        cutItem = new JMenuItem("Cortar");
        copyItem = new JMenuItem("Copiar");
        pasteItem = new JMenuItem("Pegar");

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        aboutItem = new JMenuItem("Acerca de");

        helpMenu.add(aboutItem);

        add(fileMenu);
        add(editMenu);
        add(helpMenu);
    }
}
