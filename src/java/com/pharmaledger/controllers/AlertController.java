package com.pharmaledger.controllers;

import com.pharmaledger.dao.AlertEventDAO;
import com.pharmaledger.dao.AlertRuleDAO;
import com.pharmaledger.dto.AlertEventDTO;
import com.pharmaledger.dto.AlertRuleDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alert Controller - Rules and events management
 */
@WebServlet(name = "AlertController", urlPatterns = { "/AlertController" })
public class AlertController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_ALERTS_LIST;
        String action = request.getParameter("action");

        try {
            AlertRuleDAO ruleDAO = new AlertRuleDAO();
            AlertEventDAO eventDAO = new AlertEventDAO();

            if ("RuleList".equals(action)) {
                List<AlertRuleDTO> rules = ruleDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, rules);
                url = Constants.PAGE_RULE_LIST;
            } else if ("RuleCreate".equals(action)) {
                url = Constants.PAGE_RULE_FORM;
            } else if ("RuleSave".equals(action)) {
                AlertRuleDTO rule = new AlertRuleDTO();
                rule.setRuleName(request.getParameter("ruleName"));
                rule.setRuleType(request.getParameter("ruleType"));
                rule.setThreshold(Integer.parseInt(request.getParameter("threshold")));
                rule.setThresholdUnit(request.getParameter("thresholdUnit"));
                rule.setSeverity(request.getParameter("severity"));

                boolean result = ruleDAO.insert(rule);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Rule created successfully!");
                }
                List<AlertRuleDTO> rules = ruleDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, rules);
                url = Constants.PAGE_RULE_LIST;
            } else if ("AlertsList".equals(action)) {
                List<AlertEventDTO> events = eventDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, events);
                url = Constants.PAGE_ALERTS_LIST;
            } else if ("AlertsOpen".equals(action)) {
                List<AlertEventDTO> events = eventDAO.findOpen();
                request.setAttribute(Constants.ATTR_LIST, events);
                url = Constants.PAGE_ALERTS_LIST;
            } else if ("AlertDetail".equals(action)) {
                // TODO: Implement alert detail
                url = Constants.PAGE_ALERT_DETAIL;
            } else if ("AlertExplain".equals(action)) {
                // TODO: Implement alert explanation (AI)
                url = Constants.PAGE_ALERT_EXPLAIN;
            }

        } catch (Exception e) {
            log("Error at AlertController: " + e.toString());
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
