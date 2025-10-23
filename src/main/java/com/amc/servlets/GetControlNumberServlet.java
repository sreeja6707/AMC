package com.amc.servlets;

import com.amc.dao.AMCDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetControlNumberServlet extends HttpServlet {
    
    private AMCDAO amcDAO;
    
    @Override
    public void init() throws ServletException {
        amcDAO = new AMCDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }
        
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            int nextSequence = amcDAO.getNextSequenceNumber(year, month);
            String sequenceNumber = String.format("%03d", nextSequence);
            
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("sequenceNumber", sequenceNumber);
            
            Gson gson = new Gson();
            out.write(gson.toJson(jsonResponse));
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Database error: " + e.getMessage());
            
            Gson gson = new Gson();
            out.write(gson.toJson(errorResponse));
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.flush();
        }
    }
}
