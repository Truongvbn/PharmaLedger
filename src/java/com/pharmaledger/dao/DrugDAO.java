package com.pharmaledger.dao;

import com.pharmaledger.dto.DrugDTO;
import com.pharmaledger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Drug Data Access Object
 */
public class DrugDAO {

    private static final String FIND_BY_ID = "SELECT drugId, drugCode, drugName, activeIngredient, dosageForm, strength, "
            +
            "unit, minStock, description, isActive, createdAt, updatedAt " +
            "FROM Drugs WHERE drugId = ?";

    private static final String FIND_ALL = "SELECT drugId, drugCode, drugName, activeIngredient, dosageForm, strength, "
            +
            "unit, minStock, description, isActive, createdAt " +
            "FROM Drugs WHERE isActive = 1 ORDER BY drugName";

    private static final String SEARCH = "SELECT drugId, drugCode, drugName, activeIngredient, dosageForm, strength, " +
            "unit, minStock, description, isActive, createdAt " +
            "FROM Drugs WHERE isActive = 1 " +
            "AND (drugCode LIKE ? OR drugName LIKE ? OR activeIngredient LIKE ?) " +
            "ORDER BY drugName";

    private static final String INSERT = "INSERT INTO Drugs (drugCode, drugName, activeIngredient, dosageForm, strength, unit, minStock, description) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE Drugs SET drugCode = ?, drugName = ?, activeIngredient = ?, dosageForm = ?, "
            +
            "strength = ?, unit = ?, minStock = ?, description = ?, updatedAt = GETDATE() " +
            "WHERE drugId = ?";

    private static final String DELETE = "UPDATE Drugs SET isActive = 0, updatedAt = GETDATE() WHERE drugId = ?";

    /**
     * Find drug by ID
     */
    public DrugDTO findById(int drugId) throws SQLException, ClassNotFoundException {
        DrugDTO drug = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_BY_ID);
            ptm.setInt(1, drugId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                drug = mapResultSetToDrug(rs);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ptm != null)
                ptm.close();
            if (conn != null)
                conn.close();
        }
        return drug;
    }

    /**
     * Find all active drugs
     */
    public List<DrugDTO> findAll() throws SQLException, ClassNotFoundException {
        List<DrugDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(FIND_ALL);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDrug(rs));
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
     * Search drugs by code, name, or active ingredient
     */
    public List<DrugDTO> search(String keyword) throws SQLException, ClassNotFoundException {
        List<DrugDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(SEARCH);
            String searchPattern = "%" + keyword + "%";
            ptm.setString(1, searchPattern);
            ptm.setString(2, searchPattern);
            ptm.setString(3, searchPattern);
            rs = ptm.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDrug(rs));
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
     * Insert new drug
     */
    public boolean insert(DrugDTO drug) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(INSERT);
            ptm.setString(1, drug.getDrugCode());
            ptm.setString(2, drug.getDrugName());
            ptm.setString(3, drug.getActiveIngredient());
            ptm.setString(4, drug.getDosageForm());
            ptm.setString(5, drug.getStrength());
            ptm.setString(6, drug.getUnit());
            ptm.setInt(7, drug.getMinStock());
            ptm.setString(8, drug.getDescription());
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
     * Update drug
     */
    public boolean update(DrugDTO drug) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(UPDATE);
            ptm.setString(1, drug.getDrugCode());
            ptm.setString(2, drug.getDrugName());
            ptm.setString(3, drug.getActiveIngredient());
            ptm.setString(4, drug.getDosageForm());
            ptm.setString(5, drug.getStrength());
            ptm.setString(6, drug.getUnit());
            ptm.setInt(7, drug.getMinStock());
            ptm.setString(8, drug.getDescription());
            ptm.setInt(9, drug.getDrugId());
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
     * Soft delete drug
     */
    public boolean delete(int drugId) throws SQLException, ClassNotFoundException {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement(DELETE);
            ptm.setInt(1, drugId);
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
     * Map ResultSet to DrugDTO
     */
    private DrugDTO mapResultSetToDrug(ResultSet rs) throws SQLException {
        DrugDTO drug = new DrugDTO();
        drug.setDrugId(rs.getInt("drugId"));
        drug.setDrugCode(rs.getString("drugCode"));
        drug.setDrugName(rs.getString("drugName"));
        drug.setActiveIngredient(rs.getString("activeIngredient"));
        drug.setDosageForm(rs.getString("dosageForm"));
        drug.setStrength(rs.getString("strength"));
        drug.setUnit(rs.getString("unit"));
        drug.setMinStock(rs.getInt("minStock"));
        drug.setDescription(rs.getString("description"));
        drug.setActive(rs.getBoolean("isActive"));
        drug.setCreatedAt(rs.getTimestamp("createdAt"));
        return drug;
    }
}
