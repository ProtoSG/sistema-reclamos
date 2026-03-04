package com.reclamos.service;

import java.util.List;

import com.reclamos.dto.request.CreateReclamoRequestDTO;
import com.reclamos.dto.response.ReclamoResponseDTO;
import com.reclamos.enums.ReclamoEstado;

public interface ReclamoService {
  public List<ReclamoResponseDTO> getAll();
  public ReclamoResponseDTO getById(Integer id);
  public ReclamoResponseDTO create(CreateReclamoRequestDTO requestDTO);
  public ReclamoResponseDTO update(Integer id, CreateReclamoRequestDTO requestDTO);
  public void delete(Integer id);
  public ReclamoResponseDTO updateState(Integer id, ReclamoEstado estado);
}
