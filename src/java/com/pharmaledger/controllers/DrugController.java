package com.pharmaledger.controllers;

import com.pharmaledger.dao.DrugDAO;
import com.pharmaledger.dto.DrugDTO;
import com.pharmaledger.utils.Constants;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Drug Controller - CRUD operations for drugs
 */
@WebServlet(name = "DrugController", urlPatterns = { "/DrugController" })
public class DrugController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = Constants.PAGE_DRUG_LIST;
        String action = request.getParameter("action");

        try {
            DrugDAO drugDAO = new DrugDAO();

            if ("DrugList".equals(action)) {
                List<DrugDTO> drugs = drugDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, drugs);
                url = Constants.PAGE_DRUG_LIST;
            } else if ("DrugSearch".equals(action)) {
                String keyword = request.getParameter("keyword");
                List<DrugDTO> drugs = drugDAO.search(keyword);
                request.setAttribute(Constants.ATTR_LIST, drugs);
                url = Constants.PAGE_DRUG_LIST;
            } else if ("DrugCreate".equals(action)) {
                url = Constants.PAGE_DRUG_FORM;
            } else if ("DrugSave".equals(action)) {
                DrugDTO drug = new DrugDTO();
                drug.setDrugCode(request.getParameter("drugCode"));
                drug.setDrugName(request.getParameter("drugName"));
                drug.setActiveIngredient(request.getParameter("activeIngredient"));
                drug.setDosageForm(request.getParameter("dosageForm"));
                drug.setStrength(request.getParameter("strength"));
                drug.setUnit(request.getParameter("unit"));
                drug.setMinStock(Integer.parseInt(request.getParameter("minStock")));
                drug.setDescription(request.getParameter("description"));

                boolean result = drugDAO.insert(drug);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Drug created successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to create drug!");
                }
                List<DrugDTO> drugs = drugDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, drugs);
                url = Constants.PAGE_DRUG_LIST;
            } else if ("DrugEdit".equals(action)) {
                int drugId = Integer.parseInt(request.getParameter("drugId"));
                DrugDTO drug = drugDAO.findById(drugId);
                request.setAttribute(Constants.ATTR_ITEM, drug);
                url = Constants.PAGE_DRUG_FORM;
            } else if ("DrugUpdate".equals(action)) {
                DrugDTO drug = new DrugDTO();
                drug.setDrugId(Integer.parseInt(request.getParameter("drugId")));
                drug.setDrugCode(request.getParameter("drugCode"));
                drug.setDrugName(request.getParameter("drugName"));
                drug.setActiveIngredient(request.getParameter("activeIngredient"));
                drug.setDosageForm(request.getParameter("dosageForm"));
                drug.setStrength(request.getParameter("strength"));
                drug.setUnit(request.getParameter("unit"));
                drug.setMinStock(Integer.parseInt(request.getParameter("minStock")));
                drug.setDescription(request.getParameter("description"));

                boolean result = drugDAO.update(drug);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Drug updated successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to update drug!");
                }
                List<DrugDTO> drugs = drugDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, drugs);
                url = Constants.PAGE_DRUG_LIST;
            } else if ("DrugDelete".equals(action)) {
                int drugId = Integer.parseInt(request.getParameter("drugId"));
                boolean result = drugDAO.delete(drugId);
                if (result) {
                    request.setAttribute(Constants.ATTR_SUCCESS, "Drug deleted successfully!");
                } else {
                    request.setAttribute(Constants.ATTR_ERROR, "Failed to delete drug!");
                }
                List<DrugDTO> drugs = drugDAO.findAll();
                request.setAttribute(Constants.ATTR_LIST, drugs);
                url = Constants.PAGE_DRUG_LIST;
            } else if ("DrugDetail".equals(action)) {
                int drugId = Integer.parseInt(request.getParameter("drugId"));
                DrugDTO drug = drugDAO.findById(drugId);
                request.setAttribute(Constants.ATTR_ITEM, drug);
                url = Constants.PAGE_DRUG_DETAIL;
            }

        } catch (Exception e) {
            log("Error at DrugController: " + e.toString());
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
