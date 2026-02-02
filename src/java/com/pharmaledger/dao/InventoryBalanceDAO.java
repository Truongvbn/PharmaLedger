package com.pharmaledger.dao;

import com.pharmaledger.dto.InventoryBalanceDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory Balance Data Access Object
 */
public class InventoryBalanceDAO {

    private static final String FIND_ALL_WITH_DETAILS = "SELECT ib.balanceId, ib.batchId, ib.locationId, ib.quantity, ib.updatedAt, "
            +
            "b.batchNumber, b.expiryDate, d.drugId, d.drugName, d.drugCode, l.locationName " +
            "FROM InventoryBalances ib " +
            "INNER JOIN Batches b ON ib.batchId = b.batchId " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "INNER JOIN InventoryLocations l ON ib.locationId = l.locationId " +
            "WHERE ib.quantity > 0 ORDER BY d.drugName, b.expiryDate";

    private static final String SEARCH_STOCK = "SELECT ib.balanceId, ib.batchId, ib.locationId, ib.quantity, ib.updatedAt, "
            +
            "b.batchNumber, b.expiryDate, d.drugId, d.drugName, d.drugCode, l.locationName " +
            "FROM InventoryBalances ib " +
            "INNER JOIN Batches b ON ib.batchId = b.batchId " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "INNER JOIN InventoryLocations l ON ib.locationId = l.locationId " +
            "WHERE ib.quantity > 0 " +
            "AND (d.drugCode LIKE ? OR d.drugName LIKE ? OR d.activeIngredient LIKE ?) " +
            "ORDER BY d.drugName, b.expiryDate";

    private static final String FIND_LOW_STOCK = "SELECT d.drugId, d.drugCode, d.drugName, d.minStock, " +
            "ISNULL(SUM(ib.quantity), 0) as totalStock " +
            "FROM Drugs d " +
            "LEFT JOIN Batches b ON d.drugId = b.drugId " +
            "LEFT JOIN InventoryBalances ib ON b.batchId = ib.batchId " +
            "WHERE d.isActive = 1 " +
            "GROUP BY d.drugId, d.drugCode, d.drugName, d.minStock " +
            "HAVING ISNULL(SUM(ib.quantity), 0) <= d.minStock " +
            "ORDER BY d.drugName";

    private static final String UPDATE_QUANTITY = "UPDATE InventoryBalances SET quantity = ?, updatedAt = GETDATE() WHERE balanceId = ?";

    private static final String INSERT = "INSERT INTO InventoryBalances (batchId, locationId, quantity) VALUES (?, ?, ?)";

    public List<InventoryBalanceDTO> findAllWithDetails() throws SQLException, ClassNotFoundException {
        List<InventoryBalanceDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL_WITH_DETAILS);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToBalance(rs));
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

    public List<InventoryBalanceDTO> searchStock(String keyword) throws SQLException, ClassNotFoundException {
        List<InventoryBalanceDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(SEARCH_STOCK);
            String searchPattern = "%" + keyword + "%";
            ptm.setString(1, searchPattern);
            ptm.setString(2, searchPattern);
            ptm.setString(3, searchPattern);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToBalance(rs));
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

    public boolean insert(InventoryBalanceDTO balance) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setInt(1, balance.getBatchId());
            ptm.setInt(2, balance.getLocationId());
            ptm.setInt(3, balance.getQuantity());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean updateQuantity(int balanceId, int quantity) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE_QUANTITY);
            ptm.setInt(1, quantity);
            ptm.setInt(2, balanceId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private InventoryBalanceDTO mapResultSetToBalance(ResultSet rs) throws SQLException {
        InventoryBalanceDTO balance = new InventoryBalanceDTO();
        balance.setBalanceId(rs.getInt("balanceId"));
        balance.setBatchId(rs.getInt("batchId"));
        balance.setLocationId(rs.getInt("locationId"));
        balance.setQuantity(rs.getInt("quantity"));
        balance.setUpdatedAt(rs.getTimestamp("updatedAt"));
        balance.setBatchNumber(rs.getString("batchNumber"));
        balance.setExpiryDate(rs.getDate("expiryDate"));
        balance.setDrugName(rs.getString("drugName"));
        balance.setDrugCode(rs.getString("drugCode"));
        balance.setLocationName(rs.getString("locationName"));
        return balance;
    }
}
