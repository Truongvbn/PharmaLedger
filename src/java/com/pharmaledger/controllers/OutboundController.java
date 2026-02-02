package com.pharmaledger.controllers;

import com.pharmaledger.dao.OutboundOrderDAO;
import com.pharmaledger.dto.OutboundOrderDTO;
import com.pharmaledger.dto.UserDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Outbound Controller - Export management
 */
@WebServlet(name = "OutboundController", urlPatterns = { "/OutboundController" })
public class OutboundController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_OUTBOUND_LIST;
        String action = request.getParameter("action");

        try {
            OutboundOrderDAO outboundDAO = new OutboundOrderDAO();
            UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.SESSION_USER);

            if ("OutboundList".equals(action)) {
                List<OutboundOrderDTO> orders = outboundDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, orders);
                url = Constants.PAGE_OUTBOUND_LIST;
            } else if ("OutboundCreate".equals(action)) {
                url = Constants.PAGE_OUTBOUND_FORM;
            } else if ("OutboundSave".equals(action)) {
                OutboundOrderDTO order = new OutboundOrderDTO();
                order.setOrderCode(request.getParameter("orderCode"));
                order.setOrderDate(new Date());
                order.setPurpose(request.getParameter("purpose"));
                order.setDestinationDept(request.getParameter("destinationDept"));
                order.setNotes(request.getParameter("notes"));
                order.setCreatedBy(user.getUserId());

                int orderId = outboundDAO.insert(order);
                if (orderId > 0) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Outbound order created!");
                    url = "MainController?action=OutboundDetail&orderId=" + orderId;
                    response.sendRedirect(url);
                    return;
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to create order!");
                    url = Constants.PAGE_OUTBOUND_LIST;
                }
            } else if ("OutboundDetail".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                OutboundOrderDTO order = outboundDAO.findById(orderId);
                request.setAttribute(Constants.ATTR_ITEM, order);
                url = Constants.PAGE_OUTBOUND_DETAIL;
            } else if ("OutboundApprove".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                boolean result = outboundDAO.updateStatus(orderId, "APPROVED", user.getUserId());
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Order approved successfully!");
                }
                List<OutboundOrderDTO> orders = outboundDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, orders);
                url = Constants.PAGE_OUTBOUND_LIST;
            }

        } catch (Exception e) {
            log("Error at OutboundController: " + e.toString());
            request.setAttribute(Constants.ATTR_ERROR, "System error occurred!");
        } finally {
            if (!response.isCommitted()) {
                request.getRequestDispatcher(url).forward(request, response);
            }
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
