package com.pharmaledger.dao;

import com.pharmaledger.dto.AlertEventDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Alert Event Data Access Object
 */
public class AlertEventDAO {

    private static final String FIND_ALL = "SELECT ae.eventId, ae.ruleId, ae.drugId, ae.batchId, ae.alertType, ae.severity, "
            +
            "ae.title, ae.description, ae.status, ae.triggeredAt, ae.acknowledgedAt, ae.resolvedAt, " +
            "ae.acknowledgedBy, ar.ruleName, d.drugName, b.batchNumber, u.fullname as acknowledgedByName " +
            "FROM AlertEvents ae " +
            "INNER JOIN AlertRules ar ON ae.ruleId = ar.ruleId " +
            "LEFT JOIN Drugs d ON ae.drugId = d.drugId " +
            "LEFT JOIN Batches b ON ae.batchId = b.batchId " +
            "LEFT JOIN Users u ON ae.acknowledgedBy = u.userId " +
            "ORDER BY ae.triggeredAt DESC";

    private static final String FIND_OPEN = "SELECT ae.eventId, ae.ruleId, ae.drugId, ae.batchId, ae.alertType, ae.severity, "
            +
            "ae.title, ae.description, ae.status, ae.triggeredAt, " +
            "ar.ruleName, d.drugName, b.batchNumber " +
            "FROM AlertEvents ae " +
            "INNER JOIN AlertRules ar ON ae.ruleId = ar.ruleId " +
            "LEFT JOIN Drugs d ON ae.drugId = d.drugId " +
            "LEFT JOIN Batches b ON ae.batchId = b.batchId " +
            "WHERE ae.status = 'OPEN' ORDER BY ae.severity DESC, ae.triggeredAt DESC";

    private static final String INSERT = "INSERT INTO AlertEvents (ruleId, drugId, batchId, alertType, severity, title, description) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_STATUS = "UPDATE AlertEvents SET status = ?, acknowledgedBy = ?, acknowledgedAt = GETDATE() WHERE eventId = ?";

    public List<AlertEventDTO> findAll() throws SQLException, ClassNotFoundException {
        List<AlertEventDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEvent(rs));
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

    public List<AlertEventDTO> findOpen() throws SQLException, ClassNotFoundException {
        List<AlertEventDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_OPEN);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEventBasic(rs));
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

    public boolean insert(AlertEventDTO event) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setInt(1, event.getRuleId());
            ptm.setObject(2, event.getDrugId());
            ptm.setObject(3, event.getBatchId());
            ptm.setString(4, event.getAlertType());
            ptm.setString(5, event.getSeverity());
            ptm.setString(6, event.getTitle());
            ptm.setString(7, event.getDescription());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean updateStatus(int eventId, String status, Integer acknowledgedBy)
            throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE_STATUS);
            ptm.setString(1, status);
            ptm.setObject(2, acknowledgedBy);
            ptm.setInt(3, eventId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private AlertEventDTO mapResultSetToEvent(ResultSet rs) throws SQLException {
        AlertEventDTO event = new AlertEventDTO();
        event.setEventId(rs.getInt("eventId"));
        event.setRuleId(rs.getInt("ruleId"));
        event.setDrugId(rs.getObject("drugId") != null ? rs.getInt("drugId") : null);
        event.setBatchId(rs.getObject("batchId") != null ? rs.getInt("batchId") : null);
        event.setAlertType(rs.getString("alertType"));
        event.setSeverity(rs.getString("severity"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setStatus(rs.getString("status"));
        event.setTriggeredAt(rs.getTimestamp("triggeredAt"));
        event.setAcknowledgedAt(rs.getTimestamp("acknowledgedAt"));
        event.setResolvedAt(rs.getTimestamp("resolvedAt"));
        event.setAcknowledgedBy(rs.getObject("acknowledgedBy") != null ? rs.getInt("acknowledgedBy") : null);
        event.setRuleName(rs.getString("ruleName"));
        event.setDrugName(rs.getString("drugName"));
        event.setBatchNumber(rs.getString("batchNumber"));
        event.setAcknowledgedByName(rs.getString("acknowledgedByName"));
        return event;
    }

    private AlertEventDTO mapResultSetToEventBasic(ResultSet rs) throws SQLException {
        AlertEventDTO event = new AlertEventDTO();
        event.setEventId(rs.getInt("eventId"));
        event.setRuleId(rs.getInt("ruleId"));
        event.setDrugId(rs.getObject("drugId") != null ? rs.getInt("drugId") : null);
        event.setBatchId(rs.getObject("batchId") != null ? rs.getInt("batchId") : null);
        event.setAlertType(rs.getString("alertType"));
        event.setSeverity(rs.getString("severity"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setStatus(rs.getString("status"));
        event.setTriggeredAt(rs.getTimestamp("triggeredAt"));
        event.setRuleName(rs.getString("ruleName"));
        event.setDrugName(rs.getString("drugName"));
        event.setBatchNumber(rs.getString("batchNumber"));
        return event;
    }
}
