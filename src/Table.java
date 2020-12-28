import javax.swing.*;

public class Table {
    public JTable table;

    public Table() {
        String[] columnNames = {"Date", "Customer", "Qty.IN", "Qty.OUT", "Stock", "Remarks", "Payment"};
        Object[][] data = {
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
                {"2020-12-31", "Wuhai WWTP Spare Parts Service", null, 5, 15, "RMB 3,616 each. Will pay after service Contract closed.", null},
        };
        table = new JTable(data, columnNames);
        table.getTableHeader().setReorderingAllowed(false);

        //set size for tables
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        final int COLUMN_0_WIDTH = 75;
        table.getColumnModel().getColumn(0).setPreferredWidth(COLUMN_0_WIDTH);
        table.getColumnModel().getColumn(0).setMinWidth(COLUMN_0_WIDTH);
        table.getColumnModel().getColumn(0).setMaxWidth(COLUMN_0_WIDTH * 2);
        final int COLUMN_1_WIDTH = 250;
        table.getColumnModel().getColumn(1).setPreferredWidth(COLUMN_1_WIDTH);
        table.getColumnModel().getColumn(1).setMinWidth(COLUMN_1_WIDTH);
        table.getColumnModel().getColumn(1).setMaxWidth(COLUMN_1_WIDTH * 4);
        final int COLUMN_2_WIDTH = 50;
        table.getColumnModel().getColumn(2).setPreferredWidth(COLUMN_2_WIDTH);
        table.getColumnModel().getColumn(2).setMinWidth(COLUMN_2_WIDTH);
        table.getColumnModel().getColumn(2).setMaxWidth(COLUMN_2_WIDTH * 2);
        final int COLUMN_3_WIDTH = 55;
        table.getColumnModel().getColumn(3).setPreferredWidth(COLUMN_3_WIDTH);
        table.getColumnModel().getColumn(3).setMinWidth(COLUMN_3_WIDTH);
        table.getColumnModel().getColumn(3).setMaxWidth(COLUMN_3_WIDTH * 2);
        final int COLUMN_4_WIDTH = 50;
        table.getColumnModel().getColumn(4).setPreferredWidth(COLUMN_4_WIDTH);
        table.getColumnModel().getColumn(4).setMinWidth(COLUMN_4_WIDTH);
        table.getColumnModel().getColumn(4).setMaxWidth(COLUMN_4_WIDTH * 2);

        //action on change
        table.getModel().addTableModelListener(e -> {
            System.out.println(e.getFirstRow());
            System.out.println(e.getColumn());
            System.out.println(table.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
        });
    }
}