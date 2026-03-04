package com.reclamos.dto.request;

import java.math.BigDecimal;

public record CreateReclamoRequestDTO(
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
    String pedidoConsumidor
) {}
