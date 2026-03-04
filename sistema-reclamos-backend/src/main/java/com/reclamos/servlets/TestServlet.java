package com.reclamos.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.reclamos.entities.Reclamo;
import com.reclamos.utils.EMFUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    res.setContentType("text/html; charset=UTF-8");
    PrintWriter out = res.getWriter();

    try {
      EntityManager em = EMFUtil.getEntityManager();

      List<Reclamo> reclamos = em.createQuery(
          "SELECT r FROM Reclamo r", 
          Reclamo.class
      ).getResultList();

      out.println("<html><body>");
      out.println("<h1>Test de Conexión - Sistema de Reclamos</h1>");
      out.println("<p>✅ Conexión exitosa a la base de datos</p>");
      out.println("<p>Total de reclamos: " + reclamos.size() + "</p>");
      out.println("</body></html>");

      em.close();
    } catch (Exception e) {
      out.println("<html><body>");
      out.println("<h1>❌ Error de Conexión</h1>");
      out.println("<pre>" + e.getMessage() + "</pre>");
      out.println("</body></html>");
      e.printStackTrace();
    }
  }
}
