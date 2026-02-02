package com.pharmaledger.dao;

import com.pharmaledger.dto.CaseTicketDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Case Ticket Data Access Object
 */
public class CaseTicketDAO {

    private static final String FIND_ALL = "SELECT ct.caseId, ct.eventId, ct.caseCode, ct.assigneeId, ct.status, ct.priority, "
            +
            "ct.resolution, ct.evidence, ct.createdAt, ct.updatedAt, ct.closedAt, ct.closedBy, " +
            "u1.fullname as assigneeName, u2.fullname as closedByName, " +
            "ae.title as alertTitle, ae.alertType " +
            "FROM CaseTickets ct " +
            "INNER JOIN AlertEvents ae ON ct.eventId = ae.eventId " +
            "LEFT JOIN Users u1 ON ct.assigneeId = u1.userId " +
            "LEFT JOIN Users u2 ON ct.closedBy = u2.userId " +
            "ORDER BY ct.createdAt DESC";

    private static final String FIND_OPEN = "SELECT ct.caseId, ct.eventId, ct.caseCode, ct.assigneeId, ct.status, ct.priority, "
            +
            "ct.createdAt, u.fullname as assigneeName, ae.title as alertTitle, ae.alertType " +
            "FROM CaseTickets ct " +
            "INNER JOIN AlertEvents ae ON ct.eventId = ae.eventId " +
            "LEFT JOIN Users u ON ct.assigneeId = u.userId " +
            "WHERE ct.status IN ('OPEN', 'IN_PROGRESS') ORDER BY ct.priority DESC, ct.createdAt";

    private static final String INSERT = "INSERT INTO CaseTickets (eventId, caseCode, priority) VALUES (?, ?, ?)";

    private static final String ASSIGN = "UPDATE CaseTickets SET assigneeId = ?, status = 'IN_PROGRESS', updatedAt = GETDATE() "
            +
            "WHERE caseId = ?";

    private static final String CLOSE = "UPDATE CaseTickets SET status = 'CLOSED', resolution = ?, evidence = ?, " +
            "closedAt = GETDATE(), closedBy = ?, updatedAt = GETDATE() WHERE caseId = ?";

    public List<CaseTicketDTO> findAll() throws SQLException, ClassNotFoundException {
        List<CaseTicketDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCase(rs));
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

    public List<CaseTicketDTO> findOpen() throws SQLException, ClassNotFoundException {
        List<CaseTicketDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_OPEN);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCaseBasic(rs));
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

    public boolean insert(CaseTicketDTO ticket) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setInt(1, ticket.getEventId());
            ptm.setString(2, ticket.getCaseCode());
            ptm.setString(3, ticket.getPriority());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean assign(int caseId, int assigneeId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(ASSIGN);
            ptm.setInt(1, assigneeId);
            ptm.setInt(2, caseId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean close(int caseId, String resolution, String evidence, int closedBy)
            throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(CLOSE);
            ptm.setString(1, resolution);
            ptm.setString(2, evidence);
            ptm.setInt(3, closedBy);
            ptm.setInt(4, caseId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private CaseTicketDTO mapResultSetToCase(ResultSet rs) throws SQLException {
        CaseTicketDTO ticket = new CaseTicketDTO();
        ticket.setCaseId(rs.getInt("caseId"));
        ticket.setEventId(rs.getInt("eventId"));
        ticket.setCaseCode(rs.getString("caseCode"));
        ticket.setAssigneeId(rs.getObject("assigneeId") != null ? rs.getInt("assigneeId") : null);
        ticket.setStatus(rs.getString("status"));
        ticket.setPriority(rs.getString("priority"));
        ticket.setResolution(rs.getString("resolution"));
        ticket.setEvidence(rs.getString("evidence"));
        ticket.setCreatedAt(rs.getTimestamp("createdAt"));
        ticket.setUpdatedAt(rs.getTimestamp("updatedAt"));
        ticket.setClosedAt(rs.getTimestamp("closedAt"));
        ticket.setClosedBy(rs.getObject("closedBy") != null ? rs.getInt("closedBy") : null);
        ticket.setAssigneeName(rs.getString("assigneeName"));
        ticket.setClosedByName(rs.getString("closedByName"));
        ticket.setAlertTitle(rs.getString("alertTitle"));
        ticket.setAlertType(rs.getString("alertType"));
        return ticket;
    }

    private CaseTicketDTO mapResultSetToCaseBasic(ResultSet rs) throws SQLException {
        CaseTicketDTO ticket = new CaseTicketDTO();
        ticket.setCaseId(rs.getInt("caseId"));
        ticket.setEventId(rs.getInt("eventId"));
        ticket.setCaseCode(rs.getString("caseCode"));
        ticket.setAssigneeId(rs.getObject("assigneeId") != null ? rs.getInt("assigneeId") : null);
        ticket.setStatus(rs.getString("status"));
        ticket.setPriority(rs.getString("priority"));
        ticket.setCreatedAt(rs.getTimestamp("createdAt"));
        ticket.setAssigneeName(rs.getString("assigneeName"));
        ticket.setAlertTitle(rs.getString("alertTitle"));
        ticket.setAlertType(rs.getString("alertType"));
        return ticket;
    }
}
