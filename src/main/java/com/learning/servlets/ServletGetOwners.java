package com.learning.servlets;

import com.learning.dao.OwnerPetDAO;
import com.learning.entity.Owner;
import com.learning.entity.Pet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/getOwners")
public class ServletGetOwners extends HttpServlet {
    private OwnerPetDAO ownerPetDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ownerPetDAO = new OwnerPetDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Set<Owner> set = ownerPetDAO.readOwners();
        PrintWriter writer = resp.getWriter();
        writer.println(set);
    }
}
