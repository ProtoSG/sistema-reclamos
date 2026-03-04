package com.reclamos.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAtencionRequestDTO(
  @NotBlank(message = "El titulo es obligatorio")
  String titulo,

  @NotBlank(message = "La descripcion es obligatorio")
  String descripcion
) {}
