import javax.swing.*;

public class UI {
    static JFrame frame;

    static JMenuBar menuBar;

    // file Menu
    static JMenu fileMenu;
    static JMenuItem m1, m2;
    // search menu
    static JMenu searchMenu;
    static JMenuItem f1, f2;
    // part Operation Menu
    static JMenu partOperationMenu;
    static JMenuItem stockOut, stockIn, update;

    public static void main(String[] args) {
        frame = new JFrame("NOXON spare parts management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        //file menu
        fileMenu = new JMenu("File");
        m1 = new JMenuItem("MenuItem1");
        m2 = new JMenuItem("MenuItem2");
        fileMenu.add(m1);
        fileMenu.add(m2);
        //search menu
        searchMenu = new JMenu("Search");
        f1 = new JMenuItem("MenuItem1");
        f2 = new JMenuItem("MenuItem2");
        searchMenu.add(f1);
        searchMenu.add(f2);
        // part Operation Menu
        partOperationMenu = new JMenu("Part Operation");
        stockOut = new JMenuItem("出库");
        stockIn = new JMenuItem("入库");
        update = new JMenuItem("更新信息");
        partOperationMenu.add(stockOut);
        partOperationMenu.add(stockIn);
        partOperationMenu.add(update);


        // add menubar to frame
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(partOperationMenu);
        frame.setJMenuBar(menuBar);

        //table
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(new JLabel("Bearing"));
        panel.add(generateTable());
        panel.add(generateTable());
        panel.add(generateTable());

        JScrollPane spTotal = new JScrollPane(panel);
        frame.add(spTotal);

        frame.pack();
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }

    private static JScrollPane generateTable() {
        Table t = new Table();
        return new JScrollPane(t.table);
    }
}
