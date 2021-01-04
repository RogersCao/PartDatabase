import Database.h2;
import Obj.Category;
import Obj.Customer;
import Table.partCategory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UI {
    static JFrame frame;
    static JMenuBar menuBar;
    // file Menu
    static JMenu fileMenu;
    static JMenuItem m1, m2;
    // search menu
    static JMenu searchMenu;
    static JMenuItem byPart, byCategory, byCustomer, clear;
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

    static JTabbedPane tp;

    public static void update(h2 h2) throws Exception {
        categoryList = h2.queryCategoryList();
        customerList = h2.queryCustomerList();
        tp.removeAll();

        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }
    }

    public static void updateByCategory(h2 h2, List<Category> listOfCategoryAfterSearch) throws Exception {
        categoryList = listOfCategoryAfterSearch;
        customerList = h2.queryCustomerList();
        tp.removeAll();

        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }
    }

    public static void updateByCustomer(h2 h2) throws Exception {
        categoryList = h2.queryCategoryList();
        customerList = h2.queryCustomerList();
        tp.removeAll();

        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }

        Component[] tpComponents = tp.getComponents();
        for (Component currentTabScrollPane : tpComponents) {//the ScrollPane component of each tab
            JScrollPane pane = (JScrollPane) currentTabScrollPane;
            JViewport view = pane.getViewport();
            Component component = view.getComponents()[0];
            System.out.println(component);//2 component as a group (table(header)+panel)
            JPanel panel = (JPanel) component;
            Component[] components = panel.getComponents();

            for (int i = 0; i < components.length; i += 2) {
                JTable headerTable = (JTable) components[i];
                System.out.println(headerTable);

                JScrollPane tableScrollPane = (JScrollPane) components[i + 1];
                JViewport viewSecond = tableScrollPane.getViewport();
                Component[] tableScrollPaneContent = viewSecond.getComponents();
                JTable table = (JTable) tableScrollPaneContent[0];
//                Component[] temp = tableScrollPane.getComponents();
                System.out.println(table.getRowCount());
            }

        }
    }

    public static void updateByPart(h2 h2, List<Customer> listOfCustomerAfterSearch) throws Exception {
//        categoryList = h2.queryCategoryList();
//        customerList = listOfCustomerAfterSearch;
//        tp.removeAll();
//
//        //each part has own table in tab pane
//        for (Category temp : categoryList) {
//            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
//        }
    }

    public static void main(String[] args) throws Exception {
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
        byPart = new JMenuItem("Search by part");
        byCategory = new JMenuItem("Search by Category");
        byCustomer = new JMenuItem("Search by Customer");
        clear = new JMenuItem("Clear search");
        byPart.addActionListener(e -> {
            String[] options = {"By Name", "By PartID", "By ModelNum"};
            String searchStrategy = (String) JOptionPane.showInputDialog(null, "Search part by...",
                    "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null,
                    options, // Array of choices
                    options[0]); // Initial choice

            try {
                String condition = JOptionPane.showInputDialog("Enter the condition", null);
                if (condition.equals("")) {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                } else {
                    switch (searchStrategy) {
                        case "By Name":
                            h2.queryPart("NAME", condition);
                            break;
                        case "By PartID":
                            h2.queryPart("PARTID", condition);
                            break;
                        case "By ModelNum":
                            h2.queryPart("MODELNUM", condition);
                            break;
                    }
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });
        byCategory.addActionListener(e -> {
            try {
                String condition = JOptionPane.showInputDialog("Enter the name of category", null);
                if (condition.equals("")) {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                } else {
                    updateByCategory(h2, h2.queryCategory(condition));
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });
        byCustomer.addActionListener(e -> {
            try {
                String condition = JOptionPane.showInputDialog("Enter the name of customer", null);
                if (condition.equals("")) {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                } else {
                    updateByCustomer(h2);
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });
        clear.addActionListener(e -> {
            try {
                update(h2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        searchMenu.add(byPart);
        searchMenu.add(byCategory);
        searchMenu.add(byCustomer);
        searchMenu.add(clear);

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
            if (!name.equals("")) {
                int result = JOptionPane.showConfirmDialog(frame,
                        "Double check: \n Are you sure the name of category is " + name + " ?",
                        "Final warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        System.out.println(name);
                        h2.insertCategory(name);//insert into database
                        update(h2);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    //code for local info update
                } else if (result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "OK, try again then.", "ALERT", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
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
                                h2.insertPart(partID, modelID, name, value.categoryID, quantity);//insert into database
                                update(h2);
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
        tp = new JTabbedPane();
        //each part has own table in tab pane
        update(h2);

        frame.add(tp);
        frame.pack();
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}