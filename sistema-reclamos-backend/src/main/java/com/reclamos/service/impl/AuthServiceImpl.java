package com.reclamos.service.impl;

import com.reclamos.dao.SesionDAO;
import com.reclamos.dao.UsuarioDAO;
import com.reclamos.dto.request.AuthRequestDTO;
import com.reclamos.dto.request.CreateUsuarioRequestDTO;
import com.reclamos.dto.response.AuthResponseDTO;
import com.reclamos.entities.Sesion;
import com.reclamos.entities.Usuario;
import com.reclamos.service.AuthService;
import com.reclamos.utils.JwtUtil;

import jakarta.persistence.NoResultException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AuthServiceImpl implements AuthService {

  private final UsuarioDAO usuarioDAO;
  private final SesionDAO sesionDAO;

  public AuthServiceImpl() {
    this.usuarioDAO = new UsuarioDAO();
    this.sesionDAO = new SesionDAO();
  }

  @Override
  public AuthResponseDTO register(CreateUsuarioRequestDTO dto) {
    // Validar que el username no exista
    try {
      usuarioDAO.findByUsername(dto.username());
      throw new IllegalArgumentException("El username ya existe");
    } catch (NoResultException e) {
      // Username disponible, continuar
    }

    // Encriptar password con BCrypt (jBCrypt)
    String hashedPassword = BCrypt.hashpw(dto.password(), BCrypt.gensalt());

    // Crear Usuario
    Usuario usuario = Usuario.builder()
        .username(dto.username())
        .password(hashedPassword)
        .nombre(dto.nombre())
        .activo(true)
        .build();

    // Guardar Usuario
    usuario = usuarioDAO.save(usuario);

    // Generar tokens JWT
    String accessToken = JwtUtil.generateAccessToken(usuario);
    String refreshToken = JwtUtil.generateRefreshToken(usuario);

    // Crear y guardar Sesion
    Sesion sesion = Sesion.builder()
        .usuario(usuario)
        .token(refreshToken)
        .fechaExpiracion(LocalDateTime.ofInstant(
            JwtUtil.extractExpiration(refreshToken).toInstant(), 
            ZoneId.systemDefault()))
        .activo(true)
        .build();

    sesionDAO.save(sesion);

    // Retornar respuesta
    return new AuthResponseDTO(
        usuario.getUsername(),
        accessToken,
        refreshToken
    );
  }

  @Override
  public AuthResponseDTO login(AuthRequestDTO dto) {
    // Buscar Usuario por username
    Usuario usuario;
    try {
      usuario = usuarioDAO.findByUsername(dto.username());
    } catch (NoResultException e) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }

    // Validar que usuario esté activo
    if (!usuario.getActivo()) {
      throw new IllegalArgumentException("Usuario inactivo");
    }

    // Verificar password con BCrypt (jBCrypt)
    if (!BCrypt.checkpw(dto.password(), usuario.getPassword())) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }

    // Generar tokens JWT
    String accessToken = JwtUtil.generateAccessToken(usuario);
    String refreshToken = JwtUtil.generateRefreshToken(usuario);

    // Crear y guardar nueva Sesion
    Sesion sesion = Sesion.builder()
        .usuario(usuario)
        .token(refreshToken)
        .fechaExpiracion(LocalDateTime.ofInstant(
            JwtUtil.extractExpiration(refreshToken).toInstant(), 
            ZoneId.systemDefault()))
        .activo(true)
        .build();

    sesionDAO.save(sesion);

    // Retornar respuesta
    return new AuthResponseDTO(
        usuario.getUsername(),
        accessToken,
        refreshToken
    );
  }

  @Override
  public AuthResponseDTO refreshToken(String refreshToken) {
    // Validar que token sea válido
    if (!JwtUtil.validateToken(refreshToken)) {
      throw new IllegalArgumentException("Token inválido");
    }

    // Validar que sea un refresh token
    if (!JwtUtil.isRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("El token no es un refresh token");
    }

    // Buscar Sesion activa por token
    Sesion sesion = sesionDAO.findByToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada"));

    // Validar que sesión esté activa
    if (!sesion.getActivo()) {
      throw new IllegalArgumentException("Sesión inactiva");
    }

    // Validar que sesión no esté expirada
    if (sesion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Sesión expirada");
    }

    // Extraer Usuario de la Sesion
    Usuario usuario = sesion.getUsuario();

    // Validar que usuario esté activo
    if (!usuario.getActivo()) {
      throw new IllegalArgumentException("Usuario inactivo");
    }

    // Generar nuevo Access Token
    String newAccessToken = JwtUtil.generateAccessToken(usuario);

    // Retornar respuesta con nuevo access token y mismo refresh token
    return new AuthResponseDTO(
        usuario.getUsername(),
        newAccessToken,
        refreshToken
    );
  }

}

