package Table;

import Obj.Customer;
import Obj.Part;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class partTable {
    public JTable table;

    public partTable(Part part, List<Customer> customerList) {
        String[] columnNames = {"Date", "Customer", "Qty.IN", "Qty.OUT", "Stock", "Remarks"};
        Object[][] data = new Object[part.recordList.size()][6];

        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        defaultTableModel.setColumnIdentifiers(columnNames);

        for (int i = 0; i < part.recordList.size(); i++) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            data[i][0] = ft.format(part.recordList.get(i).Date);
            for (Customer customer : customerList) {
                if (customer.CustomerID.equals(part.recordList.get(i).CustomerID)) {
                    data[i][1] = customer.Name;
                }
            }
            data[i][2] = part.recordList.get(i).QuantityIN;
            data[i][3] = part.recordList.get(i).QuantityOUT;
            data[i][4] = part.recordList.get(i).CurrentStock;
            data[i][5] = part.recordList.get(i).Remark;
        }

        table = new JTable(defaultTableModel);
        table.setAutoCreateRowSorter(true);
        for (Object[] datum : data) {
            defaultTableModel.addRow(new Object[]{datum[0], datum[1], datum[2], datum[3], datum[4], datum[5]});
        }

        Font headerFont = new Font("Verdana", Font.PLAIN, 18);
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(30);

        //set size for tables
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        final int COLUMN_0_WIDTH = 115;
        table.getColumnModel().getColumn(0).setPreferredWidth(COLUMN_0_WIDTH);
        table.getColumnModel().getColumn(0).setMinWidth(COLUMN_0_WIDTH);
        table.getColumnModel().getColumn(0).setMaxWidth(COLUMN_0_WIDTH * 2);
        final int COLUMN_1_WIDTH = 515;
        table.getColumnModel().getColumn(1).setPreferredWidth(COLUMN_1_WIDTH);
        table.getColumnModel().getColumn(1).setMinWidth(COLUMN_1_WIDTH);
        table.getColumnModel().getColumn(1).setMaxWidth(COLUMN_1_WIDTH * 4);
        final int COLUMN_2_WIDTH = 80;
        table.getColumnModel().getColumn(2).setPreferredWidth(COLUMN_2_WIDTH);
        table.getColumnModel().getColumn(2).setMinWidth(COLUMN_2_WIDTH);
        table.getColumnModel().getColumn(2).setMaxWidth(COLUMN_2_WIDTH);
        final int COLUMN_3_WIDTH = 80;
        table.getColumnModel().getColumn(3).setPreferredWidth(COLUMN_3_WIDTH);
        table.getColumnModel().getColumn(3).setMinWidth(COLUMN_3_WIDTH);
        table.getColumnModel().getColumn(3).setMaxWidth(COLUMN_3_WIDTH);
        final int COLUMN_4_WIDTH = 80;
        table.getColumnModel().getColumn(4).setPreferredWidth(COLUMN_4_WIDTH);
        table.getColumnModel().getColumn(4).setMinWidth(COLUMN_4_WIDTH);
        table.getColumnModel().getColumn(4).setMaxWidth(COLUMN_4_WIDTH);

        //action on change
        table.getModel().addTableModelListener(e -> {
//            System.out.println(e.getFirstRow());
//            System.out.println(e.getColumn());
//            System.out.println(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
        });

        //action on click
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.isAltDown()) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    System.out.println(e.isAltDown());
                    System.out.println(e.getButton());
                    JOptionPane.showMessageDialog(null, " Value in the cell clicked :" + " "
                            + table.getValueAt(row, col).toString());
                    System.out.println(" Value in the cell clicked :" + " " + table.getValueAt(row, col).toString());
                }
            }
        });
    }
}