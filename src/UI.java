import Database.h2;
import Obj.Category;
import Obj.Customer;
import Obj.Part;
import Table.partCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UI {
    static JFrame frame;
    static JMenuBar menuBar;
    // file Menu
    static JMenu fileMenu;
    static JMenuItem load, save;
    // search menu
    static JMenu searchMenu;
    static JMenuItem byPart, byCategory, byCustomer, clear;
    // part Operation Menu
    static JMenu partOperationMenu;
    static JMenuItem stockOut, stockIn, update;
    //newOp Menu
    static JMenu newOp;
    static JMenuItem newCategory, newPart;
    static String[] categoryOptions, partOptions, customerOptions;
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

    public static void updateByCustomer(h2 h2, String condition) throws Exception {
        categoryList = h2.queryCategoryList();
        customerList = h2.queryCustomerList();
        tp.removeAll();

        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }
        try {
            Component[] tpComponents = tp.getComponents();
            for (Component currentTabScrollPane : tpComponents) {//the ScrollPane component of each tab
                JScrollPane pane = (JScrollPane) currentTabScrollPane;
                JViewport view = pane.getViewport();
                Component component = view.getComponents()[0];
                JPanel panel = (JPanel) component;
                Component[] components = panel.getComponents();
                int componentCount = components.length;
                for (int i = 0; i < componentCount; i += 2) {
                    JTable headerTable = (JTable) components[i];//零件信息table
                    JScrollPane tableScrollPane = (JScrollPane) components[i + 1];
                    JViewport viewSecond = tableScrollPane.getViewport();
                    Component[] tableScrollPaneContent = viewSecond.getComponents();
                    JTable table = (JTable) tableScrollPaneContent[0];//记录table
                    DefaultTableModel tableModel = ((DefaultTableModel) table.getModel());
                    int rowCount = table.getRowCount();
                    for (int j = 0; j < rowCount; j++) {
                        String temp = (String) table.getModel().getValueAt(j, 1);
                        if (!temp.toLowerCase().contains(condition.toLowerCase())) {//compare
                            tableModel.removeRow(j);
                            j--;
                            rowCount = table.getRowCount();
                        }
                    }
                    if (table.getRowCount() == 0) {
                        panel.remove(headerTable);
                        panel.remove(tableScrollPane);
                    }
                }
                if (panel.getComponents().length == 0) {
                    tp.remove(currentTabScrollPane);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateByPart(h2 h2, String condition, String searchBy) throws Exception {
        categoryList = h2.queryCategoryList();
        customerList = h2.queryCustomerList();
        tp.removeAll();

        //each part has own table in tab pane
        for (Category temp : categoryList) {
            tp.add(temp.name, new partCategory(temp.partList, customerList).sp);
        }
        try {
            Component[] tpComponents = tp.getComponents();
            for (Component currentTabScrollPane : tpComponents) {//the ScrollPane component of each tab
                JScrollPane pane = (JScrollPane) currentTabScrollPane;
                JViewport view = pane.getViewport();
                Component component = view.getComponents()[0];
                JPanel panel = (JPanel) component;
                Component[] components = panel.getComponents();
                int componentCount = components.length;
                for (int i = 0; i < componentCount; i += 2) {
                    JTable headerTable = (JTable) components[i];//零件信息table
                    JScrollPane tableScrollPane = (JScrollPane) components[i + 1];

                    String temp = "";
                    switch (searchBy) {
                        case "By Name":
                            temp = (String) headerTable.getModel().getValueAt(0, 1);
                            break;
                        case "By PartID":
                            temp = (String) headerTable.getModel().getValueAt(0, 0);
                            break;
                        case "By ModelNum":
                            temp = (String) headerTable.getModel().getValueAt(0, 2);
                            break;
                    }
                    if (!temp.toLowerCase().contains(condition.toLowerCase())) {//compare
                        panel.remove(headerTable);
                        panel.remove(tableScrollPane);
                    }
                }
                if (panel.getComponents().length == 0) {
                    tp.remove(currentTabScrollPane);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        h2 h2 = new h2();//h2 db connection
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
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        load.addActionListener(e -> {
        });//WIP-------------------------------------
        save.addActionListener(e -> {
        });//WIP-------------------------------------
        fileMenu.add(load);
        fileMenu.add(save);

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
                            updateByPart(h2, condition, "By Name");
                            break;
                        case "By PartID":
                            updateByPart(h2, condition, "By PartID");
                            break;
                        case "By ModelNum":
                            updateByPart(h2, condition, "By ModelNum");
                            break;
                    }
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });//done
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
        });//done
        byCustomer.addActionListener(e -> {
            try {
                String condition = JOptionPane.showInputDialog("Enter the name of customer", null);
                if (condition.equals("")) {
                    JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                } else {
                    updateByCustomer(h2, condition);
                }
            } catch (Exception numberException) {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });//done
        clear.addActionListener(e -> {
            try {
                update(h2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });//done
        searchMenu.add(byPart);
        searchMenu.add(byCategory);
        searchMenu.add(byCustomer);
        searchMenu.add(clear);

        // part Operation Menu
        partOperationMenu = new JMenu("Part Operation");
        stockOut = new JMenuItem("出库");
        stockIn = new JMenuItem("入库");
//        update = new JMenuItem("更新信息");
        stockOut.addActionListener(e -> {
            try {
                categoryList = h2.queryCategoryList();
                customerList = h2.queryCustomerList();
            } catch (Exception exception) {
                exception.printStackTrace();
            }//update local info

            //category
            categoryOptions = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                categoryOptions[i] = categoryList.get(i).name;
            }
            String categoryName = (String) JOptionPane.showInputDialog(null, "Choose category...",
                    "Choose category", JOptionPane.QUESTION_MESSAGE, null,
                    categoryOptions, // Array of choices
                    categoryOptions[0]); // Initial choice
            Category category = null;
            for (Category categoryItemTemp : categoryList) {
                if (categoryItemTemp.name.equals(categoryName)) {
                    category = categoryItemTemp;
                }
            }

            //part
            assert category != null;
            partOptions = new String[category.partList.size()];
            for (int i = 0; i < category.partList.size(); i++) {
                partOptions[i] = category.partList.get(i).name + " : " + category.partList.get(i).partId;
            }
            String partName = (String) JOptionPane.showInputDialog(null, "Choose part...",
                    "Choose part", JOptionPane.QUESTION_MESSAGE, null,
                    partOptions, // Array of choices
                    partOptions[0]); // Initial choice
            Part part = null;
            for (Part partItemTemp : category.partList) {
                String temp = partItemTemp.name + " : " + partItemTemp.partId;
                if (temp.equals(partName)) {
                    part = partItemTemp;
                }
            }
            assert part != null;
            String partID = part.partId;

            //quantity
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity", null));

            //date
            JTextField yearField = new JTextField(5);
            JTextField monthField = new JTextField(5);
            JTextField dayField = new JTextField(5);
            JPanel datePanel = new JPanel();
            datePanel.add(new JLabel("Year:"));
            datePanel.add(yearField);
            datePanel.add(Box.createHorizontalStrut(15)); // a spacer
            datePanel.add(new JLabel("Month:"));
            datePanel.add(monthField);
            datePanel.add(Box.createHorizontalStrut(15)); // a spacer
            datePanel.add(new JLabel("Day:"));
            datePanel.add(dayField);
            int dateOK = JOptionPane.showConfirmDialog(null, datePanel, "Please Enter date", JOptionPane.OK_CANCEL_OPTION);
            int year = Integer.parseInt(yearField.getText());
            int month = Integer.parseInt(monthField.getText());
            int day = Integer.parseInt(dayField.getText());
            if (dateOK == JOptionPane.OK_OPTION) {
                System.out.println("x value: " + yearField.getText());
                System.out.println("y value: " + monthField.getText());
                System.out.println("y value: " + dayField.getText());
            }

            //customer
            Customer customer = null;
            customerOptions = new String[customerList.size() + 1];
            customerOptions[0] = "Enter Manually";
            for (int i = 1; i < customerList.size(); i++) {
                customerOptions[i] = customerList.get(i - 1).Name;
            }
            String customerName = (String) JOptionPane.showInputDialog(null, "Choose customer...",
                    "Choose customer", JOptionPane.QUESTION_MESSAGE, null,
                    customerOptions, // Array of choices
                    customerOptions[0]); // Initial choice
            if (!customerName.equals("Enter Manually")) {//picked customer
                for (Customer customerItemTemp : customerList) {
                    if (customerItemTemp.Name.equals(customerName)) {
                        customer = customerItemTemp;
                    }
                }
            }

            if (quantity >= 0 && month > 0 && month <= 12 && day <= 31 && day > 0) {
                if (customerName.equals("Enter Manually")) {//manual customer
                    customerName = JOptionPane.showInputDialog("Enter the customer", null);
                    try {
                        h2.insertCustomer(customerName);//insert a new customer
                        customerList = h2.queryCustomerList();
                        for (Customer customerItemTemp : customerList) {
                            if (customerItemTemp.Name.equals(customerName)) {
                                customer = customerItemTemp;
                            }
                        }
                    } catch (Exception exception) {
                        System.out.println("customerName");
                        exception.printStackTrace();
                    }//manual customer input
                }

                //remark
                String remark = JOptionPane.showInputDialog("Enter remark", null);

                String date = "'" + year + "-" + month + "-" + day + "'";

                int result = JOptionPane.showConfirmDialog(frame,
                        "Double check: \nPart: " + partName + "\nCustomer: " + customerName + "" + "\nQuantity: " + quantity + "" + "\nDate: " + date + "" + "\nRemark: " + remark + "",
                        "Final warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        int orignalStock = h2.queryStockQuantity(partID);
                        System.out.println("orignalStock: " + orignalStock);
                        int currentStock = orignalStock - quantity;
                        System.out.println("currentStock: " + currentStock);
                        if (currentStock >= 0) {
                            assert customer != null;
                            h2.insertRecord(date, customer.CustomerID, part.partId, 0, quantity, currentStock, remark);//insert
                        } else {
                            System.out.println(part.name + part.partId);
                            JOptionPane.showMessageDialog(null, "No more stock for you to extract", "ALERT", JOptionPane.WARNING_MESSAGE);
                        }
                        update(h2);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "OK, try again then.", "ALERT", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });//done
        stockIn.addActionListener(e -> {
            try {
                categoryList = h2.queryCategoryList();
                customerList = h2.queryCustomerList();
            } catch (Exception exception) {
                exception.printStackTrace();
            }//update local info

            //category
            categoryOptions = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                categoryOptions[i] = categoryList.get(i).name;
            }
            String categoryName = (String) JOptionPane.showInputDialog(null, "Choose category...",
                    "Choose category", JOptionPane.QUESTION_MESSAGE, null,
                    categoryOptions, // Array of choices
                    categoryOptions[0]); // Initial choice
            Category category = null;
            for (Category categoryItemTemp : categoryList) {
                if (categoryItemTemp.name.equals(categoryName)) {
                    category = categoryItemTemp;
                }
            }

            //part
            assert category != null;
            partOptions = new String[category.partList.size()];
            for (int i = 0; i < category.partList.size(); i++) {
                partOptions[i] = category.partList.get(i).name + " : " + category.partList.get(i).partId;
            }
            String partName = (String) JOptionPane.showInputDialog(null, "Choose part...",
                    "Choose part", JOptionPane.QUESTION_MESSAGE, null,
                    partOptions, // Array of choices
                    partOptions[0]); // Initial choice
            Part part = null;
            for (Part partItemTemp : category.partList) {
                String temp = partItemTemp.name + " : " + partItemTemp.partId;
                if (temp.equals(partName)) {
                    part = partItemTemp;
                }
            }
            assert part != null;
            String partID = part.partId;

            //quantity
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity", null));

            //date
            JTextField yearField = new JTextField(5);
            JTextField monthField = new JTextField(5);
            JTextField dayField = new JTextField(5);
            JPanel datePanel = new JPanel();
            datePanel.add(new JLabel("Year:"));
            datePanel.add(yearField);
            datePanel.add(Box.createHorizontalStrut(15)); // a spacer
            datePanel.add(new JLabel("Month:"));
            datePanel.add(monthField);
            datePanel.add(Box.createHorizontalStrut(15)); // a spacer
            datePanel.add(new JLabel("Day:"));
            datePanel.add(dayField);
            int dateOK = JOptionPane.showConfirmDialog(null, datePanel, "Please Enter date", JOptionPane.OK_CANCEL_OPTION);
            int year = Integer.parseInt(yearField.getText());
            int month = Integer.parseInt(monthField.getText());
            int day = Integer.parseInt(dayField.getText());
            if (dateOK == JOptionPane.OK_OPTION) {
                System.out.println("x value: " + yearField.getText());
                System.out.println("y value: " + monthField.getText());
                System.out.println("y value: " + dayField.getText());
            }

            //customer
            Customer customer = null;
            customerOptions = new String[customerList.size() + 1];
            customerOptions[0] = "Enter Manually";
            for (int i = 1; i < customerList.size(); i++) {
                customerOptions[i] = customerList.get(i - 1).Name;
            }
            String customerName = (String) JOptionPane.showInputDialog(null, "Choose customer...",
                    "Choose customer", JOptionPane.QUESTION_MESSAGE, null,
                    customerOptions, // Array of choices
                    customerOptions[0]); // Initial choice
            if (!customerName.equals("Enter Manually")) {//picked customer
                for (Customer customerItemTemp : customerList) {
                    if (customerItemTemp.Name.equals(customerName)) {
                        customer = customerItemTemp;
                    }
                }
            }

            if (quantity >= 0 && month > 0 && month <= 12 && day <= 31 && day > 0) {
                if (customerName.equals("Enter Manually")) {//manual customer
                    customerName = JOptionPane.showInputDialog("Enter the customer", null);
                    try {
                        h2.insertCustomer(customerName);//insert a new customer
                        customerList = h2.queryCustomerList();
                        for (Customer customerItemTemp : customerList) {
                            if (customerItemTemp.Name.equals(customerName)) {
                                customer = customerItemTemp;
                            }
                        }
                    } catch (Exception exception) {
                        System.out.println("customerName");
                        exception.printStackTrace();
                    }//manual customer input
                }

                //remark
                String remark = JOptionPane.showInputDialog("Enter remark", null);

                String date = "'" + year + "-" + month + "-" + day + "'";

                int result = JOptionPane.showConfirmDialog(frame,
                        "Double check: \nPart: " + partName + "\nCustomer: " + customerName + "" + "\nQuantity: " + quantity + "" + "\nDate: " + date + "" + "\nRemark: " + remark + "",
                        "Final warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        int orignalStock = h2.queryStockQuantity(partID);
                        System.out.println("orignalStock: " + orignalStock);
                        int currentStock = orignalStock + quantity;
                        System.out.println("currentStock: " + currentStock);
                        if (currentStock >= 0) {
                            assert customer != null;
                            h2.insertRecord(date, customer.CustomerID, part.partId, quantity, 0, currentStock, remark);//insert
                        } else {
                            System.out.println(part.name + part.partId);
                            JOptionPane.showMessageDialog(null, "No more stock for you to extract", "ALERT", JOptionPane.WARNING_MESSAGE);
                        }
                        update(h2);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "OK, try again then.", "ALERT", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
            }
        });//done
//        update.addActionListener(e -> {
//        });//WIP-------------------------------------
        partOperationMenu.add(stockOut);
        partOperationMenu.add(stockIn);
//        partOperationMenu.add(update);

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
        });//done
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
        });//done
        newOp.add(newCategory);
        newOp.add(newPart);

        // add menubar to frame
//        menuBar.add(fileMenu);
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