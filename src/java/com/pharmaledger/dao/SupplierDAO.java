package com.pharmaledger.dao;

import com.pharmaledger.dto.SupplierDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier Data Access Object
 */
public class SupplierDAO {

    private static final String FIND_BY_ID = "SELECT supplierId, supplierCode, supplierName, contactPerson, phone, email, "
            +
            "address, taxCode, contractPrice, isActive, createdAt " +
            "FROM Suppliers WHERE supplierId = ?";

    private static final String FIND_ALL = "SELECT supplierId, supplierCode, supplierName, contactPerson, phone, email, "
            +
            "address, taxCode, contractPrice, isActive, createdAt " +
            "FROM Suppliers WHERE isActive = 1 ORDER BY supplierName";

    private static final String INSERT = "INSERT INTO Suppliers (supplierCode, supplierName, contactPerson, phone, email, address, taxCode, contractPrice) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE Suppliers SET supplierCode = ?, supplierName = ?, contactPerson = ?, "
            +
            "phone = ?, email = ?, address = ?, taxCode = ?, contractPrice = ?, updatedAt = GETDATE() " +
            "WHERE supplierId = ?";

    private static final String DELETE = "UPDATE Suppliers SET isActive = 0, updatedAt = GETDATE() WHERE supplierId = ?";

    public SupplierDTO findById(int supplierId) throws SQLException, ClassNotFoundException {
        SupplierDTO supplier = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, supplierId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                supplier = mapResultSetToSupplier(rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return supplier;
    }

    public List<SupplierDTO> findAll() throws SQLException, ClassNotFoundException {
        List<SupplierDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToSupplier(rs));
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

    public boolean insert(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setString(1, supplier.getSupplierCode());
            ptm.setString(2, supplier.getSupplierName());
            ptm.setString(3, supplier.getContactPerson());
            ptm.setString(4, supplier.getPhone());
            ptm.setString(5, supplier.getEmail());
            ptm.setString(6, supplier.getAddress());
            ptm.setString(7, supplier.getTaxCode());
            ptm.setBigDecimal(8, supplier.getContractPrice());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean update(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, supplier.getSupplierCode());
            ptm.setString(2, supplier.getSupplierName());
            ptm.setString(3, supplier.getContactPerson());
            ptm.setString(4, supplier.getPhone());
            ptm.setString(5, supplier.getEmail());
            ptm.setString(6, supplier.getAddress());
            ptm.setString(7, supplier.getTaxCode());
            ptm.setBigDecimal(8, supplier.getContractPrice());
            ptm.setInt(9, supplier.getSupplierId());
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    public boolean delete(int supplierId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(DELETE);
            ptm.setInt(1, supplierId);
            result = ptm.executeUpdate() > 0;
        } finally {
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return result;
    }

    private SupplierDTO mapResultSetToSupplier(ResultSet rs) throws SQLException {
        SupplierDTO supplier = new SupplierDTO();
        supplier.setSupplierId(rs.getInt("supplierId"));
        supplier.setSupplierCode(rs.getString("supplierCode"));
        supplier.setSupplierName(rs.getString("supplierName"));
        supplier.setContactPerson(rs.getString("contactPerson"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        supplier.setTaxCode(rs.getString("taxCode"));
        supplier.setContractPrice(rs.getBigDecimal("contractPrice"));
        supplier.setActive(rs.getBoolean("isActive"));
        supplier.setCreatedAt(rs.getTimestamp("createdAt"));
        return supplier;
    }
}
