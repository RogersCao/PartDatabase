import Database.h2;
import Obj.Category;
import Obj.Customer;
import Table.partCategory;

import javax.swing.*;
import java.util.List;

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
    // h2 data
    static List<Category> categoryList;
    static List<Customer> customerList;


    public static void main(String[] args) {
        frame = new JFrame("NOXON spare parts management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //MENU
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
        //END OF MENU

        //h2 db connection
        h2 h2 = new h2();
        try {
            h2.connection();
            h2.statement();
            h2.createTable();

            categoryList = h2.queryCategoryList();
            customerList = h2.queryCustomerList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("场面一度十分尴尬");
        }

        // add tab pane, each tab is a category
        JTabbedPane tp = new JTabbedPane();
        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList,customerList).sp);
        }

        frame.add(tp);
        frame.pack();
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}
