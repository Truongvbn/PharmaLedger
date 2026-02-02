package com.pharmaledger.dao;

import com.pharmaledger.dto.StockLedgerDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stock Ledger Data Access Object
 */
public class StockLedgerDAO {

    private static final String FIND_BY_DRUG = "SELECT sl.ledgerId, sl.drugId, sl.batchId, sl.locationId, sl.transactionType, "
            +
            "sl.quantity, sl.balanceBefore, sl.balanceAfter, sl.referenceType, sl.referenceId, " +
            "sl.reason, sl.userId, sl.transactionDate, " +
            "d.drugName, d.drugCode, b.batchNumber, l.locationName, u.fullname as userName " +
            "FROM StockLedger sl " +
            "INNER JOIN Drugs d ON sl.drugId = d.drugId " +
            "LEFT JOIN Batches b ON sl.batchId = b.batchId " +
            "LEFT JOIN InventoryLocations l ON sl.locationId = l.locationId " +
            "INNER JOIN Users u ON sl.userId = u.userId " +
            "WHERE sl.drugId = ? ORDER BY sl.transactionDate DESC";

    private static final String SEARCH = "SELECT sl.ledgerId, sl.drugId, sl.batchId, sl.locationId, sl.transactionType, "
            +
            "sl.quantity, sl.balanceBefore, sl.balanceAfter, sl.referenceType, sl.referenceId, " +
            "sl.reason, sl.userId, sl.transactionDate, " +
            "d.drugName, d.drugCode, b.batchNumber, l.locationName, u.fullname as userName " +
            "FROM StockLedger sl " +
            "INNER JOIN Drugs d ON sl.drugId = d.drugId " +
            "LEFT JOIN Batches b ON sl.batchId = b.batchId " +
            "LEFT JOIN InventoryLocations l ON sl.locationId = l.locationId " +
            "INNER JOIN Users u ON sl.userId = u.userId " +
            "WHERE sl.transactionDate BETWEEN ? AND ? " +
            "ORDER BY sl.transactionDate DESC";

    private static final String INSERT = "INSERT INTO StockLedger (drugId, batchId, locationId, transactionType, quantity, "
            +
            "balanceBefore, balanceAfter, referenceType, referenceId, reason, userId) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public List<StockLedgerDTO> findByDrug(int drugId) throws SQLException, ClassNotFoundException {
        List<StockLedgerDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_DRUG);
            ptm.setInt(1, drugId);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToLedger(rs));
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

    public List<StockLedgerDTO> searchByDateRange(java.util.Date fromDate, java.util.Date toDate)
            throws SQLException, ClassNotFoundException {
        List<StockLedgerDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(SEARCH);
            ptm.setTimestamp(1, new java.sql.Timestamp(fromDate.getTime()));
            ptm.setTimestamp(2, new java.sql.Timestamp(toDate.getTime()));
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToLedger(rs));
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

    public boolean insert(StockLedgerDTO ledger) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setInt(1, ledger.getDrugId());
            ptm.setObject(2, ledger.getBatchId());
            ptm.setObject(3, ledger.getLocationId());
            ptm.setString(4, ledger.getTransactionType());
            ptm.setInt(5, ledger.getQuantity());
            ptm.setObject(6, ledger.getBalanceBefore());
            ptm.setObject(7, ledger.getBalanceAfter());
            ptm.setString(8, ledger.getReferenceType());
            ptm.setObject(9, ledger.getReferenceId());
            ptm.setString(10, ledger.getReason());
            ptm.setInt(11, ledger.getUserId());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private StockLedgerDTO mapResultSetToLedger(ResultSet rs) throws SQLException {
        StockLedgerDTO ledger = new StockLedgerDTO();
        ledger.setLedgerId(rs.getInt("ledgerId"));
        ledger.setDrugId(rs.getInt("drugId"));
        ledger.setBatchId(rs.getObject("batchId") != null ? rs.getInt("batchId") : null);
        ledger.setLocationId(rs.getObject("locationId") != null ? rs.getInt("locationId") : null);
        ledger.setTransactionType(rs.getString("transactionType"));
        ledger.setQuantity(rs.getInt("quantity"));
        ledger.setBalanceBefore(rs.getObject("balanceBefore") != null ? rs.getInt("balanceBefore") : null);
        ledger.setBalanceAfter(rs.getObject("balanceAfter") != null ? rs.getInt("balanceAfter") : null);
        ledger.setReferenceType(rs.getString("referenceType"));
        ledger.setReferenceId(rs.getObject("referenceId") != null ? rs.getInt("referenceId") : null);
        ledger.setReason(rs.getString("reason"));
        ledger.setUserId(rs.getInt("userId"));
        ledger.setTransactionDate(rs.getTimestamp("transactionDate"));
        ledger.setDrugName(rs.getString("drugName"));
        ledger.setDrugCode(rs.getString("drugCode"));
        ledger.setBatchNumber(rs.getString("batchNumber"));
        ledger.setLocationName(rs.getString("locationName"));
        ledger.setUserName(rs.getString("userName"));
        return ledger;
    }
}
