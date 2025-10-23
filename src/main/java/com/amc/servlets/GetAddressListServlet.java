package com.amc.servlets;

import com.amc.dao.AddressListDAO;
import com.amc.model.AddressList;
import com.amc.model.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetAddressListServlet extends HttpServlet {
    
    private AddressListDAO addressListDAO;
    
    @Override
    public void init() throws ServletException {
        addressListDAO = new AddressListDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            List<AddressList> addressList = addressListDAO.getAddressListByDirectorate(user.getDirectorate());
            
            Gson gson = new Gson();
            out.write(gson.toJson(addressList));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
