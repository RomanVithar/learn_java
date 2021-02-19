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
import java.sql.SQLException;

@WebServlet("/sendData")
public class MainServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OwnerPetDAO ownerPetDAO = new OwnerPetDAO();
        Owner owner = new Owner();
        owner.setName(req.getParameter("clientName"));
        Pet pet = new Pet();
        pet.setName(req.getParameter("petName"));
        pet.setType(req.getParameter("type"));
        owner.addPet(pet);
        try {
            ownerPetDAO.writeOwner(owner);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
