package com.reclamos.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reclamos.dto.request.AuthRequestDTO;
import com.reclamos.dto.request.CreateUsuarioRequestDTO;
import com.reclamos.dto.response.AuthResponseDTO;
import com.reclamos.service.AuthService;
import com.reclamos.service.impl.AuthServiceImpl;
import com.reclamos.utils.GsonUtil;
import com.reclamos.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.Set;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
  
  private AuthService authService;
  private Gson gson;

  @Override
  public void init() {
    this.authService = new AuthServiceImpl();
    this.gson = GsonUtil.getGson();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Configurar CORS
    configureCors(resp);
    
    resp.setContentType("application/json; charset=UTF-8");

    String pathInfo = req.getPathInfo();

    if (pathInfo == null || pathInfo.equals("/")) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("{\"error\":\"Endpoint no especificado\"}");
      return;
    }

    try {
      switch (pathInfo) {
        case "/register":
          handleRegister(req, resp);
          break;
        case "/login":
          handleLogin(req, resp);
          break;
        case "/refresh":
          handleRefresh(req, resp);
          break;
        default:
          resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          resp.getWriter().write("{\"error\":\"Endpoint no encontrado\"}");
      }
    } catch (IllegalArgumentException e) {
      handleClientError(resp, e.getMessage());
    } catch (Exception e) {
      handleServerError(resp, e);
    }
  }

  private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Leer y parsear body
    CreateUsuarioRequestDTO dto = gson.fromJson(req.getReader(), CreateUsuarioRequestDTO.class);

    // Validar DTO
    Set<ConstraintViolation<CreateUsuarioRequestDTO>> violations = ValidationUtil.validate(dto);
    if (!violations.isEmpty()) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("{\"error\":\"" + ValidationUtil.getErrorMessage(violations) + "\"}");
      return;
    }

    // Procesar registro
    try {
      AuthResponseDTO response = authService.register(dto);
      resp.setStatus(HttpServletResponse.SC_CREATED);
      resp.getWriter().write(gson.toJson(response));
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("ya existe")) {
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
      } else {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Leer y parsear body
    AuthRequestDTO dto = gson.fromJson(req.getReader(), AuthRequestDTO.class);

    // Validar DTO
    Set<ConstraintViolation<AuthRequestDTO>> violations = ValidationUtil.validate(dto);
    if (!violations.isEmpty()) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("{\"error\":\"" + ValidationUtil.getErrorMessage(violations) + "\"}");
      return;
    }

    // Procesar login
    try {
      AuthResponseDTO response = authService.login(dto);
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().write(gson.toJson(response));
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("inactivo")) {
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
      } else {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  private void handleRefresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Leer y parsear body
    JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
    
    if (jsonObject == null || !jsonObject.has("refreshToken")) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write("{\"error\":\"refreshToken es requerido\"}");
      return;
    }

    String refreshToken = jsonObject.get("refreshToken").getAsString();

    // Procesar refresh
    try {
      AuthResponseDTO response = authService.refreshToken(refreshToken);
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().write(gson.toJson(response));
    } catch (IllegalArgumentException e) {
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    configureCors(resp);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private void configureCors(HttpServletResponse resp) {
    resp.setHeader("Access-Control-Allow-Origin", "*");
    resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
  }

  private void handleClientError(HttpServletResponse resp, String message) throws IOException {
    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    resp.getWriter().write("{\"error\":\"" + message + "\"}");
  }

  private void handleServerError(HttpServletResponse resp, Exception e) throws IOException {
    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    resp.getWriter().write("{\"error\":\"Error interno del servidor: " + e.getMessage() + "\"}");
    e.printStackTrace();
  }

}
