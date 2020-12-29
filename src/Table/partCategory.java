package Table;

import Obj.Part;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

public class partCategory {
    public JScrollPane sp;

    public partCategory(List<Part> partList) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        for (Part temp : partList) {
            generatePartTable(panel, temp);
        }

        sp = new JScrollPane(panel);
    }

    private static void generatePartTable(JPanel panel,Part part) {
        String[] columnNames = {"零件号", "零件名称", "型号"};
        Object[][] data = {{part.partId, part.name, part.modelNum}};

        panel.add(new JTable(data, columnNames));//header
        partTable t = new partTable(part);//table body (parts)
        panel.add(new JScrollPane(t.table));
    }
}
