package com.pharmaledger.controllers;

import com.pharmaledger.dao.SupplierDAO;
import com.pharmaledger.dto.SupplierDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Supplier Controller - CRUD operations for suppliers
 */
@WebServlet(name = "SupplierController", urlPatterns = { "/SupplierController" })
public class SupplierController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_SUPPLIER_LIST;
        String action = request.getParameter("action");

        try {
            SupplierDAO supplierDAO = new SupplierDAO();

            if ("SupplierList".equals(action)) {
                List<SupplierDTO> suppliers = supplierDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, suppliers);
                url = Constants.PAGE_SUPPLIER_LIST;
            } else if ("SupplierCreate".equals(action)) {
                url = Constants.PAGE_SUPPLIER_FORM;
            } else if ("SupplierSave".equals(action)) {
                SupplierDTO supplier = new SupplierDTO();
                supplier.setSupplierCode(request.getParameter("supplierCode"));
                supplier.setSupplierName(request.getParameter("supplierName"));
                supplier.setContactPerson(request.getParameter("contactPerson"));
                supplier.setPhone(request.getParameter("phone"));
                supplier.setEmail(request.getParameter("email"));
                supplier.setAddress(request.getParameter("address"));
                supplier.setTaxCode(request.getParameter("taxCode"));
                String priceStr = request.getParameter("contractPrice");
                if (priceStr != null && !priceStr.isEmpty()) {
                    supplier.setContractPrice(new BigDecimal(priceStr));
                }

                boolean result = supplierDAO.insert(supplier);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Supplier created successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to create supplier!");
                }
                List<SupplierDTO> suppliers = supplierDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, suppliers);
                url = Constants.PAGE_SUPPLIER_LIST;
            } else if ("SupplierEdit".equals(action)) {
                int supplierId = Integer.parseInt(request.getParameter("supplierId"));
                SupplierDTO supplier = supplierDAO.findById(supplierId);
                request.setAttribute(Constants.ATTR_ITEM, supplier);
                url = Constants.PAGE_SUPPLIER_FORM;
            } else if ("SupplierUpdate".equals(action)) {
                SupplierDTO supplier = new SupplierDTO();
                supplier.setSupplierId(Integer.parseInt(request.getParameter("supplierId")));
                supplier.setSupplierCode(request.getParameter("supplierCode"));
                supplier.setSupplierName(request.getParameter("supplierName"));
                supplier.setContactPerson(request.getParameter("contactPerson"));
                supplier.setPhone(request.getParameter("phone"));
                supplier.setEmail(request.getParameter("email"));
                supplier.setAddress(request.getParameter("address"));
                supplier.setTaxCode(request.getParameter("taxCode"));
                String priceStr = request.getParameter("contractPrice");
                if (priceStr != null && !priceStr.isEmpty()) {
                    supplier.setContractPrice(new BigDecimal(priceStr));
                }

                boolean result = supplierDAO.update(supplier);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Supplier updated successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to update supplier!");
                }
                List<SupplierDTO> suppliers = supplierDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, suppliers);
                url = Constants.PAGE_SUPPLIER_LIST;
            } else if ("SupplierDelete".equals(action)) {
                int supplierId = Integer.parseInt(request.getParameter("supplierId"));
                boolean result = supplierDAO.delete(supplierId);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Supplier deleted successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to delete supplier!");
                }
                List<SupplierDTO> suppliers = supplierDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, suppliers);
                url = Constants.PAGE_SUPPLIER_LIST;
            }

        } catch (Exception e) {
            log("Error at SupplierController: " + e.toString());
            request.setAttribute(Constants.ATTR_ERROR, "System error occurred!");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
