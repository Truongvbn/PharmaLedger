package com.pharmaledger.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Alert Event Data Transfer Object
 */
public class AlertEventDTO implements Serializable {

    private int eventId;
    private int ruleId;
    private Integer drugId;
    private Integer batchId;
    private String alertType;
    private String severity;
    private String title;
    private String description;
    private String status;
    private Date triggeredAt;
    private Date acknowledgedAt;
    private Date resolvedAt;
    private Integer acknowledgedBy;

    // Joined fields
    private String ruleName;
    private String drugName;
    private String batchNumber;
    private String acknowledgedByName;

    public AlertEventDTO() {
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Date triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public Date getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(Date acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Integer getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(Integer acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getAcknowledgedByName() {
        return acknowledgedByName;
    }

    public void setAcknowledgedByName(String acknowledgedByName) {
        this.acknowledgedByName = acknowledgedByName;
    }
}
