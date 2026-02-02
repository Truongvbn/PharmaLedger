package com.pharmaledger.controllers;

import com.pharmaledger.utils.Constants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Report Controller - Analytics and stats
 */
@WebServlet(name = "ReportController", urlPatterns = { "/ReportController" })
public class ReportController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_REPORT_DASHBOARD;
        String action = request.getParameter("action");

        try {
            if ("ReportDashboard".equals(action)) {
                // TODO: Aggregate summary stats
                url = Constants.PAGE_REPORT_DASHBOARD;
            } else if ("ReportStockout".equals(action)) {
                url = Constants.PAGE_REPORT_STOCKOUT;
            } else if ("ReportExpiry".equals(action)) {
                url = Constants.PAGE_REPORT_EXPIRY;
            }

        } catch (Exception e) {
            log("Error at ReportController: " + e.toString());
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
