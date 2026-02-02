package com.pharmaledger.dao;

import com.pharmaledger.dto.BatchDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Batch Data Access Object
 */
public class BatchDAO {

    private static final String FIND_BY_ID = "SELECT b.batchId, b.drugId, b.batchNumber, b.manufactureDate, b.expiryDate, "
            +
            "b.initialQuantity, b.currentQuantity, b.unitPrice, b.createdAt, " +
            "d.drugName, d.drugCode " +
            "FROM Batches b " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "WHERE b.batchId = ?";

    private static final String FIND_BY_DRUG = "SELECT b.batchId, b.drugId, b.batchNumber, b.manufactureDate, b.expiryDate, "
            +
            "b.initialQuantity, b.currentQuantity, b.unitPrice, b.createdAt, " +
            "d.drugName, d.drugCode " +
            "FROM Batches b " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "WHERE b.drugId = ? ORDER BY b.expiryDate";

    private static final String FIND_NEAR_EXPIRY = "SELECT b.batchId, b.drugId, b.batchNumber, b.manufactureDate, b.expiryDate, "
            +
            "b.initialQuantity, b.currentQuantity, b.unitPrice, b.createdAt, " +
            "d.drugName, d.drugCode " +
            "FROM Batches b " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "WHERE b.expiryDate <= DATEADD(DAY, ?, GETDATE()) " +
            "AND b.expiryDate >= GETDATE() " +
            "AND b.currentQuantity > 0 " +
            "ORDER BY b.expiryDate";

    private static final String INSERT = "INSERT INTO Batches (drugId, batchNumber, manufactureDate, expiryDate, initialQuantity, currentQuantity, unitPrice) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE Batches SET batchNumber = ?, manufactureDate = ?, expiryDate = ?, " +
            "currentQuantity = ?, unitPrice = ?, updatedAt = GETDATE() WHERE batchId = ?";

    private static final String DELETE = "DELETE FROM Batches WHERE batchId = ?";

    /**
     * Find batch by ID
     */
    public BatchDTO findById(int batchId) throws SQLException, ClassNotFoundException {
        BatchDTO batch = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, batchId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                batch = mapResultSetToBatch(rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return batch;
    }

    /**
     * Find batches by drug ID
     */
    public List<BatchDTO> findByDrug(int drugId) throws SQLException, ClassNotFoundException {
        List<BatchDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_DRUG);
            ptm.setInt(1, drugId);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToBatch(rs));
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

    /**
     * Find batches near expiry within given days
     */
    public List<BatchDTO> findNearExpiry(int days) throws SQLException, ClassNotFoundException {
        List<BatchDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_NEAR_EXPIRY);
            ptm.setInt(1, days);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToBatch(rs));
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

    /**
     * Insert new batch
     */
    public boolean insert(BatchDTO batch) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setInt(1, batch.getDrugId());
            ptm.setString(2, batch.getBatchNumber());
            ptm.setDate(3, batch.getManufactureDate() != null ? new java.sql.Date(batch.getManufactureDate().getTime())
                    : null);
            ptm.setDate(4, new java.sql.Date(batch.getExpiryDate().getTime()));
            ptm.setInt(5, batch.getInitialQuantity());
            ptm.setInt(6, batch.getCurrentQuantity());
            ptm.setBigDecimal(7, batch.getUnitPrice());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    /**
     * Update batch
     */
    public boolean update(BatchDTO batch) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, batch.getBatchNumber());
            ptm.setDate(2, batch.getManufactureDate() != null ? new java.sql.Date(batch.getManufactureDate().getTime())
                    : null);
            ptm.setDate(3, new java.sql.Date(batch.getExpiryDate().getTime()));
            ptm.setInt(4, batch.getCurrentQuantity());
            ptm.setBigDecimal(5, batch.getUnitPrice());
            ptm.setInt(6, batch.getBatchId());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    /**
     * Delete batch
     */
    public boolean delete(int batchId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(DELETE);
            ptm.setInt(1, batchId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    /**
     * Map ResultSet to BatchDTO
     */
    private BatchDTO mapResultSetToBatch(ResultSet rs) throws SQLException {
        BatchDTO batch = new BatchDTO();
        batch.setBatchId(rs.getInt("batchId"));
        batch.setDrugId(rs.getInt("drugId"));
        batch.setBatchNumber(rs.getString("batchNumber"));
        batch.setManufactureDate(rs.getDate("manufactureDate"));
        batch.setExpiryDate(rs.getDate("expiryDate"));
        batch.setInitialQuantity(rs.getInt("initialQuantity"));
        batch.setCurrentQuantity(rs.getInt("currentQuantity"));
        batch.setUnitPrice(rs.getBigDecimal("unitPrice"));
        batch.setCreatedAt(rs.getTimestamp("createdAt"));
        batch.setDrugName(rs.getString("drugName"));
        batch.setDrugCode(rs.getString("drugCode"));
        return batch;
    }
}
