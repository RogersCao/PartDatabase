package Obj;

import Database.h2;

import java.util.List;

public class Part {
    public String partId;
    public String modelNum;
    public String name;
    public String CategoryID;
    public int Stock;
    public List<Record> recordList;

    public Part(String partId, String modelNum, String name, String CategoryID, int Stock) throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();

        this.partId = partId;
        this.modelNum = modelNum;
        this.name = name;
        this.CategoryID = CategoryID;
        this.Stock = Stock;

        try {
            recordList = h2.queryDisplayRecord(partId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Part 场面一度十分尴尬");
        }
        h2.close();
    }
}
