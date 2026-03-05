package com.reclamos.dto.response;

import java.math.BigDecimal;

import com.reclamos.enums.ReclamoEstado;

public record ReclamoResponseDTO(
    Integer id,
    String tipoDocumento,
    String numeroDocumento,
    String nombres,
    String apellidos,
    String direccion,
    String telefono,
    String email,
    String tipoBien,
    BigDecimal montoReclamado,
    String descripcionReclamo,
    String pedidoConsumidor,
    ReclamoEstado estado
) {}
