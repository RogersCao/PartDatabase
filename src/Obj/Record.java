package Obj;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Record {
    public String RecordID;
    public java.util.Date Date;
    public String CustomerID;
    public String PartID;
    public int QuantityIN;
    public int QuantityOUT;
    public int CurrentStock;
    public String Remark;

    public Record(String RecordID, String Date, String CustomerID, String PartID, int QuantityIN, int QuantityOUT, int CurrentStock, String Remark) throws ParseException {
        this.RecordID = RecordID;
        this.Date = new SimpleDateFormat("yyyy-MM-dd").parse(Date);  ;
        this.CustomerID = CustomerID;
        this.PartID = PartID;
        this.QuantityIN = QuantityIN;
        this.QuantityOUT = QuantityOUT;
        this.CurrentStock = CurrentStock;
        this.Remark = Remark;
    }
}
