package com.reclamos.filters;

import com.google.gson.Gson;
import com.reclamos.dao.SesionDAO;
import com.reclamos.entities.Sesion;
import com.reclamos.utils.GsonUtil;
import com.reclamos.utils.JwtUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebFilter(
    filterName = "authFilter",
    urlPatterns = {"/api/reclamos/*"}
)
public class AuthFilter implements Filter {

  private SesionDAO sesionDAO;
  private Gson gson;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.sesionDAO = new SesionDAO();
    this.gson = GsonUtil.getGson();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // Configurar CORS
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

    // Manejar preflight OPTIONS request
    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
      httpResponse.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    // Usar getServletPath() para obtener la ruta sin el context path
    String servletPath = httpRequest.getServletPath();
    String pathInfo = httpRequest.getPathInfo();
    String method = httpRequest.getMethod();

    // Construir la ruta completa del servlet
    String fullPath = servletPath + (pathInfo != null ? pathInfo : "");

    // Permitir rutas públicas de reclamos
    if (isPublicRoute(fullPath, method)) {
      chain.doFilter(request, response);
      return;
    }

    // Extraer header Authorization
    String authHeader = httpRequest.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      sendUnauthorized(httpResponse, "Token no proporcionado");
      return;
    }

    // Extraer token
    String token = authHeader.substring(7);

    // Validar token con JwtUtil
    if (!JwtUtil.validateToken(token)) {
      sendUnauthorized(httpResponse, "Token inválido o expirado");
      return;
    }

    // Verificar que sea un access token
    if (!JwtUtil.isAccessToken(token)) {
      sendUnauthorized(httpResponse, "Se requiere un access token");
      return;
    }

    // Extraer userId del token
    Integer userId = JwtUtil.extractUserId(token);

    // Agregar userId como atributo del request
    httpRequest.setAttribute("userId", userId);

    // Continuar con la cadena de filtros
    chain.doFilter(request, response);
  }

  private boolean isPublicRoute(String path, String method) {
    // Rutas de autenticación siempre públicas
    if (path.startsWith("/api/auth/") || path.contains("/api/auth/")) {
      return true;
    }

    // Test endpoint público
    if (path.contains("/test")) {
      return true;
    }

    // Rutas públicas del formulario HTML
    if (path.startsWith("/public/") || path.contains("/public/")) {
      return true;
    }

    // Rutas públicas de reclamos
    // POST /api/reclamos - Crear reclamo público
    // GET /api/reclamos - Listar reclamos público
    if (path.equals("/api/reclamos") || path.equals("/api/reclamos/")) {
      return "GET".equals(method) || "POST".equals(method);
    }

    // GET /api/reclamos/{id} es público (solo números, sin subrutas)
    // Ejemplo: /api/reclamos/1 o /api/reclamos/123
    if (path.matches("/api/reclamos/\\d+/?$")) {
      return "GET".equals(method);
    }

    // Todas las demás rutas requieren autenticación:
    // - PUT /api/reclamos/{id}
    // - PUT /api/reclamos/{id}/estado
    // - DELETE /api/reclamos/{id}
    // - POST /api/reclamos/{id}/atenciones
    // - GET /api/reclamos/{id}/atenciones
    return false;
  }

  private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter().write("{\"error\":\"" + message + "\"}");
  }

  @Override
  public void destroy() {
    // Cleanup si es necesario
  }

}
