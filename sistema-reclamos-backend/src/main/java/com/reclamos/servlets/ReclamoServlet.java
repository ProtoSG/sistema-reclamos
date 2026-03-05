package com.reclamos.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.reclamos.dto.request.CreateAtencionRequestDTO;
import com.reclamos.dto.request.CreateReclamoRequestDTO;
import com.reclamos.dto.request.UpdateStateReclamoRequestDTO;
import com.reclamos.dto.response.AtencionResponseDTO;
import com.reclamos.dto.response.ReclamoResponseDTO;
import com.reclamos.service.AtencionService;
import com.reclamos.service.ReclamoService;
import com.reclamos.service.impl.AtencionServiceImpl;
import com.reclamos.service.impl.ReclamoServiceImpl;
import com.reclamos.utils.GsonUtil;
import com.reclamos.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

@WebServlet("/api/reclamos/*")
public class ReclamoServlet extends HttpServlet {
  private ReclamoService service;
  private AtencionService atencionService;
  private Gson gson;

  @Override
  public void init() {
      this.service = new ReclamoServiceImpl();
      this.atencionService = new AtencionServiceImpl();
      this.gson = GsonUtil.getGson();
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Endpoint protegido - verificar autenticación
    Integer userId = (Integer) req.getAttribute("userId");
    if (userId == null) {
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      resp.setContentType("application/json; charset=UTF-8");
      resp.getWriter().write("{\"error\":\"Autenticación requerida\"}");
      return;
    }

    String pathInfo = req.getPathInfo();

    if (pathInfo == null || pathInfo.equals("/")) {
      resp.setStatus(400);

      String message = """
        {
          "error": "ID requerida"
        }
      """;

      resp.getWriter().write(message);
      return;
    } 

    try {
      Integer id = Integer.parseInt(pathInfo.substring(1));
      service.delete(id);
      resp.setStatus(204);
    } catch (Exception e) {
      resp.setStatus(404);
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json; charset=UTF-8");

    String pathInfo = req.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
          List<ReclamoResponseDTO> reclamos = service.getAll();
          resp.getWriter().write(gson.toJson(reclamos));
          return;
      }

      String[] pathParts = pathInfo.substring(1).split("/");

      if (pathParts.length == 2 && pathParts[1].equals("atenciones")) {
          // Endpoint protegido - verificar autenticación
          Integer userId = (Integer) req.getAttribute("userId");
          if (userId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Autenticación requerida\"}");
            return;
          }

          Integer id = Integer.parseInt(pathParts[0]);
          List<AtencionResponseDTO> atenciones = atencionService.getAllByReclamoId(id);
          resp.getWriter().write(gson.toJson(atenciones));
          return;
      }

      if (pathParts.length == 1) {
          Integer id = Integer.parseInt(pathParts[0]);
          ReclamoResponseDTO reclamo = service.getById(id);
          resp.getWriter().write(gson.toJson(reclamo));
          return;
      }

      resp.setStatus(404);
      resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");

    } catch (NumberFormatException e) {
      resp.setStatus(400);
      resp.getWriter().write("{\"error\":\"ID inválido\"}");
    } catch (RuntimeException e) {
      resp.setStatus(404);
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json; charset=UTF-8");

    String pathInfo = req.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        CreateReclamoRequestDTO reclamo = gson.fromJson(req.getReader(), CreateReclamoRequestDTO.class);

        Set<ConstraintViolation<CreateReclamoRequestDTO>> violations = ValidationUtil.validate(reclamo);
        if (!violations.isEmpty()) {
            resp.setStatus(400);
            String errors = ValidationUtil.getErrorMessage(violations);
            resp.getWriter().write("{\"error\":\"Validación fallida: " + errors + "\"}");
            return;
        }

        ReclamoResponseDTO reclamoSaved = service.create(reclamo);
        resp.setStatus(201);
        resp.getWriter().write(gson.toJson(reclamoSaved));
        return;
      }

      String[] pathParts = pathInfo.substring(1).split("/");

      if (pathParts.length == 2 && pathParts[1].equals("atenciones")) {
          // Endpoint protegido - verificar autenticación
          Integer userId = (Integer) req.getAttribute("userId");
          if (userId == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Autenticación requerida\"}");
            return;
          }

          Integer id = Integer.parseInt(pathParts[0]);

          CreateAtencionRequestDTO createAtencion = gson.fromJson(req.getReader(), CreateAtencionRequestDTO.class);

          Set<ConstraintViolation<CreateAtencionRequestDTO>> violations = ValidationUtil.validate(createAtencion);
          if (!violations.isEmpty()) {
              resp.setStatus(400);
              String errors = ValidationUtil.getErrorMessage(violations);
              resp.getWriter().write("{\"error\":\"Validación fallida: " + errors + "\"}");
              return;
          }

          AtencionResponseDTO atencion = atencionService.create(id, createAtencion);
          resp.setStatus(201);
          resp.getWriter().write(gson.toJson(atencion));
          return;
      }

      resp.setStatus(404);
      resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
    } catch (NumberFormatException e) {
      resp.setStatus(400);
      resp.getWriter().write("{\"error\":\"ID inválido\"}");
    } catch (RuntimeException e) {
      resp.setStatus(404);
      resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json; charset=UTF-8");

    String pathInfo = req.getPathInfo();

    if (pathInfo == null || pathInfo.equals("/")) {
      resp.setStatus(400);
      resp.getWriter().write("{\"error\":\"ID requerido\"}");
      return;
    }

    try {
      String[] pathParts = pathInfo.substring(1).split("/");
      Integer id = Integer.parseInt(pathParts[0]);

      if (pathParts.length == 2 && pathParts[1].equals("estado")) {
        // Endpoint protegido - verificar autenticación
        Integer userId = (Integer) req.getAttribute("userId");
        if (userId == null) {
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          resp.getWriter().write("{\"error\":\"Autenticación requerida\"}");
          return;
        }

        UpdateStateReclamoRequestDTO stateDTO = gson.fromJson(req.getReader(), UpdateStateReclamoRequestDTO.class);

        Set<ConstraintViolation<UpdateStateReclamoRequestDTO>> violations = ValidationUtil.validate(stateDTO);

        if (!violations.isEmpty()) {
          resp.setStatus(400);
          String errors = ValidationUtil.getErrorMessage(violations);
          resp.getWriter().write("{\"error\":\"Validación fallida: " + errors + "\"}");
          return;
        }

        ReclamoResponseDTO updated = service.updateState(id, stateDTO.reclamoEstado());
        resp.getWriter().write(gson.toJson(updated));
      } else if (pathParts.length == 1) {
        // Endpoint protegido - verificar autenticación
        Integer userId = (Integer) req.getAttribute("userId");
        if (userId == null) {
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          resp.getWriter().write("{\"error\":\"Autenticación requerida\"}");
          return;
        }

        CreateReclamoRequestDTO reclamo = gson.fromJson(req.getReader(), CreateReclamoRequestDTO.class);

        ReclamoResponseDTO reclamoUpdated = service.update(id, reclamo);
        resp.getWriter().write(gson.toJson(reclamoUpdated));
      } else {
        resp.setStatus(404);
        resp.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
      }
    } catch (NumberFormatException e) {
        resp.setStatus(400);
        resp.getWriter().write("{\"error\":\"ID inválido\"}");
    } catch (Exception e) {
        resp.setStatus(400);
        resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

}
