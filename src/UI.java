import Database.h2;
import Obj.Category;
import Obj.Customer;
import Table.partCategory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    //newOp Menu
    static JMenu newOp;
    static JMenuItem newCategory, newPart;
    static String[] categoryOptions;
    // h2 data
    static List<Category> categoryList;
    static List<Customer> customerList;


    public static void main(String[] args) {
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
        // "new" Menu
        newOp = new JMenu("New");
        newCategory = new JMenuItem("New Category");
        newPart = new JMenuItem("New Part");
        newCategory.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter the name of category", null);
            int result = JOptionPane.showConfirmDialog(frame,
                    "Double check: \n Are you sure the name of category is " + name + " ?",
                    "Final warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.out.println(name);
                try {
//                        h2.insertCategory(name);//insert into database
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                //code for local info update
            } else if (result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "OK, try again then.", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });
        newPart.addActionListener(e -> {
            categoryOptions = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                categoryOptions[i] = categoryList.get(i).name;
            }

            String partID = JOptionPane.showInputDialog("Enter the part ID", null);
            String modelID = JOptionPane.showInputDialog("Enter the model ID", null);
            String name = JOptionPane.showInputDialog("Enter the name of part", null);
            String category = (String) JOptionPane.showInputDialog(null, "Choose from category...",
                    "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null,
                    categoryOptions, // Array of choices
                    categoryOptions[0]); // Initial choice

            try {
                int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter the initial quantity", null));
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                }
                if (!partID.equals("") && !modelID.equals("") && !name.equals("")) {
                    for (Category value : categoryList) {
                        if (value.name.equals(category)) {
                            int result = JOptionPane.showConfirmDialog(frame,
                                    "Double check: \nPartID: " + partID + "\nModelID: " + modelID + "\nName: " + name + "\nCategory: " + category + "\nQuantity: " + quantity + "",
                                    "Final warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (result == JOptionPane.YES_OPTION) {
                                System.out.println("PartID: " + partID + " modelID: " + modelID + " name: " + name + " categoryID: " + value.categoryID + " quantity: " + quantity);
//                                    h2.insertPart(partID, modelID, name, value.categoryID, quantity);//insert into database
                                //code for local info update
                            } else if (result == JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "OK, try again then.", "ALERT", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    System.out.println("loop end");
                } else {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });
        newOp.add(newCategory);
        newOp.add(newPart);
        // add menubar to frame
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(partOperationMenu);
        menuBar.add(newOp);
        frame.setJMenuBar(menuBar);
        //END OF MENU

        // add tab pane, each tab is a category
        JTabbedPane tp = new JTabbedPane();
        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }

        frame.add(tp);
        frame.pack();
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}
