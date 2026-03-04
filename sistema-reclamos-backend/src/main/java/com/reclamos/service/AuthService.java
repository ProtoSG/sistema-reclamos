package com.reclamos.service;

import com.reclamos.dto.request.AuthRequestDTO;
import com.reclamos.dto.request.CreateUsuarioRequestDTO;
import com.reclamos.dto.response.AuthResponseDTO;

public interface AuthService {

  AuthResponseDTO register(CreateUsuarioRequestDTO dto);
  AuthResponseDTO login(AuthRequestDTO dto);
  AuthResponseDTO refreshToken(final String refreshToken);

}
