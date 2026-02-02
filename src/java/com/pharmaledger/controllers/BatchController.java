package com.pharmaledger.controllers;

import com.pharmaledger.dao.BatchDAO;
import com.pharmaledger.dto.BatchDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Batch Controller - CRUD operations for batches
 */
@WebServlet(name = "BatchController", urlPatterns = { "/BatchController" })
public class BatchController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_BATCH_LIST;
        String action = request.getParameter("action");

        try {
            BatchDAO batchDAO = new BatchDAO();

            if ("BatchList".equals(action)) {
                int drugId = Integer.parseInt(request.getParameter("drugId"));
                List<BatchDTO> batches = batchDAO.findByDrug(drugId);
                request.setAttribute(Constants.ATTR_LIST, batches);
                url = Constants.PAGE_BATCH_LIST;
            } else if ("BatchNearExpiry".equals(action)) {
                int days = Integer.parseInt(request.getParameter("days"));
                List<BatchDTO> batches = batchDAO.findNearExpiry(days);
                request.setAttribute(Constants.ATTR_LIST, batches);
                request.setAttribute("searchDays", days);
                url = Constants.PAGE_NEAR_EXPIRY_SEARCH;
            } else if ("BatchCreate".equals(action)) {
                url = Constants.PAGE_BATCH_FORM;
            } else if ("BatchSave".equals(action)) {
                BatchDTO batch = new BatchDTO();
                batch.setDrugId(Integer.parseInt(request.getParameter("drugId")));
                batch.setBatchNumber(request.getParameter("batchNumber"));
                // Parse dates - implement date parsing as needed
                batch.setInitialQuantity(Integer.parseInt(request.getParameter("initialQuantity")));
                batch.setCurrentQuantity(batch.getInitialQuantity());

                boolean result = batchDAO.insert(batch);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Batch created successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to create batch!");
                }
                url = Constants.PAGE_BATCH_LIST;
            } else if ("BatchEdit".equals(action)) {
                int batchId = Integer.parseInt(request.getParameter("batchId"));
                BatchDTO batch = batchDAO.findById(batchId);
                request.setAttribute(Constants.ATTR_ITEM, batch);
                url = Constants.PAGE_BATCH_FORM;
            } else if ("BatchDelete".equals(action)) {
                int batchId = Integer.parseInt(request.getParameter("batchId"));
                boolean result = batchDAO.delete(batchId);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Batch deleted successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to delete batch!");
                }
                url = Constants.PAGE_BATCH_LIST;
            }

        } catch (Exception e) {
            log("Error at BatchController: " + e.toString());
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
