package com.reclamos.dto.response;

import java.time.LocalDateTime;

public record AtencionResponseDTO(
  Integer id,
  LocalDateTime fecha,
  String titulo,
  String descripcion
) {}
