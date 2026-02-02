package com.pharmaledger.dao;

import com.pharmaledger.dto.InventoryLocationDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory Location Data Access Object
 */
public class InventoryLocationDAO {

    private static final String FIND_BY_ID = "SELECT locationId, locationCode, locationName, locationType, description, isActive, createdAt "
            +
            "FROM InventoryLocations WHERE locationId = ?";

    private static final String FIND_ALL = "SELECT locationId, locationCode, locationName, locationType, description, isActive, createdAt "
            +
            "FROM InventoryLocations WHERE isActive = 1 ORDER BY locationCode";

    private static final String INSERT = "INSERT INTO InventoryLocations (locationCode, locationName, locationType, description) "
            +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE InventoryLocations SET locationCode = ?, locationName = ?, locationType = ?, description = ? "
            +
            "WHERE locationId = ?";

    private static final String DELETE = "UPDATE InventoryLocations SET isActive = 0 WHERE locationId = ?";

    public InventoryLocationDTO findById(int locationId) throws SQLException, ClassNotFoundException {
        InventoryLocationDTO location = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, locationId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                location = mapResultSetToLocation(rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return location;
    }

    public List<InventoryLocationDTO> findAll() throws SQLException, ClassNotFoundException {
        List<InventoryLocationDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToLocation(rs));
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

    public boolean insert(InventoryLocationDTO location) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setString(1, location.getLocationCode());
            ptm.setString(2, location.getLocationName());
            ptm.setString(3, location.getLocationType());
            ptm.setString(4, location.getDescription());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean update(InventoryLocationDTO location) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, location.getLocationCode());
            ptm.setString(2, location.getLocationName());
            ptm.setString(3, location.getLocationType());
            ptm.setString(4, location.getDescription());
            ptm.setInt(5, location.getLocationId());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean delete(int locationId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(DELETE);
            ptm.setInt(1, locationId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private InventoryLocationDTO mapResultSetToLocation(ResultSet rs) throws SQLException {
        InventoryLocationDTO location = new InventoryLocationDTO();
        location.setLocationId(rs.getInt("locationId"));
        location.setLocationCode(rs.getString("locationCode"));
        location.setLocationName(rs.getString("locationName"));
        location.setLocationType(rs.getString("locationType"));
        location.setDescription(rs.getString("description"));
        location.setActive(rs.getBoolean("isActive"));
        location.setCreatedAt(rs.getTimestamp("createdAt"));
        return location;
    }
}
