package com.reclamos.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.reclamos.dao.ReclamoDAO;
import com.reclamos.dto.request.CreateReclamoRequestDTO;
import com.reclamos.dto.response.ReclamoResponseDTO;
import com.reclamos.entities.Reclamo;
import com.reclamos.enums.ReclamoEstado;
import com.reclamos.mapper.ReclamoMapper;
import com.reclamos.service.ReclamoService;

public class ReclamoServiceImpl implements ReclamoService {
  private ReclamoDAO dao;
  private ReclamoMapper mapper;

  public ReclamoServiceImpl() {
    this.dao = new ReclamoDAO();
    this.mapper = new ReclamoMapper();
  }

  @Override
  public List<ReclamoResponseDTO> getAll() {
    return dao.findAll().stream()
      .map(mapper::toDto)
      .toList();
  }

  @Override
  public ReclamoResponseDTO getById(Integer id) {
    Reclamo reclamo = dao.findById(id)
      .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

    return mapper.toDto(reclamo);
  }

  @Override
  public ReclamoResponseDTO create(CreateReclamoRequestDTO requestDTO) {
    Reclamo reclamo = mapper.toEntity(requestDTO);

    return mapper.toDto(dao.save(reclamo));
  }

  @Override
  public ReclamoResponseDTO update(Integer id, CreateReclamoRequestDTO requestDTO) {
    Reclamo reclamo = dao.findById(id)
      .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

    if (requestDTO.apellidos() != null) reclamo.setApellidos(requestDTO.apellidos());
    if (requestDTO.tipoDocumento() != null) reclamo.setTipoDocumento(requestDTO.tipoDocumento());
    if (requestDTO.numeroDocumento() != null) reclamo.setNumeroDocumento(requestDTO.numeroDocumento());
    if (requestDTO.nombres() != null) reclamo.setNombres(requestDTO.nombres());
    if (requestDTO.direccion() != null) reclamo.setDireccion(requestDTO.direccion());
    if (requestDTO.telefono() != null) reclamo.setTelefono(requestDTO.telefono());
    if (requestDTO.email() != null) reclamo.setEmail(requestDTO.email());
    if (requestDTO.tipoBien() != null) reclamo.setTipoBien(requestDTO.tipoBien());
    if (requestDTO.montoReclamado() != null) reclamo.setMontoReclamado(requestDTO.montoReclamado());
    if (requestDTO.descripcionReclamo() != null) reclamo.setDescripcionReclamo(requestDTO.descripcionReclamo());
    if (requestDTO.pedidoConsumidor() != null) reclamo.setPedidoConsumidor(requestDTO.pedidoConsumidor());

    return mapper.toDto(dao.update(reclamo));
  }

  @Override
  public void delete(Integer id) {
    Reclamo reclamo = dao.findById(id)
      .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

    dao.delete(reclamo);
  }


  @Override
  public ReclamoResponseDTO updateState(Integer id, ReclamoEstado estado) {
    Reclamo reclamo = dao.findById(id)
      .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));
    reclamo.setEstado(estado);

    if (estado == ReclamoEstado.CERRADO) {
      reclamo.setFechaCierre(LocalDateTime.now());
    }

    return mapper.toDto(dao.update(reclamo));
  }

}
