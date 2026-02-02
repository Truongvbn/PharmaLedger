package com.pharmaledger.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Outbound Order Data Transfer Object
 */
public class OutboundOrderDTO implements Serializable {

    private int orderId;
    private String orderCode;
    private Date orderDate;
    private String purpose;
    private String destinationDept;
    private String status;
    private String notes;
    private int createdBy;
    private Integer approvedBy;
    private Date createdAt;
    private Date updatedAt;

    // Joined fields
    private String createdByName;
    private String approvedByName;

    // Order lines
    private List<OutboundLineDTO> lines;

    public OutboundOrderDTO() {
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDestinationDept() {
        return destinationDept;
    }

    public void setDestinationDept(String destinationDept) {
        this.destinationDept = destinationDept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    public List<OutboundLineDTO> getLines() {
        return lines;
    }

    public void setLines(List<OutboundLineDTO> lines) {
        this.lines = lines;
    }
}
