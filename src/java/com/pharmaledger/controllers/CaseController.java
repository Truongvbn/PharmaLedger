package com.pharmaledger.controllers;

import com.pharmaledger.dao.CaseTicketDAO;
import com.pharmaledger.dto.CaseTicketDTO;
import com.pharmaledger.dto.UserDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Case Controller - Case ticket workflow
 */
@WebServlet(name = "CaseController", urlPatterns = { "/CaseController" })
public class CaseController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_CASE_LIST;
        String action = request.getParameter("action");

        try {
            CaseTicketDAO caseDAO = new CaseTicketDAO();

            if ("CaseList".equals(action)) {
                List<CaseTicketDTO> cases = caseDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, cases);
                url = Constants.PAGE_CASE_LIST;
            } else if ("CaseOpen".equals(action)) {
                List<CaseTicketDTO> cases = caseDAO.findOpen();
                request.setAttribute(Constants.ATTR_LIST, cases);
                url = Constants.PAGE_CASE_LIST;
            } else if ("CaseDetail".equals(action)) {
                // TODO: Implement case detail
                url = Constants.PAGE_CASE_DETAIL;
            } else if ("CaseAssign".equals(action)) {
                int caseId = Integer.parseInt(request.getParameter("caseId"));
                // TODO: Load assignee list
                request.setAttribute("caseId", caseId);
                url = Constants.PAGE_CASE_ASSIGN;
            } else if ("CaseDoAssign".equals(action)) {
                int caseId = Integer.parseInt(request.getParameter("caseId"));
                int assigneeId = Integer.parseInt(request.getParameter("assigneeId"));

                boolean result = caseDAO.assign(caseId, assigneeId);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Case assigned successfully!");
                }
                List<CaseTicketDTO> cases = caseDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, cases);
                url = Constants.PAGE_CASE_LIST;
            } else if ("CaseClose".equals(action)) {
                int caseId = Integer.parseInt(request.getParameter("caseId"));
                request.setAttribute("caseId", caseId);
                url = Constants.PAGE_CASE_CLOSE;
            } else if ("CaseDoClose".equals(action)) {
                int caseId = Integer.parseInt(request.getParameter("caseId"));
                String resolution = request.getParameter("resolution");
                String evidence = request.getParameter("evidence");
                UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.SESSION_USER);

                boolean result = caseDAO.close(caseId, resolution, evidence, user.getUserId());
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Case closed successfully!");
                }
                List<CaseTicketDTO> cases = caseDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, cases);
                url = Constants.PAGE_CASE_LIST;
            }

        } catch (Exception e) {
            log("Error at CaseController: " + e.toString());
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
