package Table;

import Database.h2;
import Obj.Customer;
import Obj.Part;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class partCategory {
    public JScrollPane sp;

    public partCategory(h2 h2, List<Part> partList, List<Customer> customerList) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        for (Part temp : partList) {
            generatePartTable(h2, panel, temp, customerList);
        }

        sp = new JScrollPane(panel);//add ing panel into sp using constructor
    }

    private static void generatePartTable(h2 h2, JPanel panel, Part part, List<Customer> customerList) {
        String[] columnNames = {"零件号", "零件名称", "型号"};
        Object[] data = {part.partId, part.name, part.modelNum};

        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        defaultTableModel.setColumnIdentifiers(columnNames);
        defaultTableModel.addRow(data);

        JTable header = new JTable(defaultTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //action on click
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.isAltDown()) {
                    if (e.getButton() == 1) {//left
                        int row = header.rowAtPoint(e.getPoint());
                        int col = header.columnAtPoint(e.getPoint());
                        System.out.println("Alt pressed: " + e.isAltDown() + "left mouse key pressed");
                        System.out.println(" Value in the cell clicked :" + " " + header.getValueAt(row, col).toString());
                        System.out.println(" Record ID is :" + " " + header.getValueAt(row, 0).toString());
                        try {
                            switch (col) {
                                case 0://PartID  WIP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                    int result = JOptionPane.showConfirmDialog(null,
                                            "Do you want to edit PartID: " + header.getValueAt(row, col).toString() + "?",
                                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    if (result == JOptionPane.YES_OPTION) {
                                        try {
                                            String condition = JOptionPane.showInputDialog("Enter new PartID", null);
                                            if (!condition.equals("")) {
                                                System.out.println("we made it");
//                                            h2.updatePartName(condition);
                                            } else {
                                                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                            }
                                        } catch (Exception numberException) {
                                            JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                            numberException.printStackTrace();
                                        }
                                    }
                                    break;
                                case 1://Name   DONE
                                    result = JOptionPane.showConfirmDialog(null,
                                            "Do you want to edit Name: " + header.getValueAt(row, col).toString() + "?",
                                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    if (result == JOptionPane.YES_OPTION) {
                                        try {
                                            String value = JOptionPane.showInputDialog("Enter new name", null);
                                            if (!value.equals("")) {
                                                h2.updatePartName(header.getValueAt(row, 0).toString(), value);
                                            } else {
                                                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                            }
                                        } catch (Exception numberException) {
                                            JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                    break;
                                case 2://ModelNUM (specific)  DONE
                                    result = JOptionPane.showConfirmDialog(null,
                                            "Do you want to edit ModelNUM: " + header.getValueAt(row, col).toString() + "?",
                                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                    if (result == JOptionPane.YES_OPTION) {
                                        try {
                                            String value = JOptionPane.showInputDialog("Enter new ModelNUM", null);
                                            if (!value.equals("")) {
                                                h2.updatePartModelNUM(header.getValueAt(row, 0).toString(), value);
                                            } else {
                                                JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                            }
                                        } catch (Exception numberException) {
                                            JOptionPane.showMessageDialog(null, "The info contains error, try again", "ALERT", JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                    break;
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });

        panel.add(header);//header
        header.setFont(new Font("STHeiti", Font.PLAIN, 18));
        header.setRowHeight(30);
        partTable t = new partTable(h2, part, customerList);//table body (parts)
        panel.add(new JScrollPane(t.table));
    }
}
