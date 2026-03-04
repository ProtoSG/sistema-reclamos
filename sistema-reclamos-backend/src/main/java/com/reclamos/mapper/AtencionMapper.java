package com.reclamos.mapper;

import com.reclamos.dto.request.CreateAtencionRequestDTO;
import com.reclamos.dto.response.AtencionResponseDTO;
import com.reclamos.entities.Atencion;

public class AtencionMapper {

  public AtencionResponseDTO toDto(Atencion atencion) {
    if (atencion == null) return null;

    return new AtencionResponseDTO(
      atencion.getId(), 
      atencion.getFecha(), 
      atencion.getTitulo(), 
      atencion.getDescripcion()
    );
  }

  public Atencion toEntity(CreateAtencionRequestDTO dto) {
    if (dto == null) return null;

    return Atencion.builder()
      .titulo(dto.titulo())
      .descripcion(dto.descripcion())
      .build();
  }

}
