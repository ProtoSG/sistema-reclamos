package com.reclamos.dto.request;

import com.reclamos.enums.ReclamoEstado;

import jakarta.validation.constraints.NotNull;

public record UpdateStateReclamoRequestDTO(
  @NotNull(message = "El estado es obligatorio: PENDIENTE, EN_PROCESO, ATENDIDO, CERRADO")
  ReclamoEstado reclamoEstado
) {}
