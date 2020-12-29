package Obj;

import Database.h2;

import java.util.List;

public class Category {
    public String name;
    public String categoryID;
    public List<Part> partList;

    public Category(String categoryID, String name) throws Exception {
        h2 h2 = new h2();
        h2.connection();
        h2.statement();

        this.name = name;
        this.categoryID = categoryID;

        try {
            partList = h2.queryPartByCategory(categoryID);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Category 场面一度十分尴尬");
        }
        System.out.println("Category built");
        h2.close();
    }
}