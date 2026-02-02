package com.pharmaledger.controllers;

import com.pharmaledger.utils.Constants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Front Controller - Routes all requests to appropriate controllers
 */
@WebServlet(name = "MainController", urlPatterns = { "/MainController" })
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_LOGIN;

        try {
            String action = request.getParameter("action");

            if (action == null) {
                url = Constants.PAGE_LOGIN;
            }
            // Auth actions
            else if (Constants.ACTION_LOGIN.equals(action)) {
                url = Constants.CONTROLLER_LOGIN;
            } else if (Constants.ACTION_LOGOUT.equals(action)) {
                request.getSession().invalidate();
                url = Constants.PAGE_LOGIN;
            }
            // Drug actions
            else if (action.startsWith("Drug")) {
                url = Constants.CONTROLLER_DRUG;
            }
            // Batch actions
            else if (action.startsWith("Batch")) {
                url = Constants.CONTROLLER_BATCH;
            }
            // Supplier actions
            else if (action.startsWith("Supplier")) {
                url = Constants.CONTROLLER_SUPPLIER;
            }
            // Location actions
            else if (action.startsWith("Location")) {
                url = Constants.CONTROLLER_LOCATION;
            }
            // Inventory actions
            else if (action.startsWith("Inventory") || action.startsWith("Stock")) {
                url = Constants.CONTROLLER_INVENTORY;
            }
            // Inbound actions
            else if (action.startsWith("Inbound")) {
                url = Constants.CONTROLLER_INBOUND;
            }
            // Outbound actions
            else if (action.startsWith("Outbound")) {
                url = Constants.CONTROLLER_OUTBOUND;
            }
            // Ledger actions
            else if (action.startsWith("Ledger")) {
                url = Constants.CONTROLLER_LEDGER;
            }
            // Alert actions
            else if (action.startsWith("Alert") || action.startsWith("Rule")) {
                url = Constants.CONTROLLER_ALERT;
            }
            // Case actions
            else if (action.startsWith("Case")) {
                url = Constants.CONTROLLER_CASE;
            }
            // Forecast actions
            else if (action.startsWith("Forecast") || action.startsWith("Reorder")) {
                url = Constants.CONTROLLER_FORECAST;
            }
            // Report actions
            else if (action.startsWith("Report")) {
                url = Constants.CONTROLLER_REPORT;
            }

        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
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

    @Override
    public String getServletInfo() {
        return "PharmaLedger Main Controller";
    }
}
