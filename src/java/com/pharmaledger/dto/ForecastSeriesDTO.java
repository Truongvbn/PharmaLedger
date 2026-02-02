package com.pharmaledger.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Forecast Series Data Transfer Object
 */
public class ForecastSeriesDTO implements Serializable {

    private int seriesId;
    private int drugId;
    private String forecastType;
    private String modelName;
    private int horizonDays;
    private Date createdAt;

    // Joined fields
    private String drugName;
    private String drugCode;

    // Forecast points
    private List<ForecastPointDTO> points;

    public ForecastSeriesDTO() {
    }

    // Getters and Setters
    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getForecastType() {
        return forecastType;
    }

    public void setForecastType(String forecastType) {
        this.forecastType = forecastType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getHorizonDays() {
        return horizonDays;
    }

    public void setHorizonDays(int horizonDays) {
        this.horizonDays = horizonDays;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public List<ForecastPointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<ForecastPointDTO> points) {
        this.points = points;
    }
}
