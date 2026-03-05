package com.reclamos.dto.response;

public record AuthResponseDTO(
  String username,
  String accessToken,
  String refreshToken
) {}
