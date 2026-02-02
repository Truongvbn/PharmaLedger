package com.pharmaledger.dao;

import com.pharmaledger.dto.AlertRuleDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Alert Rule Data Access Object
 */
public class AlertRuleDAO {

    private static final String FIND_ALL = "SELECT ruleId, ruleName, ruleType, threshold, thresholdUnit, severity, isActive, createdAt "
            +
            "FROM AlertRules ORDER BY ruleType";

    private static final String FIND_ACTIVE = "SELECT ruleId, ruleName, ruleType, threshold, thresholdUnit, severity, isActive, createdAt "
            +
            "FROM AlertRules WHERE isActive = 1";

    private static final String INSERT = "INSERT INTO AlertRules (ruleName, ruleType, threshold, thresholdUnit, severity) "
            +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE AlertRules SET ruleName = ?, ruleType = ?, threshold = ?, thresholdUnit = ?, "
            +
            "severity = ?, isActive = ?, updatedAt = GETDATE() WHERE ruleId = ?";

    public List<AlertRuleDTO> findAll() throws SQLException, ClassNotFoundException {
        List<AlertRuleDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToRule(rs));
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

    public List<AlertRuleDTO> findActive() throws SQLException, ClassNotFoundException {
        List<AlertRuleDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ACTIVE);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToRule(rs));
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

    public boolean insert(AlertRuleDTO rule) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setString(1, rule.getRuleName());
            ptm.setString(2, rule.getRuleType());
            ptm.setObject(3, rule.getThreshold());
            ptm.setString(4, rule.getThresholdUnit());
            ptm.setString(5, rule.getSeverity());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean update(AlertRuleDTO rule) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, rule.getRuleName());
            ptm.setString(2, rule.getRuleType());
            ptm.setObject(3, rule.getThreshold());
            ptm.setString(4, rule.getThresholdUnit());
            ptm.setString(5, rule.getSeverity());
            ptm.setBoolean(6, rule.isActive());
            ptm.setInt(7, rule.getRuleId());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private AlertRuleDTO mapResultSetToRule(ResultSet rs) throws SQLException {
        AlertRuleDTO rule = new AlertRuleDTO();
        rule.setRuleId(rs.getInt("ruleId"));
        rule.setRuleName(rs.getString("ruleName"));
        rule.setRuleType(rs.getString("ruleType"));
        rule.setThreshold(rs.getObject("threshold") != null ? rs.getInt("threshold") : null);
        rule.setThresholdUnit(rs.getString("thresholdUnit"));
        rule.setSeverity(rs.getString("severity"));
        rule.setActive(rs.getBoolean("isActive"));
        rule.setCreatedAt(rs.getTimestamp("createdAt"));
        return rule;
    }
}
