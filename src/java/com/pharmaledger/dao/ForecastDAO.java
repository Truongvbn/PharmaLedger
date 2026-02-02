package com.pharmaledger.dao;

import com.pharmaledger.dto.ForecastSeriesDTO;
import com.pharmaledger.dto.ForecastPointDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Forecast Data Access Object
 */
public class ForecastDAO {

    private static final String FIND_SERIES_BY_DRUG = "SELECT fs.seriesId, fs.drugId, fs.modelName, fs.horizonDays, fs.trainedAt, "
            +
            "fs.maeScore, fs.rmseScore, fs.isActive, d.drugName, d.drugCode " +
            "FROM ForecastSeries fs " +
            "INNER JOIN Drugs d ON fs.drugId = d.drugId " +
            "WHERE fs.drugId = ? AND fs.isActive = 1";

    private static final String FIND_LATEST_SERIES = "SELECT TOP 10 fs.seriesId, fs.drugId, fs.modelName, fs.horizonDays, fs.trainedAt, "
            +
            "fs.maeScore, fs.rmseScore, fs.isActive, d.drugName, d.drugCode " +
            "FROM ForecastSeries fs " +
            "INNER JOIN Drugs d ON fs.drugId = d.drugId " +
            "WHERE fs.isActive = 1 " +
            "ORDER BY fs.trainedAt DESC";

    private static final String FIND_POINTS = "SELECT pointId, seriesId, forecastDate, predictedQty, lowerBound, upperBound, actualQty "
            +
            "FROM ForecastPoints WHERE seriesId = ? ORDER BY forecastDate";

    private static final String INSERT_SERIES = "INSERT INTO ForecastSeries (drugId, modelName, horizonDays, maeScore, rmseScore) "
            +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_POINT = "INSERT INTO ForecastPoints (seriesId, forecastDate, predictedQty, lowerBound, upperBound) "
            +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_ACTUAL = "UPDATE ForecastPoints SET actualQty = ? WHERE pointId = ?";

    public ForecastSeriesDTO findSeriesByDrug(int drugId) throws SQLException, ClassNotFoundException {
        ForecastSeriesDTO series = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_SERIES_BY_DRUG);
            ptm.setInt(1, drugId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                series = mapResultSetToSeries(rs);
                series.setPoints(findPoints(series.getSeriesId()));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return series;
    }

    public List<ForecastSeriesDTO> findLatestSeries() throws SQLException, ClassNotFoundException {
        List<ForecastSeriesDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_LATEST_SERIES);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToSeries(rs));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return list;
    }

    public List<ForecastPointDTO> findPoints(int seriesId) throws SQLException, ClassNotFoundException {
        List<ForecastPointDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_POINTS);
            ptm.setInt(1, seriesId);
            rs = ptm.executeQuery();
            while (rs.next()) {
                ForecastPointDTO point = new ForecastPointDTO();
                point.setPointId(rs.getInt("pointId"));
                point.setSeriesId(rs.getInt("seriesId"));
                point.setForecastDate(rs.getDate("forecastDate"));
                point.setPredictedQty(rs.getInt("predictedQty"));
                point.setLowerBound(rs.getObject("lowerBound") != null ? rs.getInt("lowerBound") : null);
                point.setUpperBound(rs.getObject("upperBound") != null ? rs.getInt("upperBound") : null);
                point.setActualQty(rs.getObject("actualQty") != null ? rs.getInt("actualQty") : null);
                list.add(point);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return list;
    }

    public int insertSeries(ForecastSeriesDTO series) throws SQLException, ClassNotFoundException {
        int seriesId = -1;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_SERIES, Statement.RETURN_GENERATED_KEYS);
            ptm.setInt(1, series.getDrugId());
            ptm.setString(2, series.getModelName());
            ptm.setInt(3, series.getHorizonDays());
            ptm.setBigDecimal(4, series.getMaeScore());
            ptm.setBigDecimal(5, series.getRmseScore());
            ptm.executeUpdate();
            rs = ptm.getGeneratedKeys();
            if (rs.next()) {
                seriesId = rs.getInt(1);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return seriesId;
    }

    public boolean insertPoint(ForecastPointDTO point) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_POINT);
            ptm.setInt(1, point.getSeriesId());
            ptm.setDate(2, new java.sql.Date(point.getForecastDate().getTime()));
            ptm.setInt(3, point.getPredictedQty());
            ptm.setObject(4, point.getLowerBound());
            ptm.setObject(5, point.getUpperBound());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private ForecastSeriesDTO mapResultSetToSeries(ResultSet rs) throws SQLException {
        ForecastSeriesDTO series = new ForecastSeriesDTO();
        series.setSeriesId(rs.getInt("seriesId"));
        series.setDrugId(rs.getInt("drugId"));
        series.setModelName(rs.getString("modelName"));
        series.setHorizonDays(rs.getInt("horizonDays"));
        series.setTrainedAt(rs.getTimestamp("trainedAt"));
        series.setMaeScore(rs.getBigDecimal("maeScore"));
        series.setRmseScore(rs.getBigDecimal("rmseScore"));
        series.setActive(rs.getBoolean("isActive"));
        series.setDrugName(rs.getString("drugName"));
        series.setDrugCode(rs.getString("drugCode"));
        return series;
    }
}
