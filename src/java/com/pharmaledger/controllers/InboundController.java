package com.pharmaledger.controllers;

import com.pharmaledger.dao.InboundOrderDAO;
import com.pharmaledger.dto.InboundOrderDTO;
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
 * Inbound Controller - Import management
 */
@WebServlet(name = "InboundController", urlPatterns = { "/InboundController" })
public class InboundController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_INBOUND_LIST;
        String action = request.getParameter("action");

        try {
            InboundOrderDAO inboundDAO = new InboundOrderDAO();
            UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.SESSION_USER);

            if ("InboundList".equals(action)) {
                List<InboundOrderDTO> orders = inboundDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, orders);
                url = Constants.PAGE_INBOUND_LIST;
            } else if ("InboundCreate".equals(action)) {
                // TODO: Load suppliers list for select box
                url = Constants.PAGE_INBOUND_FORM;
            } else if ("InboundSave".equals(action)) {
                InboundOrderDTO order = new InboundOrderDTO();
                order.setOrderCode(request.getParameter("orderCode"));
                order.setSupplierId(Integer.parseInt(request.getParameter("supplierId")));
                order.setOrderDate(new Date()); // Default to now
                order.setNotes(request.getParameter("notes"));
                order.setCreatedBy(user.getUserId());

                int orderId = inboundDAO.insert(order);
                if (orderId > 0) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Inbound order created!");
                    // Redirect to detail to add lines
                    url = "MainController?action=InboundDetail&orderId=" + orderId;
                    response.sendRedirect(url);
                    return;
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to create order!");
                    url = Constants.PAGE_INBOUND_LIST;
                }
            } else if ("InboundDetail".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                InboundOrderDTO order = inboundDAO.findById(orderId);
                request.setAttribute(Constants.ATTR_ITEM, order);
                url = Constants.PAGE_INBOUND_DETAIL;
            } else if ("InboundApprove".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                boolean result = inboundDAO.updateStatus(orderId, "APPROVED", user.getUserId());
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Order approved successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to approve order!");
                }
                List<InboundOrderDTO> orders = inboundDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, orders);
                url = Constants.PAGE_INBOUND_LIST;
            }

        } catch (Exception e) {
            log("Error at InboundController: " + e.toString());
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
