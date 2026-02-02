package com.pharmaledger.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Forecast Point Data Transfer Object
 */
public class ForecastPointDTO implements Serializable {

    private int pointId;
    private int seriesId;
    private Date forecastDate;
    private BigDecimal predictedQuantity;
    private BigDecimal lowerBound;
    private BigDecimal upperBound;
    private BigDecimal confidence;

    public ForecastPointDTO() {
    }

    // Getters and Setters
    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public BigDecimal getPredictedQuantity() {
        return predictedQuantity;
    }

    public void setPredictedQuantity(BigDecimal predictedQuantity) {
        this.predictedQuantity = predictedQuantity;
    }

    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }
}
