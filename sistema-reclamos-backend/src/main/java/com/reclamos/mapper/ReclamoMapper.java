package com.reclamos.mapper;

import com.reclamos.dto.request.CreateReclamoRequestDTO;
import com.reclamos.dto.response.ReclamoResponseDTO;
import com.reclamos.entities.Reclamo;

public class ReclamoMapper {

  public ReclamoResponseDTO toDto(Reclamo reclamo) {
    if (reclamo == null) return null;

    return new ReclamoResponseDTO(
        reclamo.getId(),
        reclamo.getTipoDocumento(), 
        reclamo.getNumeroDocumento(),
        reclamo.getNombres(),
        reclamo.getApellidos(), 
        reclamo.getDireccion(),
        reclamo.getTelefono(),
        reclamo.getEmail(), 
        reclamo.getTipoBien(), 
        reclamo.getMontoReclamado(), 
        reclamo.getDescripcionReclamo(), 
        reclamo.getPedidoConsumidor(),
        reclamo.getEstado()
    );
  }

  public Reclamo toEntity(CreateReclamoRequestDTO dto) {
    if (dto == null) return null;

    return Reclamo.builder()
      .tipoDocumento(dto.tipoDocumento())
      .numeroDocumento(dto.numeroDocumento())
      .nombres(dto.nombres())
      .apellidos(dto.apellidos())
      .direccion(dto.direccion())
      .telefono(dto.telefono())
      .email(dto.email())
      .tipoBien(dto.tipoBien())
      .montoReclamado(dto.montoReclamado())
      .descripcionReclamo(dto.descripcionReclamo())
      .pedidoConsumidor(dto.pedidoConsumidor())
      .build();
  }
}
