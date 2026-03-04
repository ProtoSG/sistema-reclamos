package com.reclamos.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.reclamos.dao.AtencionDAO;
import com.reclamos.dao.ReclamoDAO;
import com.reclamos.dto.request.CreateAtencionRequestDTO;
import com.reclamos.dto.response.AtencionResponseDTO;
import com.reclamos.entities.Atencion;
import com.reclamos.entities.Reclamo;
import com.reclamos.mapper.AtencionMapper;
import com.reclamos.service.AtencionService;

public class AtencionServiceImpl implements AtencionService {
  private AtencionDAO dao;
  private AtencionMapper mapper;
  private ReclamoDAO reclamoDAO;

  public AtencionServiceImpl() {
    this.dao = new AtencionDAO();
    this.mapper = new AtencionMapper();
    this.reclamoDAO = new ReclamoDAO();
  }

  @Override
  public AtencionResponseDTO create(Integer reclamoId, CreateAtencionRequestDTO dto) {
    Reclamo reclamo = reclamoDAO.findById(reclamoId)
      .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

    Atencion atencion = mapper.toEntity(dto);
    atencion.setReclamo(reclamo);

    return mapper.toDto(dao.save(atencion));
  }

  @Override
  public List<AtencionResponseDTO> getAllByReclamoId(Integer id) {
    return dao.findAll(id).stream()
      .map(mapper::toDto)
      .toList();
  }

}
