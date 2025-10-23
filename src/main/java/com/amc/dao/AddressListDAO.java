package com.amc.dao;

import com.amc.model.AddressList;
import com.amc.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressListDAO {
    
    public List<AddressList> getAddressListByDirectorate(String directorate) {
        List<AddressList> addressList = new ArrayList<>();
        String sql = "SELECT al.* FROM Address_List al " +
                    "INNER JOIN Directorate_Master dm ON al.directorate_id = dm.directorate_id " +
                    "WHERE dm.directorate_name = ? AND al.status = 'Active' " +
                    "ORDER BY al.user_name";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, directorate);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AddressList address = new AddressList();
                address.setAddressId(rs.getInt("address_id"));
                address.setDirectorateId(rs.getInt("directorate_id"));
                address.setUserName(rs.getString("user_name"));
                address.setDesignation(rs.getString("designation"));
                address.setDepartment(rs.getString("department"));
                address.setAddress(rs.getString("address"));
                address.setStatus(rs.getString("status"));
                addressList.add(address);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return addressList;
    }
    
    public List<AddressList> getAllAddressList() {
        List<AddressList> addressList = new ArrayList<>();
        String sql = "SELECT * FROM Address_List WHERE status = 'Active' ORDER BY user_name";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AddressList address = new AddressList();
                address.setAddressId(rs.getInt("address_id"));
                address.setDirectorateId(rs.getInt("directorate_id"));
                address.setUserName(rs.getString("user_name"));
                address.setDesignation(rs.getString("designation"));
                address.setDepartment(rs.getString("department"));
                address.setAddress(rs.getString("address"));
                address.setStatus(rs.getString("status"));
                addressList.add(address);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return addressList;
    }
}
