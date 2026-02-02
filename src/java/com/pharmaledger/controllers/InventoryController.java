package com.pharmaledger.controllers;

import com.pharmaledger.dao.InventoryBalanceDAO;
import com.pharmaledger.dto.InventoryBalanceDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Inventory Controller - Dashboard and search operations
 */
@WebServlet(name = "InventoryController", urlPatterns = { "/InventoryController" })
public class InventoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_INVENTORY_DASHBOARD;
        String action = request.getParameter("action");

        try {
            InventoryBalanceDAO inventoryDAO = new InventoryBalanceDAO();

            if ("InventoryDashboard".equals(action)) {
                List<InventoryBalanceDTO> inventory = inventoryDAO.findAllWithDetails();
                request.setAttribute(Constants.ATTR_LIST, inventory);
                url = Constants.PAGE_INVENTORY_DASHBOARD;
            } else if ("StockSearch".equals(action)) {
                String keyword = request.getParameter("keyword");
                if (keyword != null && !keyword.trim().isEmpty()) {
                    List<InventoryBalanceDTO> inventory = inventoryDAO.searchStock(keyword);
                    request.setAttribute(Constants.ATTR_LIST, inventory);
                }
                url = Constants.PAGE_STOCK_SEARCH;
            } else if ("LowStockSearch".equals(action)) {
                // TODO: Implement low stock search
                url = Constants.PAGE_LOW_STOCK_SEARCH;
            }

        } catch (Exception e) {
            log("Error at InventoryController: " + e.toString());
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
