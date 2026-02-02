package com.pharmaledger.dao;

import com.pharmaledger.dto.OutboundOrderDTO;
import com.pharmaledger.dto.OutboundLineDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Outbound Order Data Access Object
 */
public class OutboundOrderDAO {

    private static final String FIND_ALL = "SELECT oo.orderId, oo.orderCode, oo.orderDate, oo.purpose, oo.destinationDept, "
            +
            "oo.status, oo.notes, oo.createdBy, oo.approvedBy, oo.createdAt, " +
            "u1.fullname as createdByName, u2.fullname as approvedByName " +
            "FROM OutboundOrders oo " +
            "INNER JOIN Users u1 ON oo.createdBy = u1.userId " +
            "LEFT JOIN Users u2 ON oo.approvedBy = u2.userId " +
            "ORDER BY oo.orderDate DESC";

    private static final String FIND_BY_ID = "SELECT oo.orderId, oo.orderCode, oo.orderDate, oo.purpose, oo.destinationDept, "
            +
            "oo.status, oo.notes, oo.createdBy, oo.approvedBy, oo.createdAt, " +
            "u1.fullname as createdByName, u2.fullname as approvedByName " +
            "FROM OutboundOrders oo " +
            "INNER JOIN Users u1 ON oo.createdBy = u1.userId " +
            "LEFT JOIN Users u2 ON oo.approvedBy = u2.userId " +
            "WHERE oo.orderId = ?";

    private static final String FIND_LINES = "SELECT ol.lineId, ol.orderId, ol.batchId, ol.locationId, ol.quantity, " +
            "b.batchNumber, d.drugName, d.drugCode, l.locationName " +
            "FROM OutboundLines ol " +
            "INNER JOIN Batches b ON ol.batchId = b.batchId " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "INNER JOIN InventoryLocations l ON ol.locationId = l.locationId " +
            "WHERE ol.orderId = ?";

    private static final String INSERT_ORDER = "INSERT INTO OutboundOrders (orderCode, orderDate, purpose, destinationDept, status, notes, createdBy) "
            +
            "VALUES (?, ?, ?, ?, 'DRAFT', ?, ?)";

    private static final String INSERT_LINE = "INSERT INTO OutboundLines (orderId, batchId, locationId, quantity) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_STATUS = "UPDATE OutboundOrders SET status = ?, approvedBy = ?, updatedAt = GETDATE() WHERE orderId = ?";

    public List<OutboundOrderDTO> findAll() throws SQLException, ClassNotFoundException {
        List<OutboundOrderDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
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

    public OutboundOrderDTO findById(int orderId) throws SQLException, ClassNotFoundException {
        OutboundOrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, orderId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                order = mapResultSetToOrder(rs);
                order.setLines(findLines(orderId));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return order;
    }

    public List<OutboundLineDTO> findLines(int orderId) throws SQLException, ClassNotFoundException {
        List<OutboundLineDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_LINES);
            ptm.setInt(1, orderId);
            rs = ptm.executeQuery();
            while (rs.next()) {
                OutboundLineDTO line = new OutboundLineDTO();
                line.setLineId(rs.getInt("lineId"));
                line.setOrderId(rs.getInt("orderId"));
                line.setBatchId(rs.getInt("batchId"));
                line.setLocationId(rs.getInt("locationId"));
                line.setQuantity(rs.getInt("quantity"));
                line.setBatchNumber(rs.getString("batchNumber"));
                line.setDrugName(rs.getString("drugName"));
                line.setDrugCode(rs.getString("drugCode"));
                line.setLocationName(rs.getString("locationName"));
                list.add(line);
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

    public int insert(OutboundOrderDTO order) throws SQLException, ClassNotFoundException {
        int orderId = -1;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            ptm.setString(1, order.getOrderCode());
            ptm.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            ptm.setString(3, order.getPurpose());
            ptm.setString(4, order.getDestinationDept());
            ptm.setString(5, order.getNotes());
            ptm.setInt(6, order.getCreatedBy());
            ptm.executeUpdate();
            rs = ptm.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return orderId;
    }

    public boolean insertLine(OutboundLineDTO line) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_LINE);
            ptm.setInt(1, line.getOrderId());
            ptm.setInt(2, line.getBatchId());
            ptm.setInt(3, line.getLocationId());
            ptm.setInt(4, line.getQuantity());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean updateStatus(int orderId, String status, Integer approvedBy)
            throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE_STATUS);
            ptm.setString(1, status);
            ptm.setObject(2, approvedBy);
            ptm.setInt(3, orderId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private OutboundOrderDTO mapResultSetToOrder(ResultSet rs) throws SQLException {
        OutboundOrderDTO order = new OutboundOrderDTO();
        order.setOrderId(rs.getInt("orderId"));
        order.setOrderCode(rs.getString("orderCode"));
        order.setOrderDate(rs.getDate("orderDate"));
        order.setPurpose(rs.getString("purpose"));
        order.setDestinationDept(rs.getString("destinationDept"));
        order.setStatus(rs.getString("status"));
        order.setNotes(rs.getString("notes"));
        order.setCreatedBy(rs.getInt("createdBy"));
        order.setApprovedBy(rs.getObject("approvedBy") != null ? rs.getInt("approvedBy") : null);
        order.setCreatedAt(rs.getTimestamp("createdAt"));
        order.setCreatedByName(rs.getString("createdByName"));
        order.setApprovedByName(rs.getString("approvedByName"));
        return order;
    }
}
