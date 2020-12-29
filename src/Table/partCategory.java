package Table;

import javax.swing.*;

public class partCategory {
    public JScrollPane sp;

    public partCategory() {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        generatePartTable(panel);
        generatePartTable(panel);
        generatePartTable(panel);
        generatePartTable(panel);

        sp = new JScrollPane(panel);
    }

    private static void generatePartTable(JPanel panel) {
        String[] columnNames = {"零件号", "零件名称", "型号"};
        Object[][] data = {
                {"LA0131", "Ball Bearing", "6222-2Z/C3"},
        };
        panel.add(new JTable(data, columnNames));
        partTable t = new partTable();
        panel.add(new JScrollPane(t.table));
    }
}
