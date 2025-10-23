package com.amc.servlets;

import com.amc.dao.AMCDAO;
import com.amc.model.AMCDetails;
import com.amc.model.AMCItemDetails;
import com.amc.model.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AMCServlet extends HttpServlet {
    
    private AMCDAO amcDAO;
    private SimpleDateFormat dateFormat;
    
    @Override
    public void init() throws ServletException {
        amcDAO = new AMCDAO();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("list".equals(action)) {
            listAMCs(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            saveAMCDetails(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
    
    private void saveAMCDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            AMCDetails amc = new AMCDetails();
            amc.setControlNo(request.getParameter("controlNo"));
            amc.setAmcUserId(request.getParameter("amcUserId"));
            amc.setDirectorate(user.getDirectorate());
            amc.setAmcLetterNo(request.getParameter("amcLetterNo"));
            amc.setSubject(request.getParameter("subject"));
            amc.setGemOrderNo(request.getParameter("gemOrderNo"));
            amc.setPaymentMode(request.getParameter("paymentMode"));
            amc.setCreatedBy(user.getUsername());
            
            // Parse dates
            try {
                if (request.getParameter("amcLetterDate") != null && !request.getParameter("amcLetterDate").isEmpty()) {
                    amc.setAmcLetterDate(new Date(dateFormat.parse(request.getParameter("amcLetterDate")).getTime()));
                }
                if (request.getParameter("gemOrderDate") != null && !request.getParameter("gemOrderDate").isEmpty()) {
                    amc.setGemOrderDate(new Date(dateFormat.parse(request.getParameter("gemOrderDate")).getTime()));
                }
                if (request.getParameter("amcEffectFrom") != null && !request.getParameter("amcEffectFrom").isEmpty()) {
                    amc.setAmcEffectFrom(new Date(dateFormat.parse(request.getParameter("amcEffectFrom")).getTime()));
                }
                if (request.getParameter("validUpto") != null && !request.getParameter("validUpto").isEmpty()) {
                    amc.setValidUpto(new Date(dateFormat.parse(request.getParameter("validUpto")).getTime()));
                }
            } catch (ParseException e) {
                throw new ServletException("Invalid date format", e);
            }
            
            // Parse AMC order value
            String orderValue = request.getParameter("amcOrderValue");
            if (orderValue != null && !orderValue.isEmpty()) {
                amc.setAmcOrderValue(new BigDecimal(orderValue));
            }
            
            // Parse item details
            List<AMCItemDetails> itemDetailsList = parseItemDetails(request, amc.getControlNo());
            amc.setItemDetailsList(itemDetailsList);
            
            boolean success = amcDAO.saveAMC(amc);
            
            Map<String, Object> jsonResponse = new HashMap<>();
            if (success) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "AMC details saved successfully");
                jsonResponse.put("controlNo", amc.getControlNo());
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Failed to save AMC details");
            }
            
            Gson gson = new Gson();
            out.write(gson.toJson(jsonResponse));
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error: " + e.getMessage());
            
            Gson gson = new Gson();
            out.write(gson.toJson(errorResponse));
        } finally {
            out.flush();
        }
    }
    
    private List<AMCItemDetails> parseItemDetails(HttpServletRequest request, String controlNo) {
        List<AMCItemDetails> itemDetailsList = new ArrayList<>();
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        int maxRowNum = 0;
        for (String paramName : parameterMap.keySet()) {
            if (paramName.startsWith("itemName_")) {
                try {
                    int rowNum = Integer.parseInt(paramName.substring("itemName_".length()));
                    maxRowNum = Math.max(maxRowNum, rowNum);
                } catch (NumberFormatException e) {
                    // Ignore invalid row numbers
                }
            }
        }
        
        for (int i = 1; i <= maxRowNum; i++) {
            String itemName = request.getParameter("itemName_" + i);
            String make = request.getParameter("make_" + i);
            String model = request.getParameter("model_" + i);
            String quantityStr = request.getParameter("quantity_" + i);
            
            if (itemName != null && !itemName.trim().isEmpty() &&
                make != null && !make.trim().isEmpty() &&
                model != null && !model.trim().isEmpty() &&
                quantityStr != null && !quantityStr.trim().isEmpty()) {
                
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    AMCItemDetails item = new AMCItemDetails(controlNo, i, itemName.trim(), 
                                                           make.trim(), model.trim(), quantity);
                    itemDetailsList.add(item);
                } catch (NumberFormatException e) {
                    // Skip invalid quantity
                }
            }
        }
        
        return itemDetailsList;
    }
    
    private void listAMCs(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<AMCDetails> amcList = amcDAO.getAllAMCs();
        request.setAttribute("amcList", amcList);
        request.getRequestDispatcher("list-amc.jsp").forward(request, response);
    }
}
