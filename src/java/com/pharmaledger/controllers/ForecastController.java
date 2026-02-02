package com.pharmaledger.controllers;

import com.pharmaledger.dao.ForecastDAO;
import com.pharmaledger.dto.ForecastSeriesDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Forecast Controller - AI prediction management
 */
@WebServlet(name = "ForecastController", urlPatterns = { "/ForecastController" })
public class ForecastController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_FORECAST_DASHBOARD;
        String action = request.getParameter("action");

        try {
            ForecastDAO forecastDAO = new ForecastDAO();

            if ("ForecastDashboard".equals(action)) {
                List<ForecastSeriesDTO> seriesList = forecastDAO.findLatestSeries();
                request.setAttribute(Constants.ATTR_LIST, seriesList);
                url = Constants.PAGE_FORECAST_DASHBOARD;
            } else if ("ForecastDetail".equals(action)) {
                int drugId = Integer.parseInt(request.getParameter("drugId"));
                ForecastSeriesDTO series = forecastDAO.findSeriesByDrug(drugId);
                request.setAttribute(Constants.ATTR_ITEM, series);
                url = Constants.PAGE_FORECAST_DETAIL;
            } else if ("ReorderSuggest".equals(action)) {
                // TODO: Implement reorder logic based on forecast
                url = Constants.PAGE_REORDER_SUGGEST;
            }

        } catch (Exception e) {
            log("Error at ForecastController: " + e.toString());
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
