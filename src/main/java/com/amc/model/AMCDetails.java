package com.amc.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class AMCDetails {
    private String controlNo;
    private String amcUserId;
    private String directorate;
    private String amcLetterNo;
    private Date amcLetterDate;
    private String subject;
    private String gemOrderNo;
    private Date gemOrderDate;
    private Date amcEffectFrom;
    private Date validUpto;
    private String paymentMode;
    private BigDecimal amcOrderValue;
    private Timestamp createdDate;
    private String createdBy;
    private String status;
    private List<AMCItemDetails> itemDetailsList;
    
    public AMCDetails() {}
    
    // Getters and Setters
    public String getControlNo() { return controlNo; }
    public void setControlNo(String controlNo) { this.controlNo = controlNo; }
    
    public String getAmcUserId() { return amcUserId; }
    public void setAmcUserId(String amcUserId) { this.amcUserId = amcUserId; }
    
    public String getDirectorate() { return directorate; }
    public void setDirectorate(String directorate) { this.directorate = directorate; }
    
    public String getAmcLetterNo() { return amcLetterNo; }
    public void setAmcLetterNo(String amcLetterNo) { this.amcLetterNo = amcLetterNo; }
    
    public Date getAmcLetterDate() { return amcLetterDate; }
    public void setAmcLetterDate(Date amcLetterDate) { this.amcLetterDate = amcLetterDate; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getGemOrderNo() { return gemOrderNo; }
    public void setGemOrderNo(String gemOrderNo) { this.gemOrderNo = gemOrderNo; }
    
    public Date getGemOrderDate() { return gemOrderDate; }
    public void setGemOrderDate(Date gemOrderDate) { this.gemOrderDate = gemOrderDate; }
    
    public Date getAmcEffectFrom() { return amcEffectFrom; }
    public void setAmcEffectFrom(Date amcEffectFrom) { this.amcEffectFrom = amcEffectFrom; }
    
    public Date getValidUpto() { return validUpto; }
    public void setValidUpto(Date validUpto) { this.validUpto = validUpto; }
    
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    
    public BigDecimal getAmcOrderValue() { return amcOrderValue; }
    public void setAmcOrderValue(BigDecimal amcOrderValue) { this.amcOrderValue = amcOrderValue; }
    
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<AMCItemDetails> getItemDetailsList() { return itemDetailsList; }
    public void setItemDetailsList(List<AMCItemDetails> itemDetailsList) { this.itemDetailsList = itemDetailsList; }
}
