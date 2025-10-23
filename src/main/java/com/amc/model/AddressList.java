package com.amc.model;

public class AddressList {
    private int addressId;
    private int directorateId;
    private String userName;
    private String designation;
    private String department;
    private String address;
    private String status;
    
    public AddressList() {}
    
    // Getters and Setters
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    
    public int getDirectorateId() { return directorateId; }
    public void setDirectorateId(int directorateId) { this.directorateId = directorateId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
