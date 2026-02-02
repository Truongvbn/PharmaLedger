package com.pharmaledger.dao;

import com.pharmaledger.dto.InboundOrderDTO;
import com.pharmaledger.dto.InboundLineDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Inbound Order Data Access Object
 */
public class InboundOrderDAO {

    private static final String FIND_ALL = "SELECT io.orderId, io.orderCode, io.supplierId, io.orderDate, io.receivedDate, "
            +
            "io.status, io.totalAmount, io.notes, io.createdBy, io.approvedBy, io.createdAt, " +
            "s.supplierName, u1.fullname as createdByName, u2.fullname as approvedByName " +
            "FROM InboundOrders io " +
            "INNER JOIN Suppliers s ON io.supplierId = s.supplierId " +
            "INNER JOIN Users u1 ON io.createdBy = u1.userId " +
            "LEFT JOIN Users u2 ON io.approvedBy = u2.userId " +
            "ORDER BY io.orderDate DESC";

    private static final String FIND_BY_ID = "SELECT io.orderId, io.orderCode, io.supplierId, io.orderDate, io.receivedDate, "
            +
            "io.status, io.totalAmount, io.notes, io.createdBy, io.approvedBy, io.createdAt, " +
            "s.supplierName, u1.fullname as createdByName, u2.fullname as approvedByName " +
            "FROM InboundOrders io " +
            "INNER JOIN Suppliers s ON io.supplierId = s.supplierId " +
            "INNER JOIN Users u1 ON io.createdBy = u1.userId " +
            "LEFT JOIN Users u2 ON io.approvedBy = u2.userId " +
            "WHERE io.orderId = ?";

    private static final String FIND_LINES = "SELECT il.lineId, il.orderId, il.batchId, il.quantity, il.unitPrice, il.totalPrice, "
            +
            "b.batchNumber, d.drugName, d.drugCode " +
            "FROM InboundLines il " +
            "INNER JOIN Batches b ON il.batchId = b.batchId " +
            "INNER JOIN Drugs d ON b.drugId = d.drugId " +
            "WHERE il.orderId = ?";

    private static final String INSERT_ORDER = "INSERT INTO InboundOrders (orderCode, supplierId, orderDate, status, notes, createdBy) "
            +
            "VALUES (?, ?, ?, 'DRAFT', ?, ?)";

    private static final String INSERT_LINE = "INSERT INTO InboundLines (orderId, batchId, quantity, unitPrice, totalPrice) "
            +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_STATUS = "UPDATE InboundOrders SET status = ?, approvedBy = ?, receivedDate = GETDATE(), updatedAt = GETDATE() "
            +
            "WHERE orderId = ?";

    public List<InboundOrderDTO> findAll() throws SQLException, ClassNotFoundException {
        List<InboundOrderDTO> list = new ArrayList<>();
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

    public InboundOrderDTO findById(int orderId) throws SQLException, ClassNotFoundException {
        InboundOrderDTO order = null;
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

    public List<InboundLineDTO> findLines(int orderId) throws SQLException, ClassNotFoundException {
        List<InboundLineDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_LINES);
            ptm.setInt(1, orderId);
            rs = ptm.executeQuery();
            while (rs.next()) {
                InboundLineDTO line = new InboundLineDTO();
                line.setLineId(rs.getInt("lineId"));
                line.setOrderId(rs.getInt("orderId"));
                line.setBatchId(rs.getInt("batchId"));
                line.setQuantity(rs.getInt("quantity"));
                line.setUnitPrice(rs.getBigDecimal("unitPrice"));
                line.setTotalPrice(rs.getBigDecimal("totalPrice"));
                line.setBatchNumber(rs.getString("batchNumber"));
                line.setDrugName(rs.getString("drugName"));
                line.setDrugCode(rs.getString("drugCode"));
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

    public int insert(InboundOrderDTO order) throws SQLException, ClassNotFoundException {
        int orderId = -1;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            ptm.setString(1, order.getOrderCode());
            ptm.setInt(2, order.getSupplierId());
            ptm.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            ptm.setString(4, order.getNotes());
            ptm.setInt(5, order.getCreatedBy());
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

    public boolean insertLine(InboundLineDTO line) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT_LINE);
            ptm.setInt(1, line.getOrderId());
            ptm.setInt(2, line.getBatchId());
            ptm.setInt(3, line.getQuantity());
            ptm.setBigDecimal(4, line.getUnitPrice());
            ptm.setBigDecimal(5, line.getTotalPrice());
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

    private InboundOrderDTO mapResultSetToOrder(ResultSet rs) throws SQLException {
        InboundOrderDTO order = new InboundOrderDTO();
        order.setOrderId(rs.getInt("orderId"));
        order.setOrderCode(rs.getString("orderCode"));
        order.setSupplierId(rs.getInt("supplierId"));
        order.setOrderDate(rs.getDate("orderDate"));
        order.setReceivedDate(rs.getDate("receivedDate"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("totalAmount"));
        order.setNotes(rs.getString("notes"));
        order.setCreatedBy(rs.getInt("createdBy"));
        order.setApprovedBy(rs.getObject("approvedBy") != null ? rs.getInt("approvedBy") : null);
        order.setCreatedAt(rs.getTimestamp("createdAt"));
        order.setSupplierName(rs.getString("supplierName"));
        order.setCreatedByName(rs.getString("createdByName"));
        order.setApprovedByName(rs.getString("approvedByName"));
        return order;
    }
}
