package com.pharmaledger.controllers;

import com.pharmaledger.dao.UserDAO;
import com.pharmaledger.dto.UserDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login Controller
 */
@WebServlet(name = "LoginController", urlPatterns = { "/LoginController" })
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_LOGIN;

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDAO userDAO = new UserDAO();
            UserDTO loginUser = userDAO.checkLogin(username, password);

            if (loginUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.SESSION_USER, loginUser);
                session.setAttribute(Constants.SESSION_ROLE, loginUser.getRoleId());

                String roleId = loginUser.getRoleId();
                if (Constants.ROLE_ADMIN.equals(roleId)) {
                    url = Constants.PAGE_INVENTORY_DASHBOARD;
                } else if (Constants.ROLE_PHARMACIST.equals(roleId)) {
                    url = Constants.PAGE_INVENTORY_DASHBOARD;
                } else if (Constants.ROLE_AUDITOR.equals(roleId)) {
                    url = Constants.PAGE_LEDGER_LIST;
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Your role is not supported!");
                }
            } else {
                request.setAttribute(Constants.ATTR_ERROR, "Incorrect username or password!");
            }

        } catch (Exception e) {
            log("Error at LoginController: " + e.toString());
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
