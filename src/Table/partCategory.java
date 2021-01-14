package Table;

import Obj.Customer;
import Obj.Part;

import javax.swing.*;
import java.awt.*;
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

        sp = new JScrollPane(panel);//add ing panel into sp using constructor
    }

    private static void generatePartTable(JPanel panel, Part part, List<Customer> customerList) {
        String[] columnNames = {"零件号", "零件名称", "型号"};
        Object[][] data = {{part.partId, part.name, part.modelNum}};
        JTable header = new JTable(data, columnNames);
        panel.add(header);//header
        header.setFont(new Font("STHeiti", Font.PLAIN, 18));
        header.setRowHeight(30);
        partTable t = new partTable(part, customerList);//table body (parts)
        panel.add(new JScrollPane(t.table));
    }
}
