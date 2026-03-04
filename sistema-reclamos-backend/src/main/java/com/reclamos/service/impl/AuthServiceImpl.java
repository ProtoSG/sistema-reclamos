package com.reclamos.service.impl;

import com.reclamos.dao.UsuarioDAO;
import com.reclamos.dto.request.AuthRequestDTO;
import com.reclamos.dto.request.CreateUsuarioRequestDTO;
import com.reclamos.dto.response.AuthResponseDTO;
import com.reclamos.service.AuthService;

public class AuthServiceImpl implements AuthService {

  private UsuarioDAO dao;

  public AuthServiceImpl() {
    this.dao = new UsuarioDAO();
  }

  @Override
  public AuthResponseDTO register(CreateUsuarioRequestDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'register'");
  }

  @Override
  public AuthResponseDTO login(AuthRequestDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'login'");
  }

  @Override
  public AuthResponseDTO refreshToken(String refreshToken) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
  }

  
}
