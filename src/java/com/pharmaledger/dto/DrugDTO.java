package com.pharmaledger.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Drug Data Transfer Object
 */
public class DrugDTO implements Serializable {
    
    private int drugId;
    private String drugCode;
    private String drugName;
    private String activeIngredient;
    private String dosageForm;
    private String strength;
    private String unit;
    private int minStock;
    private String description;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    
    public DrugDTO() {
    }

    // Getters and Setters
    public int getDrugId() { return drugId; }
    public void setDrugId(int drugId) { this.drugId = drugId; }
    
    public String getDrugCode() { return drugCode; }
    public void setDrugCode(String drugCode) { this.drugCode = drugCode; }
    
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    
    public String getActiveIngredient() { return activeIngredient; }
    public void setActiveIngredient(String activeIngredient) { this.activeIngredient = activeIngredient; }
    
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
