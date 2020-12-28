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
    static JMenuItem stockOut, stockIn;

    //table
    static JTable table;

    public static void main(String[] args) {
        frame = new JFrame("NOXON spare parts management");

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
        partOperationMenu.add(stockOut);
        partOperationMenu.add(stockIn);


        // add menubar to frame
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(partOperationMenu);
        frame.setJMenuBar(menuBar);

        //table
        String[] columnNames = {"Name", "型号", "零件号", "日期", "出库", "入库", "数量", "入库采购价格", "出库价格"};
        Object[][] data = {
                {"Belt", "A123", "A1234", "2020-12-31", 0, 1, 1, 1000, 2000}
        };
        table = new JTable(data, columnNames);
        JScrollPane sp1 = new JScrollPane(table);
        frame.add(sp1);


        frame.pack();
//        frame.setSize(1920, 1040);
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}
