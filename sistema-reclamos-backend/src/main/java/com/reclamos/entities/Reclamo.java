package com.reclamos.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import com.reclamos.enums.ReclamoEstado;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "reclamo")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Reclamo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @CreationTimestamp
  private LocalDateTime fechaRegistro;
  private String tipoDocumento;
  private String numeroDocumento;
  private String nombres;
  private String apellidos;
  private String direccion;
  private String telefono;
  private String email;
  private String tipoBien;
  private BigDecimal montoReclamado;
  private String descripcionReclamo;
  private String pedidoConsumidor;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ReclamoEstado estado = ReclamoEstado.PENDIENTE;

  private LocalDateTime fechaCierre;
}
