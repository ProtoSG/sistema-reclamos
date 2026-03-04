package com.reclamos.dto.request;

public record CreateUsuarioRequestDTO(
  String username,
  String password,
  String nombre
) {}
