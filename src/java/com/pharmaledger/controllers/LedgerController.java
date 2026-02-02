package com.pharmaledger.controllers;

import com.pharmaledger.dao.StockLedgerDAO;
import com.pharmaledger.dto.StockLedgerDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ledger Controller - Audit log management
 */
@WebServlet(name = "LedgerController", urlPatterns = { "/LedgerController" })
public class LedgerController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_LEDGER_LIST;
        String action = request.getParameter("action");

        try {
            StockLedgerDAO ledgerDAO = new StockLedgerDAO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if ("LedgerSearch".equals(action)) {
                String fromStr = request.getParameter("fromDate");
                String toStr = request.getParameter("toDate");

                Date fromDate = (fromStr == null || fromStr.isEmpty())
                        ? new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000)
                        : sdf.parse(fromStr);
                Date toDate = (toStr == null || toStr.isEmpty()) ? new Date() : sdf.parse(toStr);

                List<StockLedgerDTO> ledger = ledgerDAO.searchByDateRange(fromDate, toDate);
                request.setAttribute(Constants.ATTR_LIST, ledger);
                url = Constants.PAGE_LEDGER_LIST;
            }

        } catch (Exception e) {
            log("Error at LedgerController: " + e.toString());
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
