package com.amc.model;

public class AMCItemDetails {
    private int id;
    private String controlNo;
    private int sno;
    private String itemName;
    private String make;
    private String model;
    private int quantity;
    
    public AMCItemDetails() {}
    
    public AMCItemDetails(String controlNo, int sno, String itemName, String make, String model, int quantity) {
        this.controlNo = controlNo;
        this.sno = sno;
        this.itemName = itemName;
        this.make = make;
        this.model = model;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getControlNo() { return controlNo; }
    public void setControlNo(String controlNo) { this.controlNo = controlNo; }
    
    public int getSno() { return sno; }
    public void setSno(int sno) { this.sno = sno; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
