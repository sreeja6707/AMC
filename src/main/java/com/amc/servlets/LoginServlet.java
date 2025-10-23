package com.amc.servlets;

import com.amc.dao.UserDAO;
import com.amc.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String directorate = request.getParameter("directorate");
        
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            directorate == null || directorate.trim().isEmpty()) {
            
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        User user = userDAO.authenticate(username.trim(), password, directorate);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("directorate", user.getDirectorate());
            session.setMaxInactiveInterval(30 * 60);
            
            System.out.println("User logged in: " + user.getUsername() + " - " + user.getDirectorate());
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "Invalid username, password, or directorate");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
