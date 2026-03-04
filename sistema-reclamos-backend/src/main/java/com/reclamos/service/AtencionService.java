package com.reclamos.service;

import java.util.List;

import com.reclamos.dto.request.CreateAtencionRequestDTO;
import com.reclamos.dto.response.AtencionResponseDTO;

public interface AtencionService {

  public AtencionResponseDTO create(Integer reclamoId, CreateAtencionRequestDTO dto);
  public List<AtencionResponseDTO> getAllByReclamoId(Integer id);

}
