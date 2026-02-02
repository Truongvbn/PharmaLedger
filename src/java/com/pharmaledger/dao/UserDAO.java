package com.pharmaledger.dao;

import com.pharmaledger.dto.UserDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User Data Access Object
 */
public class UserDAO {

    private static final String LOGIN = "SELECT userId, username, fullname, email, phone, roleId, isActive " +
            "FROM Users WHERE username = ? AND password = ? AND isActive = 1";

    private static final String FIND_BY_ID = "SELECT userId, username, fullname, email, phone, roleId, isActive, createdAt "
            +
            "FROM Users WHERE userId = ?";

    private static final String FIND_ALL = "SELECT userId, username, fullname, email, phone, roleId, isActive, createdAt "
            +
            "FROM Users ORDER BY userId";

    private static final String INSERT = "INSERT INTO Users (username, password, fullname, email, phone, roleId) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE Users SET fullname = ?, email = ?, phone = ?, roleId = ?, updatedAt = GETDATE() "
            +
            "WHERE userId = ?";

    private static final String DELETE = "UPDATE Users SET isActive = 0, updatedAt = GETDATE() WHERE userId = ?";

    /**
     * Check user login
     */
    public UserDTO checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(LOGIN);
            ptm.setString(1, username);
            ptm.setString(2, password);
            rs = ptm.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRoleId(rs.getString("roleId"));
                user.setActive(rs.getBoolean("isActive"));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return user;
    }

    /**
     * Find user by ID
     */
    public UserDTO findById(int userId) throws SQLException, ClassNotFoundException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, userId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return user;
    }

    /**
     * Find all users
     */
    public List<UserDTO> findAll() throws SQLException, ClassNotFoundException {
        List<UserDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
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
     * Insert new user
     */
    public boolean insert(UserDTO user) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setString(1, user.getUsername());
            ptm.setString(2, user.getPassword());
            ptm.setString(3, user.getFullname());
            ptm.setString(4, user.getEmail());
            ptm.setString(5, user.getPhone());
            ptm.setString(6, user.getRoleId());
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
     * Update user
     */
    public boolean update(UserDTO user) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, user.getFullname());
            ptm.setString(2, user.getEmail());
            ptm.setString(3, user.getPhone());
            ptm.setString(4, user.getRoleId());
            ptm.setInt(5, user.getUserId());
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
     * Soft delete user
     */
    public boolean delete(int userId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(DELETE);
            ptm.setInt(1, userId);
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
     * Map ResultSet to UserDTO
     */
    private UserDTO mapResultSetToUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setUserId(rs.getInt("userId"));
        user.setUsername(rs.getString("username"));
        user.setFullname(rs.getString("fullname"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getString("roleId"));
        user.setActive(rs.getBoolean("isActive"));
        user.setCreatedAt(rs.getTimestamp("createdAt"));
        return user;
    }
}
