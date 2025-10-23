package com.amc.dao;

import com.amc.model.AMCDetails;
import com.amc.model.AMCItemDetails;
import com.amc.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AMCDAO {
    
    public boolean saveAMC(AMCDetails amc) {
        Connection connection = null;
        PreparedStatement amcStatement = null;
        PreparedStatement itemStatement = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);
            
            // Insert parent record
            String amcSql = "INSERT INTO AMC_Warranty_Details (control_no, amc_user_id, directorate, " +
                           "amc_letter_no, amc_letter_date, subject, gem_order_no, gem_order_date, " +
                           "amc_effect_from, valid_upto, payment_mode, amc_order_value, created_by) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            amcStatement = connection.prepareStatement(amcSql);
            amcStatement.setString(1, amc.getControlNo());
            amcStatement.setString(2, amc.getAmcUserId());
            amcStatement.setString(3, amc.getDirectorate());
            amcStatement.setString(4, amc.getAmcLetterNo());
            amcStatement.setDate(5, amc.getAmcLetterDate());
            amcStatement.setString(6, amc.getSubject());
            amcStatement.setString(7, amc.getGemOrderNo());
            amcStatement.setDate(8, amc.getGemOrderDate());
            amcStatement.setDate(9, amc.getAmcEffectFrom());
            amcStatement.setDate(10, amc.getValidUpto());
            amcStatement.setString(11, amc.getPaymentMode());
            amcStatement.setBigDecimal(12, amc.getAmcOrderValue());
            amcStatement.setString(13, amc.getCreatedBy());
            
            int amcResult = amcStatement.executeUpdate();
            
            if (amcResult > 0 && amc.getItemDetailsList() != null) {
                // Insert child records
                String itemSql = "INSERT INTO AMC_Warranty_Item_Details (control_no, sno, item_name, make, model, quantity) " +
                               "VALUES (?, ?, ?, ?, ?, ?)";
                
                itemStatement = connection.prepareStatement(itemSql);
                
                for (AMCItemDetails item : amc.getItemDetailsList()) {
                    itemStatement.setString(1, item.getControlNo());
                    itemStatement.setInt(2, item.getSno());
                    itemStatement.setString(3, item.getItemName());
                    itemStatement.setString(4, item.getMake());
                    itemStatement.setString(5, item.getModel());
                    itemStatement.setInt(6, item.getQuantity());
                    itemStatement.addBatch();
                }
                
                itemStatement.executeBatch();
            }
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (amcStatement != null) amcStatement.close();
                if (itemStatement != null) itemStatement.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getNextSequenceNumber(String year, String month) throws SQLException {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);
            
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            
            String selectSql = "SELECT last_sequence FROM Control_Number_Sequence WHERE year = ? AND month = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, yearInt);
            selectStatement.setInt(2, monthInt);
            
            ResultSet rs = selectStatement.executeQuery();
            
            int nextSequence;
            if (rs.next()) {
                int currentSequence = rs.getInt("last_sequence");
                nextSequence = currentSequence + 1;
                
                String updateSql = "UPDATE Control_Number_Sequence SET last_sequence = ? WHERE year = ? AND month = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setInt(1, nextSequence);
                updateStatement.setInt(2, yearInt);
                updateStatement.setInt(3, monthInt);
                updateStatement.executeUpdate();
            } else {
                nextSequence = 1;
                String insertSql = "INSERT INTO Control_Number_Sequence (year, month, last_sequence) VALUES (?, ?, ?)";
                insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setInt(1, yearInt);
                insertStatement.setInt(2, monthInt);
                insertStatement.setInt(3, nextSequence);
                insertStatement.executeUpdate();
            }
            
            connection.commit();
            return nextSequence;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            try {
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (updateStatement != null) updateStatement.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<AMCDetails> getAllAMCs() {
        List<AMCDetails> amcList = new ArrayList<>();
        String sql = "SELECT * FROM AMC_Warranty_Details ORDER BY created_date DESC";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AMCDetails amc = new AMCDetails();
                amc.setControlNo(rs.getString("control_no"));
                amc.setAmcUserId(rs.getString("amc_user_id"));
                amc.setDirectorate(rs.getString("directorate"));
                amc.setAmcLetterNo(rs.getString("amc_letter_no"));
                amc.setAmcLetterDate(rs.getDate("amc_letter_date"));
                amc.setSubject(rs.getString("subject"));
                amc.setGemOrderNo(rs.getString("gem_order_no"));
                amc.setGemOrderDate(rs.getDate("gem_order_date"));
                amc.setAmcEffectFrom(rs.getDate("amc_effect_from"));
                amc.setValidUpto(rs.getDate("valid_upto"));
                amc.setPaymentMode(rs.getString("payment_mode"));
                amc.setAmcOrderValue(rs.getBigDecimal("amc_order_value"));
                amc.setCreatedDate(rs.getTimestamp("created_date"));
                amc.setCreatedBy(rs.getString("created_by"));
                amc.setStatus(rs.getString("status"));
                
                amc.setItemDetailsList(getItemDetailsByControlNo(amc.getControlNo()));
                
                amcList.add(amc);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return amcList;
    }
    
    public List<AMCItemDetails> getItemDetailsByControlNo(String controlNo) {
        List<AMCItemDetails> itemList = new ArrayList<>();
        String sql = "SELECT * FROM AMC_Warranty_Item_Details WHERE control_no = ? ORDER BY sno";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, controlNo);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                AMCItemDetails item = new AMCItemDetails();
                item.setId(rs.getInt("id"));
                item.setControlNo(rs.getString("control_no"));
                item.setSno(rs.getInt("sno"));
                item.setItemName(rs.getString("item_name"));
                item.setMake(rs.getString("make"));
                item.setModel(rs.getString("model"));
                item.setQuantity(rs.getInt("quantity"));
                itemList.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return itemList;
    }
}
