package Table;

import Obj.Customer;
import Obj.Part;

import javax.swing.*;
import java.util.List;

public class partCategory {
    public JScrollPane sp;

    public partCategory(List<Part> partList, List<Customer> customerList) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        for (Part temp : partList) {
            generatePartTable(panel, temp, customerList);
        }

        sp = new JScrollPane(panel);
    }

    private static void generatePartTable(JPanel panel, Part part, List<Customer> customerList) {
        String[] columnNames = {"零件号", "零件名称", "型号"};
        Object[][] data = {{part.partId, part.name, part.modelNum}};

        panel.add(new JTable(data, columnNames));//header
        partTable t = new partTable(part, customerList);//table body (parts)
        panel.add(new JScrollPane(t.table));
    }
}
